name := """epickur-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers += DefaultMavenRepository
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers ++= Seq("RoundEights" at "http://maven.spikemark.net/roundeights")

libraryDependencies ++= Seq(
	jdbc,
	cache,
	ws,
	"org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
	"com.googlecode.libphonenumber" % "libphonenumber" % "7.7.2",
	"org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
	"org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % Test,
	"org.specs2" %% "specs2-core" % "3.0" % Test,
	"org.specs2" %% "specs2-mock" % "3.6.5" % Test
)
libraryDependencies ++= Seq("com.roundeights" %% "hasher" % "1.2.0")
libraryDependencies += specs2 % Test
fork in run := true
