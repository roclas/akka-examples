package com.roclas.loadbalancingexample

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster


class WorkingActor extends Actor with ActorLogging {

  val cluster = Cluster(context.system)
 
  // subscribe to cluster changes, re-subscribe when restart 
  override def preStart(): Unit = {
    log.info("\n\n\n\n\nStarting Working Actor\n\n\n\n\n")
  }
  override def postStop(): Unit = cluster.unsubscribe(self)
  

  def receive = {
    //CLIENT MESSAGES
    case s: String => {
      log.info(s"working actor receives message: ${s}")
      println(s"working actor receives message: ${s}")
    }
  }
}
