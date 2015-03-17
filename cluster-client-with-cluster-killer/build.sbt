name := "Cluster with a cluster killer that reads from command line and send messages to actor selections"

version := "0.1"

scalaVersion := "2.10.4"


libraryDependencies ++= Seq( 
  "com.typesafe.akka" %% "akka-cluster" % "2.3.4"
  ,"com.typesafe.akka" %% "akka-remote" % "2.3.4"
  ,"com.typesafe.akka" % "akka-contrib_2.10" % "2.3.4"
)
