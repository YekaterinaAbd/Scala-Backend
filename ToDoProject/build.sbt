import com.typesafe.sbt.packager.docker.ExecCmd

enablePlugins(JavaAppPackaging, AshScriptPlugin)

name := "todoscalaproject"

dockerBaseImage := "openjdk:8-jre-alpine"
packageName in Docker := "todoscalaproject"


version := "0.1"

scalaVersion := "2.13.5"


scalaVersion := "2.12.6"

javacOptions ++= Seq("-source", "1.8")

val akkaVersion = "2.6.8"
val akkaHttpVersion = "10.2.4"
val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.21.0",

  "org.scalatest" %% "scalatest" % "3.0.5" % Test,

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "net.debasishg" %% "redisclient" % "3.30",

  "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0",

  "io.monix" %% "shade" % "1.10.0"

)

dockerCommands := dockerCommands.value.map {
  case ExecCmd("CMD", _ @ _*) =>
    ExecCmd("CMD", "/opt/docker/bin/todoscalaproject")
  case other =>
    other
}

//.\deploy.sh todoscalaproject f8768fdbb0b1 349a0f69-7e70-4e39-9e64-0fe6383a7d76

//local dockerize
//sbt docker:publishLocal
//docker run -d -p 9000:9000 todoscalaproject:0.1

//docker ps - get containers
//docker stop container_name

// https://todoscalaproject.herokuapp.com/todos



