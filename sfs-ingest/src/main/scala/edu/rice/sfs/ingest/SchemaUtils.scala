package edu.rice.sfs.ingest

import edu.rice.sfs.common.model.ValueType
import edu.rice.sfs.common.model.ValueType._
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.DataTypes._

object SchemaUtils {

  def valueTypeToSparkType(v: ValueType): DataType = {
    v match {
      case BYTES => BinaryType
      case STRING => StringType
      case INT => IntegerType
      case LONG => LongType
      case FLOAT => FloatType
      case DOUBLE => DoubleType
      case BOOL => BooleanType
      case UNIX_TIMESTAMP => TimestampType
      case BYTES_LIST => createArrayType(BinaryType)
      case STRING_LIST => createArrayType(StringType)
      case INT_LIST => createArrayType(IntegerType)
      case LONG_LIST => createArrayType(LongType)
      case FLOAT_LIST => createArrayType(FloatType)
      case DOUBLE_LIST => createArrayType(DoubleType)
      case BOOL_LIST => createArrayType(BooleanType)
      case UNIX_TIMESTAMP_LIST => createArrayType(TimestampType)
      case _ => NullType
    }
  }
}