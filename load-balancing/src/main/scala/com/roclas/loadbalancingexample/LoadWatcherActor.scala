package com.roclas.loadbalancingexample

import java.lang.management.ManagementFactory
import javax.management.{Attribute, AttributeList, MBeanServer, ObjectName}
import scala.concurrent.duration._

import akka.actor.{Props, Actor, ActorLogging}
import scala.concurrent.ExecutionContext.Implicits.global

object LoadWatcherActor{
  def props(): Props = Props(new LoadWatcherActor())
}

class LoadWatcherActor extends Actor with ActorLogging {
  context.system.scheduler.schedule(1 second,2 seconds,self,"watchload")

  def watchLoad():Double={
    val mbs:MBeanServer= ManagementFactory.getPlatformMBeanServer()
    val name:ObjectName     = ObjectName.getInstance("java.lang:type=OperatingSystem")
    val list : AttributeList = mbs.getAttributes(name,Array[String]("ProcessCpuLoad") )
    
    if (list.isEmpty())return Double.NaN
    val value:Double   = list.get(0).asInstanceOf[Attribute].getValue().asInstanceOf[Double]
    if (value == -1.0) return Double.NaN

    ((value * 1000) / 10.0)
  }

  def receive = {
    //CLIENT MESSAGES
    case "watchload"=>
      self ! watchLoad()
    case msg => {
      log.info(s"load watcher actor receives message: $msg")
    }
  }
}
