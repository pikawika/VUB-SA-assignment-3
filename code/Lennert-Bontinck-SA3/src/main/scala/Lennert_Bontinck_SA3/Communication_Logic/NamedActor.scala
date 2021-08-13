package Lennert_Bontinck_SA3.Communication_Logic

import akka.actor.ActorRef

case class NamedActor(name: String, actorRef: ActorRef)
