javacOptions ++= Seq("-source", "1.8",
  "-target", "1.8",
  "-Xlint")

lazy val root = (project in file(".")).
  settings(
    name := "quaich",
    organization := "codes.bytes",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.8",
    retrieveManaged := true,
    libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.0.0",
    libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "1.0.0"
  )

assemblyMergeStrategy in assembly := { 
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
