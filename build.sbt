javacOptions ++= Seq(
  "-source", "1.8",
  "-target", "1.8",
  "-Xlint"
)

lazy val root = (project in file(".")).
  settings(
    name := "quaich",
    organization := "codes.bytes",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.8",
    retrieveManaged := true
  )


lazy val httpMacros = (project in file("http-macros")).
  settings(
    name := "quaich-http-macros"
  )

lazy val demo = (project in file("demo")).
  settings(
    name := "quaich-demo",
    handlerName := Some("codes.bytes.quaich.demo.DemoHTTPServer::handleRequest"),
    s3Bucket := Some("quaich-demo"),
    roleArn := Some("arn:aws:iam::176770676006:role/lambda_basic_execution")
  ).
  dependsOn(httpApi).
  enablePlugins(AwsLambdaPlugin)


lazy val httpApi = (project in file("http-api")).
  settings(
    name := "quaich-http-api",
    libraryDependencies ++= Seq(
    )
  ).dependsOn(api, httpMacros)

lazy val api = (project in file("api")).
  settings(
    name := "quaich-api",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.0.0",
      "com.amazonaws" % "aws-lambda-java-events" % "1.0.0"
    )
  ).dependsOn(util)

lazy val util = (project in file("util")).
  settings(
    name := "quaich-util",
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.0.0",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.json4s" %% "json4s-jackson" % "3.4.0",
      "commons-io" % "commons-io" % "2.4"
    )
  )


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
