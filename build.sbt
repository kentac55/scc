import Dependencies._

name := "scc"

version := "0.1"

scalaVersion := "2.13.0"
libraryDependencies ++= Seq(parserCombinators, enumeratum)
val unusedWarnings = "-Ywarn-unused" ::
  Nil

scalacOptions ++= (
  "-deprecation" ::
    "-unchecked" ::
    "-Xlint" ::
    "-language:existentials" ::
    "-language:higherKinds" ::
    "-language:implicitConversions" ::
    Nil
) ::: unusedWarnings

Seq(Compile, Test).flatMap(
  c => scalacOptions in (c, console) --= unusedWarnings
)
