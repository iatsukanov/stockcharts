import sbt._
import sbt.Keys._

object Dependencies {
  import GlobalExclusions._

  val akkaVersion = "2.5.4"
  val akkaHttpVersion = "10.0.10"
  private val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion

  val commonDeps = libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.3.1",
    "com.iheart" %% "ficus" % "1.4.0",
    "ch.qos.logback" % "logback-classic" % "1.2.3",

    "org.json4s" %% "json4s-native" % "3.5.3",

    "com.typesafe.akka" %% "akka-stream-kafka" % "0.17",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  )

  val extractorDeps = libraryDependencies ++= Seq(
    akkaHttp
  )

  val uiDeps = libraryDependencies ++= Seq(
    akkaHttp
  )

  val simulationDeps = libraryDependencies ++= Seq(
    "eu.verdelhan" % "ta4j" % "0.9",
    akkaHttp,
    "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
  )

  val kafkaDeps = libraryDependencies ++= Seq(
    "net.manub" %% "scalatest-embedded-kafka-streams" % "0.15.1"
  ).withoutLog4j

}

object GlobalExclusions {

  private val log4jDeps = Seq(
    ExclusionRule("log4j", "log4j"),
    ExclusionRule("org.slf4j", "slf4j-log4j12")
  )
  private val fromLog4jToLogbackDep = "org.slf4j" % "log4j-over-slf4j" % "1.7.12"

  implicit class ModuleIDGlobalExclusions(val deps: Seq[ModuleID]) {
    def without(exclusions: Seq[ExclusionRule]): Seq[ModuleID] = deps.map(_.excludeAll(exclusions: _*))
    def without(exclusions: ExclusionRule): Seq[ModuleID] = without(Seq(exclusions))

    lazy val withoutLog4j = (deps without log4jDeps) :+ fromLog4jToLogbackDep
  }

}
