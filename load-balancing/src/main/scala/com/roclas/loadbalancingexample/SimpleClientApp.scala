package com.roclas.loadbalancingexample

import akka.actor._
import akka.contrib.pattern.ClusterClient
import com.typesafe.config.ConfigFactory

object SimpleClientApp extends App {
  val remotePort ="2551"
  var contacts= Set("172.17.0.2", "172.17.0.3").
    map(ip=>s"akka.tcp://ClusterSystem@$ip:$remotePort/user/receptionist") //may be overriden by input
  
  // Create an Akka system
  implicit val system = ActorSystem( "ClientSystem", ConfigFactory.load.getConfig("clusterclient"))

  // Override the configuration of the port
  // and serverIps
  // when specified as program argument
  if (args.nonEmpty){
    contacts=args.filter(_.matches(".*:.*")).map{//if it is an ip:port, add it to the list of contacts
      ipAndPort=>s"akka.tcp://ClusterSystem@$ipAndPort/user/receptionist"
    }.toSet
    args.filter(_.matches("\\d*")).map{ //if it is a port, override it
      System.setProperty("clusterclient.akka.remote.netty.tcp.port", _)
    }
  }
  
  println(s"contacts=$contacts")
  val client = system.actorOf(ClusterClient.props( contacts.map(system.actorSelection) ))

  val stream: Stream[Int] = 0 #:: stream.map(n=>(n+1)%10)
  stream.foreach(i => {
    //val dest="/user/loadWatcherService"
    val dest="/user/workingService"
    println(s"Sending message $i to $dest")
    client ! ClusterClient.Send(
      dest,
      s"This is request $i",
      localAffinity = true)
    Thread.sleep(1000)
  })


}
