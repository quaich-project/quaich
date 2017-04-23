logLevel := Level.Warn

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

resolvers += Resolver.url("dancingrobot84-bintray", url("http://dl.bintray.com/dancingrobot84/sbt-plugins/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.jcenterRepo

addSbtPlugin("com.dancingrobot84" % "sbt-idea-plugin" % "0.4.0")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

addSbtPlugin("codes.bytes" % "sbt-quartercask-lambda" % "0.0.1-SNAPSHOT")