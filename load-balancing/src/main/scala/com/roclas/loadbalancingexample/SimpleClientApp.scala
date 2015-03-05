package com.roclas.loadbalancingexample

import akka.actor._
import akka.contrib.pattern.ClusterClient
import com.typesafe.config.ConfigFactory


object SimpleClientApp extends App {

  // Override the configuration of the port
  // when specified as program argument
  //if (args.nonEmpty) System.setProperty("akka.remote.netty.tcp.port", args(0))

  // Create an Akka system
  implicit val system = ActorSystem("ClientSystem", ConfigFactory.load.getConfig("clusterclient"))
  //implicit val system = ActorSystem("ClusterSystem", ConfigFactory.load.getConfig("clusterclient"))

  val initialContacts = Set(
    //system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/receptionist"),
    system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/receptionist"))

  println("Starting Cluster Client")
  val c = system.actorOf(ClusterClient.props(initialContacts))
  val receptionistActor0=system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/")
  val receptionistActor=system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/receptionist")
  val serviceActor=system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/service")

  while(true){
      Thread.sleep(5000)
      serviceActor ! "hola serviceActor"
      receptionistActor0 ! "hola receptionistActor0"
      receptionistActor ! "hola receptionistActor"
      println("Sending message from Cluster Client")
      c ! ClusterClient.Send("/user/receptionist", "hello", localAffinity = true)
      Thread.sleep(2000)
      println("Sending message from Cluster Client to /user/service")
      c ! ClusterClient.Send("/user/service", "hello", localAffinity = true)
  }

}
