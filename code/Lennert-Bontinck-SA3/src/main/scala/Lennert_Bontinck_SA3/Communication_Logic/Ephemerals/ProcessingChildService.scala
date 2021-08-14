package Lennert_Bontinck_SA3.Communication_Logic.Ephemerals

import Lennert_Bontinck_SA3.Business_Logic.{ProductWithQuantity, Purchase}
import Lennert_Bontinck_SA3.Communication_Logic.Helper_Classes.NamedActor
import Lennert_Bontinck_SA3.Communication_Logic.Messages.PrimeAlternatives.FillOrderPrime
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{FillOrder, InStock, NearestStockHousesFound, OrderDelayed, OrderShipped}
import akka.actor.{Actor, ActorLogging, ActorRef}

import java.util.UUID

/** Ephemeral child actor for the processing service, is responsible for managing a singular purchase. */
class ProcessingChildService(purchase: Purchase,
                             corrID: UUID,
                             requestingClientActor: ActorRef,
                             isPrime: Boolean = false) extends Actor with ActorLogging {

  /** Keep a list of stock house actors that are not yet contacted for fulfilling order.
   * NOTE: use of var in actor might trigger a warning in IntelliJ,
   * however, using a var is acceptable here as per: https://stackoverflow.com/a/18810678 */
  private var remainingStockHousesToGatherFrom: List[NamedActor] = List()

  //---------------------------------------------------------------------------
  //| START RECEIVE FUNCTION
  //---------------------------------------------------------------------------

  /** Receive function which processes incoming messages (Actor specific function).
   * This is the receive function for this actor's initial form. */
  def receive: Receive = {
    case NearestStockHousesFound(stockHouseNamedActors: List[NamedActor], `corrID`) =>
      // Use custom made private function inside actor
      nearestStockHousesFound(stockHouseNamedActors)
    case NearestStockHousesFound(_, wrongID: UUID) =>
      // Got wrong corrID, terminate per default behaviour seen in lecture
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
    // Become the aggregating actor to collect all items from warehouses (second and final form of this actor)
    context.become(receiveAggregating)
    // Sent first FillOrder request to first found warehouse (nearest)
    val fillOrderMessage = FillOrder(purchase.remainingProducts, corrID)
    if(isPrime) {
      remainingStockHousesToGatherFrom.head.actorRef ! FillOrderPrime(fillOrderMessage)
    } else {
      remainingStockHousesToGatherFrom.head.actorRef ! fillOrderMessage
    }
    log.info("ProcessingChildService " + corrID + ": Got nearest stock houses, became aggregating receiver and sent first FillOrder message.")
  }

  //---------------------------------------------------------------------------
  //| END PROCESS NearestStockHousesFound
  //---------------------------------------------------------------------------
  //| START AGGREGATING RECEIVE
  //---------------------------------------------------------------------------

  /** New receive method for processing aggregating actor stage (second, final stage) - "partial". */
  def receiveAggregatingProcessor: Receive = {
    case InStock(productsWithQuantity: Set[ProductWithQuantity], stockHouseName: String, `corrID`) =>
      // Use custom made private function inside actor
      inStock(productsWithQuantity, stockHouseName)

    case InStock(_, _, wrongID: UUID) =>
      // Got wrong corrID, terminate per default behaviour seen in lecture
      log.error("ProcessingChildService " + corrID + ": corrID mismatch, got: " + wrongID)
      context.stop(self)
  }

  /** New receive method for checking completion of aggregating actor stage - "partial". */
  def checkCompletedProcess: Receive = {
    case _ =>
      log.info("ProcessingChildService " + corrID + ": got more InStock messages, updated information.")
      if (purchase.isPurchaseFulfilled) {
        // order fulfilled
        requestingClientActor ! OrderShipped(corrID)
        log.info("ProcessingChildService " + corrID + ": got all items, sending OrderShipped and terminating myself.")
        context.stop(self)
      } else {
        if (remainingStockHousesToGatherFrom.isEmpty) {
          // No more stock houses to contact and order not fulfilled
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
    // Remove stock house from list since reply is received
    remainingStockHousesToGatherFrom = remainingStockHousesToGatherFrom.filter(_.name != stockHouseName)
    // Update list of needed items in domain logic
    purchase.updateRemainingProducts(productsWithQuantity)
  }
}
