package com.roclas.loadbalancingexample

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.contrib.pattern.ClusterReceptionistExtension
import com.typesafe.config.ConfigFactory

object SimpleClusterApp extends App {

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

  val service = system.actorOf(Props(
    new WorkingActor()),
    "workingService")

  receptionistExtension.registerService(service)


}
