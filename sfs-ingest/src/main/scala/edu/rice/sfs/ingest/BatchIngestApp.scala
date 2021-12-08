package edu.rice.sfs.ingest

import com.audienceproject.spark.dynamodb.implicits.DynamoDBDataFrameWriter
import com.typesafe.config.ConfigFactory
import edu.rice.sfs.common.client.RegistryServiceClient
import edu.rice.sfs.ingest.source.BatchSource
import io.delta.implicits.DeltaDataFrameWriter
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, current_timestamp}
import org.apache.spark.sql.types.DataTypes

import scala.collection.JavaConverters._

object BatchIngestApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val config = ConfigFactory.load()

    val batchSource = BatchSource.apply(spark.sparkContext.getConf)
    val registryService = new RegistryServiceClient(config.getString("external.service.registry-service-endpoint"))
    val featureTable = registryService.getFeatureTable(batchSource.featureTable)

    val entityCol = col(batchSource.entityCol).cast(DataTypes.StringType).as(featureTable.getEntity.getName)
    val deltaFeatureCols = featureTable.getFeatures.asScala.map(f => {
      col(batchSource.featureColMap.getOrElse(f.getName, featureNotFoundExp(f.getName)))
        .cast(SchemaUtils.valueTypeToSparkType(f.getValueType)).as(f.getDeltaTableColName)
    })
    val dynamoFeatureCols = featureTable.getFeatures.asScala.map(f => {
      col(batchSource.featureColMap.getOrElse(f.getName, featureNotFoundExp(f.getName)))
        .cast(SchemaUtils.valueTypeToSparkType(f.getValueType)).as(f.getDynamoTableColName)
    })

    val df = spark.read
      .format(batchSource.format)
      .options(batchSource.options)
      .load(batchSource.path)
      .toDF(batchSource.cols: _*)

    df.printSchema()

    df.select(Seq(entityCol) ++ deltaFeatureCols: _*)
      .withColumn("__updated_at__", current_timestamp())
      .repartition(1)
      .write
      .mode("append")
      .option("mergeSchema", "true")
      .option("overwriteSchema", "true")
      .delta(featureTable.getDeltaTablePath)
    df.select(Seq(entityCol) ++ dynamoFeatureCols: _*)
      .repartition(100)
      .write
      .option("writeBatchSize", 25)
      .option("throughput", 25000)
      .dynamodb(featureTable.getDynamoTableName)
    spark.stop()
  }

  private def entityNotFoundExp[V](entity: String): V = {
    throw new IllegalArgumentException("Can not find entity: " + entity)
  }

  private def featureNotFoundExp[V](feature: String): V = {
    throw new IllegalArgumentException("Can not find feature: " + feature)
  }
}
