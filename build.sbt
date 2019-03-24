name := "Challenges"

version := "0.1"

scalaVersion := "2.12.8"

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