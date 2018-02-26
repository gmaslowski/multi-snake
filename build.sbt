organization := "com.gmaslowski"
name := "multi-snake"

scalaVersion := "2.12.4"

val akkaV = "2.5.10"
val akkaHttpV = "10.0.11"
val logbackV = "1.2.3"
val scalatestV = "3.0.5"

libraryDependencies ++= Seq(
  // akka
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,

  // logging
  "ch.qos.logback" % "logback-classic" % logbackV,

  // testing
  "org.scalatest" %% "scalatest" % scalatestV % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
)
