import sbt._

object Dependencies {

  object versions {
    val http4s = "0.21.21"
    val newtype = "0.4.4"
    val odin = "0.11.0"
    val scalatest = "3.2.8"
    val squants = "1.7.4"
  }

  val scalactic = Seq("org.scalactic" %% "scalactic" % versions.scalatest)
  val scalatest = Seq("org.scalatest" %% "scalatest" % versions.scalatest % Test)
  val http4s =
    Seq("org.http4s" %% "http4s-dsl", "org.http4s" %% "http4s-circe", "org.http4s" %% "http4s-blaze-client").map(_ % versions.http4s)
  val squants = Seq("org.typelevel" %% "squants" % versions.squants)
  val odin = Seq("com.github.valskalla" %% "odin-core" % versions.odin)
  val newtype = Seq("io.estatico" %% "newtype" % versions.newtype)
}
