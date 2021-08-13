package Lennert_Bontinck_SA3.Communication_Logic.Helper_Classes

import Lennert_Bontinck_SA3.Communication_Logic.Messages.PrimeAlternatives.{FillOrderPrime, FindNearestStockHousesPrime, PurchaseConfirmedPrime}
import akka.actor.ActorSystem
import akka.dispatch.PriorityGenerator
import akka.dispatch.UnboundedPriorityMailbox
import com.typesafe.config.Config

/** Custom mailbox to use so that prime messages are prioritized.
 * Inspiration from: https://doc.akka.io/docs/akka/current/mailboxes.html */
class PrimePriorityMailbox(settings: ActorSystem.Settings, config: Config) extends UnboundedPriorityMailbox(
  // Create a new PriorityGenerator, lower priority means more important
  PriorityGenerator {
    // Prime messages have highest priority (priority 0)
    case msg: FillOrderPrime => 0
    case msg: FindNearestStockHousesPrime => 0
    case msg: PurchaseConfirmedPrime => 0
    // All other messages are treated equally but slower (priority 1)
    case _ => 1
  })

