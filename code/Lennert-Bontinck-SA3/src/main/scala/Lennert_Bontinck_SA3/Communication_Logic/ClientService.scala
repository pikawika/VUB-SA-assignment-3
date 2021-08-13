package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.{Client, Purchase}
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{OrderDelayed, OrderShipped}
import akka.actor.Actor

class ClientService(val client: Client) extends Actor {

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case OrderShipped() =>
      //TODO
    case OrderDelayed() =>
    //TODO
  }

  def placePurchase(purchase: Purchase): Unit = {
    client.addPurchase(purchase)
    // TODO: The ClientService will send the purchases to the ProcessingService.
  }
}
