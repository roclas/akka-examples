package com.roclas.loadbalancingexample

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.contrib.pattern.ClusterReceptionistExtension
import com.typesafe.config.ConfigFactory



object SimpleClusterApp extends App {

  // Override the configuration of the port
  // when specified as program argument
  if (args.nonEmpty){
    System.setProperty("clusterserver.akka.remote.netty.tcp.port", args(0))
    println(s"\n\n\n\n\nclusterserver.akka.remote.netty.tcp.port=${args(0)}\n\n\n\n\n")
  }else{
    val port=System.getProperty("clusterserver.akka.remote.netty.tcp.port")
    println(s"\n\n\n\n\nclusterserver.akka.remote.netty.tcp.port=${port}\n\n\n\n\n")
  }

  // Create an Akka system
  implicit val system = ActorSystem("ClusterSystem", ConfigFactory.load.getConfig("clusterserver"))

  val clusterListener = system.actorOf(Props( new SimpleClusterListener()), name = "clusterListener")
  Cluster(system).subscribe(clusterListener, classOf[ClusterDomainEvent])

  val service = system.actorOf(Props(new WorkingActor()), "workingService")
  ClusterReceptionistExtension(system).registerService(service)


}
