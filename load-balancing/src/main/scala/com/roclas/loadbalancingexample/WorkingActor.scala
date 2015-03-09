package com.roclas.loadbalancingexample

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.{MemberStatus, Cluster}


object WorkingActor{
  def props(): Props = Props(new WorkingActor())
}

class WorkingActor extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("\n\n\n\n\nStarting Working Actor\n\n\n\n\n")
  }
  
  def doWork:Int={
    /* TODO */
    0
  }


  def reroute(value: Any): Unit = {
    val nodes=Cluster(context.system).state.members.collect{
        case m if m.status == MemberStatus.Up => m.address
    }
    println(s"nodes=$nodes")
  }

  def receive = {
    //CLIENT MESSAGES
    case msg => {
      log.info(s"working actor receives message: $msg")
      reroute(msg)
      
    }
  }
}
