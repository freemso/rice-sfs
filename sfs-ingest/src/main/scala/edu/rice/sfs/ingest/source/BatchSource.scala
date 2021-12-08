package edu.rice.sfs.ingest.source

object BatchSource extends SourceConfig {
  override type Self = BatchSource
  override val configPrefix: String = "spark.sfs.source.batch."

  override def apply(options: Map[String, String]): BatchSource = {
    BatchSource(
      options = getStringMapProperty("options", options)(Map()),
      format = getStringProperty("format", options)(),
      path = getStringProperty("path", options)(),
      cols = getStringListProperty("cols", options)(null),
      featureTable = getStringProperty("feature-table", options)(),
      entityCol = getStringProperty("entity-col", options)(),
      featureColMap = getStringMapProperty("feature-col-map", options)(),
    )
  }
}

case class BatchSource
(
  options: Map[String, String],
  format: String,
  path: String,
  cols: Seq[String], // columns in the original file
  featureTable: String, // name of the feature table
  entityCol: String, // entity col name
  featureColMap: Map[String, String], // map from feature name to col name
)
