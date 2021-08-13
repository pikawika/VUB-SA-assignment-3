package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.{ProductWithQuantity, Purchase}
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{InStock, PurchaseConfirmed}
import akka.actor.Actor

class ProcessingService() extends Actor {
  // TODO: A ProcessingService will process the products requested by the ClientService.

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case PurchaseConfirmed(purchase: Purchase) =>
    // Client has placed a purchase as sent by the ClientService
    // TODO: The ProcessingService will then collect all products selected and placed in the
    //  purchase. In order to do so, it will need to contact the StockHouseServices to check
    //  the products availability in the 5 nearest StockHouses.
    //  Once the nearest stock houses are identified, the ProcessingService will query each
    //  of them for the availability of the products via a message FillOrder.

    case InStock(productWithQuantity: ProductWithQuantity) =>
    // Stock house has this product available as confirmed by StockHouseService
    // TODO: the ProcessingService aggregates responses
    //  from the StockHouseService with the closest StockHouse,
    //  stops aggregating when sufficient responses have arrived to fill the order
    //  once all products are ready, an OrderShipped message needs to be sent back
    //  to the same Client requesting the products. However, if an order is not yet
    //  complete due to delays in the stock houses or lack
    //  of available products, an OrderDelayed message have to be sent instead.
  }
}
