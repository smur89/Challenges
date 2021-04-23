name := "Challenges"

version := "0.1"
scalaVersion := "2.13.5"

lazy val global = project.in(file(".")).aggregate(elevator)

lazy val elevator = project
  .settings(settings, libraryDependencies ++= Seq(Dependencies.scalactic, Dependencies.scalatest).flatten)

lazy val decimalzip = project
  .settings(settings, libraryDependencies ++= Seq(Dependencies.scalactic, Dependencies.scalatest).flatten)

lazy val password = project
  .settings(settings, libraryDependencies ++= Seq(Dependencies.scalactic, Dependencies.scalatest).flatten)

lazy val smallestnumberequallength = project
  .settings(settings, libraryDependencies ++= Seq(Dependencies.scalactic, Dependencies.scalatest).flatten)

lazy val squareroots = project.settings(settings, libraryDependencies ++= Seq(Dependencies.scalactic, Dependencies.scalatest).flatten)

lazy val arbitrage = project
  .settings(
    settings,
    libraryDependencies ++= Seq(
      Dependencies.scalactic,
      Dependencies.scalatest,
      Dependencies.http4s,
      Dependencies.squants,
      Dependencies.odin,
      Dependencies.newtype
    ).flatten
  )

lazy val settings = Seq(
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)
