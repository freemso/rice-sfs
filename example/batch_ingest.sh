spark-submit \
  --master "local[4]" \
  --deploy-mode client \
  --class edu.rice.sfs.ingest.BatchIngestApp \
  --name BatchIngestApp \
  --packages com.audienceproject:spark-dynamodb_2.12:1.1.2 \
  --packages io.delta:delta-core_2.12:1.1.0 \
  --conf "spark.driver.extraJavaOptions=-Daws.dynamodb.endpoint=http://localhost:8000/" \
  --conf "spark.sfs.source.batch.options=delimiter:|" \
  --conf "spark.sfs.source.batch.format=csv" \
  --conf "spark.sfs.source.batch.path=/Users/freemso/dev/cloud-536/rice-sfs/example/data/ml-100k/u.user" \
  --conf "spark.sfs.source.batch.cols=id, age, gender, occupation, zip_code" \
  --conf "spark.sfs.source.batch.feature-table=user_feat" \
  --conf "spark.sfs.source.batch.entity-col=id" \
  --conf "spark.sfs.source.batch.feature-col-map=user_age:age,user_gender:gender,user_occupation:occupation,user_zip_code:zip_code" \
  --driver-java-options "-Daws.dynamodb.endpoint=http://localhost:8000/" \
  --driver-memory 1024M \
  --driver-cores 1 \
  --executor-memory 1G \
  ../sfs-ingest/target/sfs-ingest-0.0.1-SNAPSHOT-jar-with-dependencies.jar