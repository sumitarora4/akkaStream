name := "AkkaEssentials"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5"
 )