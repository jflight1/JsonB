name := "JsonB"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "junit" % "junit" % "4.10" % "test"
//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test



//resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
libraryDependencies +=  "com.typesafe.play" %% "play-json" % "2.6.9"
