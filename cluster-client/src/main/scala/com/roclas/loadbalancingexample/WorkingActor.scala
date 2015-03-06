package com.roclas.loadbalancingexample

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster


class WorkingActor extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("\n\n\n\n\nStarting Working Actor\n\n\n\n\n")
  }

  def receive = {
    //CLIENT MESSAGES
    case msg => {
      log.info(s"working actor receives message: $msg")
    }
  }
}
