package Lennert_Bontinck_SA3.Communication_Logic.Ephemerals

import Lennert_Bontinck_SA3.Business_Logic.{ProductWithQuantity, Purchase}
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{FillOrder, InStock, NearestStockHousesFound, OrderDelayed, OrderShipped}
import Lennert_Bontinck_SA3.Communication_Logic.NamedActor
import akka.actor.{Actor, ActorLogging, ActorRef}

import java.util.UUID

class ProcessingChildService(purchase: Purchase,
                             corrID: UUID,
                             requestingClientActor: ActorRef) extends Actor with ActorLogging {

  private var remainingStockHousesToGatherFrom: List[NamedActor] = List()

  //---------------------------------------------------------------------------
  //| START RECEIVE FUNCTION
  //---------------------------------------------------------------------------

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case NearestStockHousesFound(stockHouseNamedActors: List[NamedActor], `corrID`) =>
      nearestStockHousesFound(stockHouseNamedActors)
    case NearestStockHousesFound(_, wrongID: UUID) =>
      log.error("ProcessingChildService " + corrID + ": corrID mismatch, got: " + wrongID)
      context.stop(self)
  }

  //---------------------------------------------------------------------------
  //| END RECEIVE FUNCTION
  //---------------------------------------------------------------------------
  //| START PROCESS NearestStockHousesFound
  //---------------------------------------------------------------------------

  /** Function to process NearestStockHousesFound message. */
  private def nearestStockHousesFound(stockHouseNamedActors: List[NamedActor]): Unit = {
    // Save stock houses to contact as a set
    remainingStockHousesToGatherFrom = stockHouseNamedActors
    // Become the aggregating actor to collect all items from warehouses
    context.become(receiveAggregating)
    // Sent first FillOrder request to first found warehouse (nearest)
    remainingStockHousesToGatherFrom.head.actorRef ! FillOrder(purchase.remainingProducts, corrID)
    log.info("ProcessingChildService " + corrID + ": Got nearest stock houses, became aggregating receiver and sent first FillOrder message.")
  }

  //---------------------------------------------------------------------------
  //| END PROCESS NearestStockHousesFound
  //---------------------------------------------------------------------------
  //| START AGGREGATING RECEIVE
  //---------------------------------------------------------------------------

  /** New receive method for processing aggregating actor stage. */
  def receiveAggregatingProcessor: Receive = {
    case InStock(productsWithQuantity: Set[ProductWithQuantity], stockHouseName: String, `corrID`) =>
      inStock(productsWithQuantity, stockHouseName)

    case InStock(_, _, wrongID: UUID) =>
      log.error("ProcessingChildService " + corrID + ": corrID mismatch, got: " + wrongID)
      context.stop(self)
  }

  /** New receive method for checking completion of aggregating actor stage. */
  def checkCompletedProcess: Receive = {
    case msg =>
      log.info("ProcessingChildService " + corrID + ": got more InStock messages, updated information.")
      if (purchase.isPurchaseFulfilled) {
        requestingClientActor ! OrderShipped(corrID)
        log.info("ProcessingChildService " + corrID + ": got all items, sending OrderShipped and terminating myself.")
        context.stop(self)
      } else {
        if (remainingStockHousesToGatherFrom.isEmpty) {
          requestingClientActor ! OrderDelayed(corrID)
          log.info("ProcessingChildService " + corrID + ": couldn't collect all items, sending OrderDelayed and terminating myself.")
          context.stop(self)
        } else {
          // Sent new FillOrder request to next stock house in line
          remainingStockHousesToGatherFrom.head.actorRef ! FillOrder(purchase.remainingProducts, corrID)
        }
      }
  }

  /** New receive method for aggregating actor stage is combination of processing and checking completion. */
  def receiveAggregating: Receive = receiveAggregatingProcessor andThen checkCompletedProcess

  //---------------------------------------------------------------------------
  //| END AGGREGATING RECEIVE
  //---------------------------------------------------------------------------
  //| START IN STOCK PROCESSING
  //---------------------------------------------------------------------------

  /** Function to process InStock reply of warehouse that products are given for this purchase. */
  private def inStock(productsWithQuantity: Set[ProductWithQuantity], stockHouseName: String): Unit = {
    // remove stock house from list since reply is received
    remainingStockHousesToGatherFrom = remainingStockHousesToGatherFrom.filter(_.name != stockHouseName)
    // update list of needed items in domain logic
    purchase.updateRemainingProducts(productsWithQuantity)
  }
}
