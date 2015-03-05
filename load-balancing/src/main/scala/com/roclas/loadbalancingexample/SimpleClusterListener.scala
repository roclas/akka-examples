package com.roclas.loadbalancingexample


import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.MemberStatus.Up


class SimpleClusterListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)
 
  // subscribe to cluster changes, re-subscribe when restart 
  override def preStart(): Unit = {
    //#subscribe
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
    //#subscribe
  }
  override def postStop(): Unit = cluster.unsubscribe(self)
  
 
  def receive = {
    //CLUSTER EVENTS
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
      log.info("Current members:{}",cluster.state.members.filter(_.status == Up))
      for (m<-cluster.state.members.filter(_.status == Up)){
        log.info(s"sending message to ${m.address}/user/clusterListener")
        context.actorSelection(s"${m.address}/user/clusterListener") ! "hello world"
      }

    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}", member.address, previousStatus)
      log.info("Current members:{}",cluster.state.members.filter(_.status == Up))
    case me: MemberEvent => {
      log.info(s"clusterlistener receives message: ${me} from:${sender}")
    }
    case s: String => log.info(s"clusterlistener receives string: ${s} from:${sender}")
    case _=> {
      log.info(s"clusterlistener receives _")
    }

    
    //CLIENT MESSAGES
  }
}
