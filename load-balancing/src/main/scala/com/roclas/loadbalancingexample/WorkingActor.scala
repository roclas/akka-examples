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


  def reroute(message: Any): Unit = {
    val nodes=cluster.state.members.collect{
        case m if m.status == MemberStatus.Up => m.address.toString
    }.map(context.actorSelection(_)).toSeq
    nodes(random.nextInt(nodes.length))!message
  }

  def receive = {
    //CLIENT MESSAGES
    case msg => {
      log.info(s"working actor receives message: $msg")
      reroute(msg)
    }
  }
}
