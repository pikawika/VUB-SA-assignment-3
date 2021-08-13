package Lennert_Bontinck_SA3.Communication_Logic.Helper_Classes

import akka.actor.ActorRef

/** Case class that represent a created actor by saving its name and ActorRef. */
case class NamedActor(name: String, actorRef: ActorRef)
