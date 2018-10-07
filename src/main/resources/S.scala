package scalafix.benchmarks;

package object S {

  private object SparkBuildInfo {

    val (
        spark_version: String,
        spark_branch: String,
        spark_revision: String,
        spark_build_user: String,
        spark_repo_url: String,
        spark_build_date: String) = {

      val resourceStream = Thread.currentThread().getContextClassLoader.
        getResourceAsStream("spark-version-info.properties")
      if (resourceStream == null) {
        throw new SparkException("Could not find spark-version-info.properties")
      }

      try {
        val unknownProp = "<unknown>"
        val props = new Properties()
        props.load(resourceStream)
        (
          props.getProperty("version", unknownProp),
          props.getProperty("branch", unknownProp),
          props.getProperty("revision", unknownProp),
          props.getProperty("user", unknownProp),
          props.getProperty("url", unknownProp),
          props.getProperty("date", unknownProp)
        )
      } catch {
        case e: Exception =>
          throw new SparkException("Error loading properties from spark-version-info.properties", e)
      } finally {
        if (resourceStream != null) {
          try {
            resourceStream.close()
          } catch {
            case e: Exception =>
              throw new SparkException("Error closing spark build info resource stream", e)
          }
        }
      }
    }
  } /* scalafix:ok Foo */

  val SPARK_VERSION = SparkBuildInfo.spark_version
  val SPARK_BRANCH = SparkBuildInfo.spark_branch
  val SPARK_REVISION = SparkBuildInfo.spark_revision
  val SPARK_BUILD_USER = SparkBuildInfo.spark_build_user
  val SPARK_REPO_URL = SparkBuildInfo.spark_repo_url
  val SPARK_BUILD_DATE = SparkBuildInfo.spark_build_date
}
