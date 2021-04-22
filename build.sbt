name := "Challenges"

version := "0.1"

scalaVersion := "2.13.5"

lazy val global = project
  .in(file("."))
  .aggregate(
    elevator
  )

lazy val elevator = project
  .settings(settings,
    libraryDependencies ++= commonDependencies
  )


lazy val decimalzip = project
  .settings(settings,
    libraryDependencies ++= commonDependencies
  )

lazy val password = project
  .settings(settings,
    libraryDependencies ++= commonDependencies
  )

lazy val smallestnumberequallength = project
  .settings(settings,
    libraryDependencies ++= commonDependencies
  )

lazy val squareroots = project
  .settings(settings,
    libraryDependencies ++= commonDependencies
  )
  
lazy val arbitrage = project
  .settings(settings,
    libraryDependencies ++= commonDependencies ++
      Seq(
      "org.http4s" %% "http4s-dsl" % "0.21.21",
      "org.http4s" %% "http4s-circe" % "0.21.21",
      "org.http4s" %% "http4s-blaze-client" % "0.21.21",
      "org.typelevel" %% "squants" % "1.7.4",
      "com.github.valskalla" %% "odin-core" % "0.11.0",
      "io.estatico" %% "newtype" % "0.4.4"
    )
  )

lazy val settings = Seq(
  scalacOptions ++= Seq(
    "-unchecked",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-deprecation",
    "-encoding",
    "utf8"
  ),
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val dependencies =
  new {
    val scalactic = "org.scalactic" %% "scalactic" % "3.0.5"
    val scalatest = "org.scalatest" %% "scalatest" % "3.0.5"
  }

lazy val commonDependencies = Seq(
  dependencies.scalactic,
  dependencies.scalatest % Test
)