package Lennert_Bontinck_SA3.Communication_Logic

import Lennert_Bontinck_SA3.Business_Logic.{Address, ProductWithQuantity, StockHouse, StockHouseManager}
import Lennert_Bontinck_SA3.Communication_Logic.Helper_Classes.NamedActor
import Lennert_Bontinck_SA3.Communication_Logic.Messages.PrimeAlternatives.FindNearestStockHousesPrime
import Lennert_Bontinck_SA3.Communication_Logic.Messages._
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import java.util.UUID

/** Actor for the managing communication of the stock house manager. */
class StockHouseManagerService(stockHouseManager: StockHouseManager) extends Actor with ActorLogging {

  /** Keep a list of all stock house actors so right one can be forwarded.
   * NOTE: use of var in actor might trigger a warning in IntelliJ,
   * however, using a var is acceptable here as per: https://stackoverflow.com/a/18810678 */
  private var stockHouseServices: Set[NamedActor] = Set()


  //---------------------------------------------------------------------------
  //| START RECEIVE FUNCTION
  //---------------------------------------------------------------------------

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case FindNearestStockHouses(address: Address, replyTo: ActorRef, corrID: UUID, amountOfStockHouses: Int) =>
      // use custom made private function inside actor
      findNearestStockHouses(address, replyTo, corrID, amountOfStockHouses)

    case FindNearestStockHousesPrime(findNearestStockHousesMsg: FindNearestStockHouses) =>
      // use custom made private function inside actor
      findNearestStockHouses(findNearestStockHousesMsg.address,
        findNearestStockHousesMsg.replyTo,
        findNearestStockHousesMsg.corrID,
        findNearestStockHousesMsg.amountOfStockHouses,
        isPrime = true)

    case AddStockHouse(newStockHouse: StockHouse) =>
      // Use custom made private function inside actor
      addStockHouse(newStockHouse)

    case AddProductToStockHouseDummy(stockHouse: StockHouse, productWithQuantity: ProductWithQuantity) =>
      // "Dummy" function to be able to add products from main application without being an actor, should not be needed "IRL"
      getNamedActorForStockHouse(stockHouse).actorRef ! AddProductToStockHouse(productWithQuantity)
  }
  //---------------------------------------------------------------------------
  //| END RECEIVE FUNCTION
  //---------------------------------------------------------------------------
  //| START ACTOR FINDER FUNCTIONS
  //---------------------------------------------------------------------------

  /** Determines the name of a stock house actor by assuming unique address. */
  private def determineNameOfStockHouseActor(stockHouse: StockHouse): String = {
    "StockHouse-X_" + stockHouse.address.x.toString + "-Y_" + stockHouse.address.y.toString
  }

  /** Processes a FindNearestStockHouses message */
  private def findNearestStockHouses(address: Address,
                                     replyTo: ActorRef,
                                     corrID: UUID,
                                     amountOfStockHouses: Int,
                                     isPrime: Boolean = false): Unit = {
    // Artificial wait to demonstrate Prime Priority by building up messages in mailbox
    Thread.sleep(500)
    // Use business logic to find nearest stock houses
    val foundStockHouses: List[StockHouse] = stockHouseManager.findNearestStockHouses(address, amountOfStockHouses)

    // Make set of named actors from stock houses found via business logic
    var foundNamedActors: Set[NamedActor] = Set()
    for (foundStockHouse <- foundStockHouses) {
      val foundNamedActor = getNamedActorForStockHouse(foundStockHouse)
      foundNamedActors = foundNamedActors + foundNamedActor
    }

    // Reply found nearest stock houses
    replyTo ! NearestStockHousesFound(foundNamedActors.toList, corrID)

    if (isPrime) {
      log.info("!!!PRIME PRIORITY!!! StockHouseManagerService: processed FindNearestStockHouses message.")
    } else {
      log.info("StockHouseManagerService: processed FindNearestStockHouses message.")
    }
  }

  /** Gets named actor object for provided stock house.
   * NOTE: assumes stock house exists! */
  private def getNamedActorForStockHouse(stockHouse: StockHouse): NamedActor = {
    val nameOfStockHouse = determineNameOfStockHouseActor(stockHouse)

    // Get stock house from list of services
    stockHouseServices.filter(_.name == nameOfStockHouse).head
  }

  //---------------------------------------------------------------------------
  //| END ACTOR FINDER FUNCTIONS
  //---------------------------------------------------------------------------
  //| START STOCK HOUSE ADDER FUNCTIONS
  //---------------------------------------------------------------------------

  /** Adds stock house to business logic and communication logic if not already present. */
  private def addStockHouse(newStockHouse: StockHouse): Unit = {
    // Add stock house to business logic, will return whether or not stock house is added
    val stockHouseAdded: Boolean = stockHouseManager.addStockHouse(newStockHouse)

    // Determine name of new actor
    val nameOfNewActor: String = determineNameOfStockHouseActor(newStockHouse)

    // Only add to actor list if stock house indeed added
    if (stockHouseAdded) {
      // Keep actor refs for messaging
      val newActorRef: ActorRef = context.actorOf(
        Props(new StockHouseService(newStockHouse)), name = nameOfNewActor)
      stockHouseServices = stockHouseServices + Helper_Classes.NamedActor(nameOfNewActor, newActorRef)
      log.info(nameOfNewActor + " added!")
    } else {
      log.info(nameOfNewActor + " not added, the stock house most likely already existed")
    }
  }
}


