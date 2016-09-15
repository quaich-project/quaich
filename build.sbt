
name := "quaich"

val projectVersion        = "0.1-SNAPSHOT"
val scalacticVersion      = "3.0.0"
val scalatestVersion      = "3.0.0"
val json4sVersion         = "3.4.0"
val commonsIOVersion      = "2.4"
val awsLambdaVersion      = "1.0.0"
val scalaMetaVersion      = "1.1.0"
val metaParadiseVersion   = "3.0.0-M5"

lazy val commonSettings = Seq(
  organization := "codes.bytes",
  version := projectVersion,
  scalaVersion := "2.11.8",
  retrieveManaged := true,
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % scalacticVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
    "org.json4s" %% "json4s-jackson" % json4sVersion,
    "commons-io" % "commons-io" % commonsIOVersion
  ),
  scalacOptions := Seq(
    "-encoding",
    "UTF-8",
    "-target:jvm-1.8",
    "-deprecation",
    "-language:_"
  ),
  fork in (Test, run) := true
)


lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "quaich",
    organization := "codes.bytes",
    version := projectVersion
  ).
  aggregate(
    util, httpMacros, httpApi, api, demo
  )


lazy val httpMacros = (project in file("http-macros")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-http-macros",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % scalaMetaVersion
    ),
    addCompilerPlugin("org.scalameta" % "paradise" % metaParadiseVersion cross CrossVersion.full)
  )

lazy val demo = (project in file("demo")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-demo",
    handlerName := Some("codes.bytes.quaich.demo.DemoHTTPServer::handleRequest"),
    s3Bucket := Some("quaich-demo")
  ).
  dependsOn(httpApi).
  enablePlugins(AwsLambdaPlugin)


lazy val httpApi = (project in file("http-api")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-http-api",
    libraryDependencies ++= Seq(
    )
  ).dependsOn(api, httpMacros)

lazy val api = (project in file("api")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-api",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % awsLambdaVersion,
      "com.amazonaws" % "aws-lambda-java-events" % awsLambdaVersion
    )
  ).dependsOn(util)

lazy val util = (project in file("util")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-util",
    libraryDependencies ++= Seq(
    )
  )


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
