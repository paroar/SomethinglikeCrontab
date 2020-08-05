enablePlugins(JavaAppPackaging, AshScriptPlugin)
name := "Crontab"

version := "0.1"

scalaVersion := "2.13.3"

dockerBaseImage := "openjdk:8-jre"
packageName := "crontab"

resolvers += Resolver.sonatypeRepo("releases")

val circeVersion = "0.12.3"
val doobieVersion = "0.8.8"

libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "6.2",
  "com.typesafe.akka" %% "akka-stream" % "2.6.8",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12",

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
  "io.spray" %%  "spray-json" % "1.3.5",


  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2"   % doobieVersion

)