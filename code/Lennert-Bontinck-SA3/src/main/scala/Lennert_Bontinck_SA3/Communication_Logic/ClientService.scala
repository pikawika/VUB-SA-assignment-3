package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.{Client, Purchase}
import Lennert_Bontinck_SA3.Communication_Logic.Ephemerals.ClientChildService
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{PurchaseConfirmed, PurchasePlaced}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import java.util.UUID

/** Actor for the managing communication of a client. */
class ClientService(val client: Client, processingServiceActor: ActorRef) extends Actor with ActorLogging{

  //---------------------------------------------------------------------------
  //| START RECEIVE FUNCTION
  //---------------------------------------------------------------------------

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case PurchasePlaced(purchase: Purchase) =>
      // Use custom made private function of actor
      purchasePlaced(purchase)
  }

  //---------------------------------------------------------------------------
  //| END RECEIVE FUNCTION
  //---------------------------------------------------------------------------
  //| START PURCHASE PLACING FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to process a PurchasePlaced message. */
  private def purchasePlaced(purchase: Purchase): Unit = {
    // Add to business logic
    client.addPurchase(purchase)
    // Create ephemeral child responsible for further handling this purchase
    val corrID = UUID.randomUUID()
    val childActor = context.actorOf(Props(new ClientChildService(corrID)))
    // Sent PurchaseConfirmed message to processing service actor with ephemeral child as reply
    processingServiceActor ! PurchaseConfirmed(purchase, corrID, childActor)
    log.info("ClientService: received PurchasePlaced, registered purchase, created ephemeral child and sent PurchaseConfirmed.")
  }

}
