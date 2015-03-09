package com.roclas.loadbalancingexample

import akka.actor._
import akka.cluster.Cluster
import akka.contrib.pattern.ClusterReceptionistExtension
import com.typesafe.config.ConfigFactory

object SimpleClusterApp extends App {

  // Override the configuration of the port
  // when specified as program argument
  if (args.nonEmpty)
    System.setProperty("clusterserver.akka.remote.netty.tcp.port", args(0))
  
  // Create an Akka system
  implicit val system = ActorSystem(
    "ClusterSystem",
    ConfigFactory.load.getConfig("clusterserver"))

  val cluster: Cluster =
    Cluster(system)

  val receptionistExtension: ClusterReceptionistExtension =
    ClusterReceptionistExtension(system)

  val clusterListener = system.actorOf(Props(
    new SimpleClusterListener(cluster)),
    "clusterListener")

  val service = system.actorOf(Props[LoadWatcherActor],"loadWatcherService")
  val service2 = system.actorOf(Props[WorkingActor],"workingService")

  receptionistExtension.registerService(service)
  receptionistExtension.registerService(service2)


}
