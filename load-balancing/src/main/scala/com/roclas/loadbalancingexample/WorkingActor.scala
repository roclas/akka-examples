package com.roclas.loadbalancingexample

import akka.actor.{Props, Actor, ActorLogging}


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

  def receive = {
    //CLIENT MESSAGES
    case msg => {
      log.info(s"working actor receives message: $msg")
    }
  }
}
