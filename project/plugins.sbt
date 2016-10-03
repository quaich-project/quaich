logLevel := Level.Warn

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

resolvers += Resolver.url("dancingrobot84-bintray",
  url("http://dl.bintray.com/dancingrobot84/sbt-plugins/"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.dancingrobot84" % "sbt-idea-plugin" % "0.4.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

addSbtPlugin("codes.bytes" % "sbt-quartercask-lambda" % "0.1-SNAPSHOT")




