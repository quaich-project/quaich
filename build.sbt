import sbt.Keys.publishTo

name := "quaich"

val projectVersion        = "0.0.2-SNAPSHOT"
val projectOrg            = "codes.bytes"

val json4sVersion         = "3.5.0.RC1"
val commonsIOVersion      = "2.4"
val awsLambdaVersion      = "1.1.0"
val awsLambdaEventsVer    = "1.3.0"
val awsLambdaLog4jVer     = "1.0.0"
val metaParadiseVersion   = "3.0.0-M8"
val awsSdkVersion         = "1.11.52"

lazy val commonSettings = Seq(
  organization := projectOrg,
  version := projectVersion,
  scalaVersion := "2.12.2",
  retrieveManaged := true,
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.0",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.json4s" %% "json4s-jackson" % json4sVersion,
    "commons-io" % "commons-io" % commonsIOVersion
  ),
  scalacOptions := Seq(
    "-encoding",
    "UTF-8",
    "-target:jvm-1.8",
    "-deprecation",
    "-language:_"/*,
    "-Ymacro-debug-lite"*/
  ),
  fork in (Test, run) := true,

  bintrayOrganization := Some("quaich"),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
)


lazy val macroSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  addCompilerPlugin("org.scalameta" % "paradise" % metaParadiseVersion cross CrossVersion.full),
  scalacOptions ++= Seq(
    "-Xplugin-require:macroparadise"/*,
    "-Ymacro-debug-lite"*/
  )
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "quaich",
    organization := projectOrg,
    version := projectVersion,
    publishArtifact := false,
    publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo"))),
    bintrayRelease := {}
  ).
  aggregate(
    util, http, httpMacros, httpApi, api, demo
  )

lazy val util = (project in file("util")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-util",
    libraryDependencies ++= Seq(
    )
  )

lazy val api = (project in file("api")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-api",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % awsLambdaVersion,
      "com.amazonaws" % "aws-lambda-java-events" % awsLambdaEventsVer,
      "com.amazonaws" % "aws-lambda-java-log4j" % awsLambdaLog4jVer
    )
  ).dependsOn(util)

lazy val httpApi = (project in file("http-api")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-http-api",
    libraryDependencies ++= Seq(
    )
  ).dependsOn(api)

lazy val httpMacros = (project in file("http-macros")).
  settings(commonSettings: _*).
  settings(macroSettings: _*).
  settings(
    name := "quaich-http-macros"
  ).
  dependsOn(httpApi)

lazy val http = (project in file("http")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-http"
  ).
  dependsOn(httpMacros, httpApi).
  aggregate(httpMacros, httpApi)

lazy val demo = (project in file("demo")).
  settings(commonSettings: _*).
  settings(macroSettings: _*).
  settings(
    name := "quaich-demo",
    createAutomatically := true,

    lambdaName := "quaich-http-demo",
    handlerName := "codes.bytes.quaich.demo.http.DemoHTTPServer::handleRequest",
    awsLambdaMemory := 192,
    region := "eu-west-1",

    publishArtifact in (Compile, packageDoc) := false
  ).
  dependsOn(http).
  enablePlugins(AWSLambdaPlugin)

lazy val httpMetadataPlugin = (project in file("http-metadata-plugin")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-http-metadata-plugin"
  )


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}


