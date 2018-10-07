name := "scalafix-benchmark"

version := "0.1"

scalaVersion := "2.12.7"

enablePlugins(JmhPlugin)

libraryDependencies ++= Seq(
  "org.scalameta" %% "contrib" % "4.0.0-22-7283fda3-20181007-0913"
)