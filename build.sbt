name := """epickur-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
	jdbc,
	cache,
	ws,
	"org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
	"org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
	"org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % Test,
	"org.specs2" %% "specs2-core" % "3.0" % Test,
	"org.specs2" %% "specs2-mock" % "3.6.5" % Test
)

fork in run := true
