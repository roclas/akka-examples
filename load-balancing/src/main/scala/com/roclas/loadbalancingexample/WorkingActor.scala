package com.roclas.loadbalancingexample

import java.util.Random

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.{MemberStatus, Cluster}


object WorkingActor{
  def props(cluster:Cluster): Props = Props(new WorkingActor(cluster))
}

class WorkingActor(cluster:Cluster)extends Actor with ActorLogging {
  val random=new Random()

  override def preStart(): Unit = {
    log.info("\n\n\n\n\nStarting Working Actor\n\n\n\n\n")
  }
  
  def doWork:Int={
    /* TODO */
    0
  }


  def reroute(message: String): Unit = {
    val n=cluster.state.members.collect{
        case m if m.status == MemberStatus.Up => s"${m.address}/user/workingService"
    }.toSeq
    val receiver=n(random.nextInt(n.length))
    log.info(s"\n\nworking reroutes message: $message to $receiver\n\n")
    context.actorSelection(receiver) ! reroutedMessage(message)
  }

  def receive = {
    //CLIENT MESSAGES
    case reroutedMessage(m) => {
      log.info(s"\n\n\n\n\nconsumming message $m\n\n\n\n\n")
    }
    case msg:String => {
      log.info(s"working actor receives message: $msg from $sender")
      reroute(msg)
    }
  }
}
