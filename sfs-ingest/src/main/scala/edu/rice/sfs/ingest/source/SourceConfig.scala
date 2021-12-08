package edu.rice.sfs.ingest.source

import org.apache.spark.SparkConf

trait SourceConfig extends Serializable {

  type Self

  val configPrefix: String

  def apply(options: Map[String, String]): Self

  def apply(sparkConf: SparkConf): Self = apply(getOptionsFromConf(sparkConf))

  def applyFromPrefixedMap(options: Map[String, String]): Self =
    apply(stripPrefix(options.filterKeys(_.startsWith(configPrefix))))

  protected def getOptionsFromConf(sparkConf: SparkConf): Map[String, String] = {
    stripPrefix(sparkConf.getAll.filter(_._1.startsWith(configPrefix)).toMap)
  }

  def getStringProperty(prop: String, options: Map[String, String])
                       (default: => String = notFoundExp(prop)): String = {
    options.getOrElse(prop, default).trim
  }

  def getIntegerProperty(prop: String, options: Map[String, String])
                        (default: => Int = notFoundExp(prop)): Int = {
    options.get(prop).map(_.toInt).getOrElse(default)
  }

  def getDoubleProperty(prop: String, options: Map[String, String])
                       (default: => Double = notFoundExp(prop)): Double = {
    options.get(prop).map(_.toDouble).getOrElse(default)
  }

  def getBooleanProperty(prop: String, options: Map[String, String])
                        (default: => Boolean = notFoundExp(prop)): Boolean = {
    options.get(prop).map(_.toBoolean).getOrElse(default)
  }

  def getStringListProperty(prop: String,
                            options: Map[String, String],
                            delimiter: String = ",")
                           (default: => Seq[String] = notFoundExp(prop)): Seq[String] = {
    options.get(prop).map(_.split(delimiter).map(_.trim).toSeq).getOrElse(default)
  }

  def getStringMapProperty(prop: String,
                           options: Map[String, String],
                           entryDelimiter: String = ",",
                           kvDelimiter: String = ":")
                          (default: => Map[String, String] = notFoundExp(prop)): Map[String, String] = {
    options.get(prop).map(raw =>
      raw.split(entryDelimiter)
        .map(_.trim.split(kvDelimiter))
        .map({ case Array(k, v) => (k.trim, v.trim) })
        .toMap
    ).getOrElse(default)
  }

  private def stripPrefix(options: Map[String, String]): Map[String, String] =
    options.map(kv => (kv._1.toLowerCase.stripPrefix(configPrefix), kv._2))

  private def notFoundExp[V](prop: String): V = {
    throw new IllegalArgumentException("Can not find property: " + configPrefix + prop)
  }
}
