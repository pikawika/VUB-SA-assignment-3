package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.{ProductWithQuantity, StockHouse}
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{AddProductToStockHouse, FillOrder}
import akka.actor.{Actor, ActorLogging}

class StockHouseService(stockHouse: StockHouse) extends Actor with ActorLogging {
  // TODO: A StockHouseService will manage a StockHouse and represent its communication logic.
  //  The StockHouseService will take care of checking and sending product
  //  availability in the stock when requested from the ProcessingService.

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case FillOrder(product: List[ProductWithQuantity]) =>
    // TODO: check availability of each product
    //  if product is available with right quantity sent InStock message for that specific product

    case AddProductToStockHouse(productWithQuantity: ProductWithQuantity) =>
      stockHouse.addProductWithQuantityToStock(productWithQuantity)
      log.info("Added product: " + productWithQuantity.quantity + "x " + productWithQuantity.product.name)
  }
}
