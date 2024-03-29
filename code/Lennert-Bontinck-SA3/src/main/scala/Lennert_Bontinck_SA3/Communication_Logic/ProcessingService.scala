package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.Purchase
import Lennert_Bontinck_SA3.Communication_Logic.Ephemerals.ProcessingChildService
import Lennert_Bontinck_SA3.Communication_Logic.Messages.PrimeAlternatives.{FindNearestStockHousesPrime, PurchaseConfirmedPrime}
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{FindNearestStockHouses, PurchaseConfirmed}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import java.util.UUID

/** Actor for the ProcessingService. */
class ProcessingService(stockHouseManagerServiceActor: ActorRef) extends Actor with ActorLogging {

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case PurchaseConfirmed(purchase: Purchase, corrID: UUID, replyTo: ActorRef) =>
      // Use custom made private function of actor
      purchaseConfirmed(purchase, corrID, replyTo)
    case PurchaseConfirmedPrime(purchaseConfirmedMsg: PurchaseConfirmed) =>
      // Use custom made private function of actor
      purchaseConfirmed(purchaseConfirmedMsg.purchase,
        purchaseConfirmedMsg.corrID,
        purchaseConfirmedMsg.replyTo,
        isPrime = true)
  }

  /** Function to process a PurchaseConfirmed message. */
  private def purchaseConfirmed(purchase: Purchase, corrID: UUID, replyTo: ActorRef, isPrime: Boolean = false): Unit = {
    // Artificial wait to demonstrate Prime Priority by building up messages in mailbox
    Thread.sleep(500)
    // Create ephemeral child responsible for further handling this purchase
    val childActor = context.actorOf(Props(new ProcessingChildService(purchase, corrID, replyTo, isPrime)))
    // Request nearest stock houses and sent further responses to child
    val findNearestStockHousesMessage = FindNearestStockHouses(purchase.client.address, childActor, corrID)
    if (isPrime) {
      stockHouseManagerServiceActor ! FindNearestStockHousesPrime(findNearestStockHousesMessage)
      log.info("!!!PRIME PRIORITY!!! ProcessingService: received purchaseConfirmed, created ephemeral child and sent FindNearestStockHouses.")
    } else {
      stockHouseManagerServiceActor ! findNearestStockHousesMessage
      log.info("ProcessingService: received purchaseConfirmed, created ephemeral child and sent FindNearestStockHouses.")
    }

  }
}
