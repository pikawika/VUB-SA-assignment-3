package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.Purchase
import Lennert_Bontinck_SA3.Communication_Logic.Ephemerals.ProcessingChildService
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{FindNearestStockHouses, PurchaseConfirmed}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import java.util.UUID

class ProcessingService(stockHouseManagerServiceActor: ActorRef) extends Actor with ActorLogging {
  // TODO: A ProcessingService will process the products requested by the ClientService.

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case PurchaseConfirmed(purchase: Purchase, corrID: UUID, replyTo: ActorRef) =>
      // Use custom made private function of actor
      purchaseConfirmed(purchase, corrID, replyTo)
  }

  // Done (?)
  private def purchaseConfirmed(purchase: Purchase, corrID: UUID, replyTo: ActorRef): Unit = {
    // TODO: The ProcessingService will then collect all products selected and placed in the
    //  purchase. In order to do so, it will need to contact the StockHouseServices to check
    //  the products availability in the 5 nearest StockHouses.
    //  Once the nearest stock houses are identified, the ProcessingService will query each
    //  of them for the availability of the products via a message FillOrder.

    // Create ephemeral child responsible for further handling this purchase
    val childActor = context.actorOf(Props(new ProcessingChildService(purchase, corrID, replyTo)))
    // Request nearest stock houses and further responses work to child
    stockHouseManagerServiceActor ! FindNearestStockHouses(purchase.client.address, childActor, corrID)
    log.info("ProcessingService: received purchaseConfirmed, created ephemeral child and sent FindNearestStockHouses.")
  }
}
