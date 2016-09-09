import sbt.impl.GroupArtifactID

name := "JsonB"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "junit" % "junit" % "4.10" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4"



resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
libraryDependencies +=  "com.typesafe.play" %% "play-json_2.11" % "2.3.8"




/*
val d: GroupArtifactID = "com.typesafe.play" %% "play-json_2.11"
val d1: ModuleID = d % "2.3.8"

libraryDependencies +=  d1
*/

