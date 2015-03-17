package com.roclas.loadbalancingexample

import java.io.{BufferedReader, InputStreamReader}

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

  val client = system.actorOf(ClusterClient.props(initialContacts))

  val stream: Stream[Int] = 0 #:: stream.map(_+1)
  stream.foreach(i => {
    //  prompt the user to enter their name
    Thread.sleep(100)
    System.out.print("\n\n\nEnter the actor selection you want to send the message to: ");
    //  open up standard input
    val br = new BufferedReader(new InputStreamReader(System.in))
    val actorPatternString= br.readLine();
    Thread.sleep(100)
    System.out.print(s"\n\n\nEnter the message you want to send to $actorPatternString: ");
    var command= br.readLine();
    command match{
      case "kill"=>system.actorSelection(actorPatternString)!Kill
              client ! ClusterClient.SendToAll( actorPatternString, Kill )
      case _=>system.actorSelection(actorPatternString)!command
              client ! ClusterClient.SendToAll( actorPatternString, command)
    }
  })


}
