package com.roclas.loadbalancingexample


import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.MemberStatus.Up

class SimpleClusterListener(cluster: Cluster) extends Actor with ActorLogging {

  override def preStart(): Unit = {
    cluster.subscribe(
      self,
      initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent],
      classOf[UnreachableMember])
  }

  override def postStop(): Unit =
    cluster.unsubscribe(self)

  def receive = {
    //CLUSTER EVENTS
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
      log.info("Current members:{}", cluster.state.members.filter(_.status == Up))
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}", member.address, previousStatus)
      log.info("Current members:{}", cluster.state.members.filter(_.status == Up))
    case me: MemberEvent =>
      log.info(s"clusterlistener receives message: $me from:${sender()}")
    case other =>
      log.info(s"clusterlistener receives other $other")

  }
}
