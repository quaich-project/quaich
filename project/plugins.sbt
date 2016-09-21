resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

lazy val demo = (project in file("demo")).dependsOn(plugin)

lazy val plugin = (project in file("aws-lambda-sbt-plugin")).settings(
  sbtPlugin := true,
  scalaVersion := "2.10.6",
  unmanagedSourceDirectories in Compile :=
    mirrorScalaSource((baseDirectory in ThisBuild).value.getParentFile / "aws-lambda-sbt-plugin")
)

def mirrorScalaSource(baseDirectory: File): Seq[File] = {
  val scalaSourceDir = baseDirectory / "src" / "main" / "scala"
  if (scalaSourceDir.exists) scalaSourceDir :: Nil
  else sys.error(s"Missing source directory: $scalaSourceDir")
}


