name := "akka-training"

version := "0.1"


scalaVersion := "2.10.4"

scalacOptions ++= Seq("-deprecation")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.15",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.15",
  "com.typesafe.akka" %% "akka-contrib" % "2.3.15",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.3.15",
  "com.typesafe.akka" %% "akka-remote" % "2.3.15",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.15",
  "com.typesafe.akka" %% "akka-http" % "10.0.1",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.1",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.1"
  //"com.esotericsoftware.kryo" % "kryo" % "2.10"
  /*  "ch.qos.logback" %% "logback-classic" % "1.0.9",
    "com.typesafe" %% "scalalogging-slf4j_2.10" % "1.0.1"*/
)

resolvers ++= Seq(
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Scala-Tools Snapshots" at "http://scala-tools.org/repo-snapshots",
  "Scala Tools Releases" at "http://scala-tools.org/repo-releases"
)