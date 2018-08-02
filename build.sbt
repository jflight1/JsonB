//import sbt.impl.GroupArtifactID

name := "JsonB"

version := "1.0"

scalaVersion := "2.12.6"

//resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
//resolvers += "Typesafe Repo" at "http://dl.bintray.com/typesafe/maven-releases/"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test

//libraryDependencies +=  "com.typesafe.play" %% "play-json" % "2.4.0"
libraryDependencies +=  "com.typesafe.play" %% "play-json" % "2.6.9"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.1"
libraryDependencies += "commons-io" % "commons-io" % "2.5"


/*
val d: GroupArtifactID = "com.typesafe.play" %% "play-json_2.11"
val d1: ModuleID = d % "2.3.8"

libraryDependencies +=  d1
*/

