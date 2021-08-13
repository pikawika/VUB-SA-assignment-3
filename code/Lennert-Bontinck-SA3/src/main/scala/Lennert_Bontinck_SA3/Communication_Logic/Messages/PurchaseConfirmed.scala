package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.Purchase
import akka.actor.ActorRef

import java.util.UUID

/** Case class that represents a message to let know that a purchase is made and confirmed. */
case class PurchaseConfirmed(purchase: Purchase, corrID: UUID, replyTo: ActorRef)
