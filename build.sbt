name := """scala-lucene-play-example"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.outr" %% "lucene4s" % "1.8.1",
)

coverageExcludedPackages := """controllers\..*Reverse.*;router.Routes.*;"""

testOptions in Test += Tests.Argument("-oS")
