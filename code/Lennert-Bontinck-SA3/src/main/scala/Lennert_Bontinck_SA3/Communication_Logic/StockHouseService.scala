package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.{ProductWithQuantity, StockHouse}
import Lennert_Bontinck_SA3.Communication_Logic.Messages.PrimeAlternatives.FillOrderPrime
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{AddProductToStockHouse, FillOrder, InStock}
import akka.actor.{Actor, ActorLogging, ActorRef}

import java.util.UUID

/** Actor for the managing communication of a stock house. */
class StockHouseService(stockHouse: StockHouse) extends Actor with ActorLogging {

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case FillOrder(requestedProductsWithQuantity: Set[ProductWithQuantity], corrID: UUID) =>
      fillOrder(requestedProductsWithQuantity, corrID, sender())

    case FillOrderPrime(requestedProductsWithQuantity: Set[ProductWithQuantity], corrID: UUID) =>
      fillOrder(requestedProductsWithQuantity, corrID, sender(), isPrime = true)

    case AddProductToStockHouse(productWithQuantity: ProductWithQuantity) =>
      stockHouse.addProductWithQuantityToStock(productWithQuantity)
      log.info(determineNameOfStockHouseActor() + ": Added " + productWithQuantity.quantity + "x " + productWithQuantity.product.name)
  }

  /** Determines the name of an stock house actor by assuming unique address. */
  private def determineNameOfStockHouseActor(): String = {
    "StockHouse-X_" + stockHouse.address.x.toString + "-Y_" + stockHouse.address.y.toString
  }

  /** Function to process a FillOrder message. */
  private def fillOrder(requestedProductsWithQuantity: Set[ProductWithQuantity],
                        corrID: UUID,
                        replyTo: ActorRef,
                        isPrime: Boolean = false): Unit = {
    // Artificial wait to demonstrate Prime Priority by building up messages in mailbox
    Thread.sleep(500)
    // Get products from stock in business logic
    val collectedProducts = stockHouse.provideAvailableProductsForPurchase(requestedProductsWithQuantity)
    // Reply collected products
    replyTo ! InStock(collectedProducts, determineNameOfStockHouseActor(), corrID)
    // Log progress
    if(isPrime) {
      log.info("!!!PRIME PRIORITY!!! " + determineNameOfStockHouseActor() + ": Checked available items in stock and provided them for client.")
    } else {
      log.info(determineNameOfStockHouseActor() + ": Checked available items in stock and provided them for client.")
    }
  }
}
