package com.roclas.loadbalancingexample

import akka.actor._
import akka.contrib.pattern.ClusterClient
import com.typesafe.config.ConfigFactory

object SimpleClientApp extends App {

  // Override the configuration of the port
  // when specified as program argument
  if (args.nonEmpty)
    System.setProperty("clusterclient.akka.remote.netty.tcp.port", args(0))

  // Create an Akka system
  implicit val system = ActorSystem(
    "ClientSystem",
    ConfigFactory.load.getConfig("clusterclient"))

  val contacts = List(
  "akka.tcp://ClusterSystem@127.0.0.1:2552/user/receptionist",
  "akka.tcp://ClusterSystem@127.0.0.1:2551/user/receptionist")

  val initialContacts = contacts.map(system.actorSelection).toSet

  println("Starting Cluster Client")

  val client = system.actorOf(ClusterClient.props(initialContacts))

  val stream: Stream[Int] = 0 #:: stream.map(_+1)
  stream.foreach(i => {
    println(s"Sending message $i")
    client ! ClusterClient.Send(
      "/user/loadWatcherService",
      s"This is request $i",
      localAffinity = true)
    Thread.sleep(1000)
  })


}
