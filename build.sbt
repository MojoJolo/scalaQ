import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

name := "textteaser_summarizer"

version := "1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
	"com.twitter" %% "finagle-core" % "6.5.2",
	"com.twitter" %% "finagle-http" % "6.5.2",
	"org.slf4j" % "slf4j-api" % "1.7.5",
  	"ch.qos.logback" % "logback-classic" % "1.0.13"
)