package Lennert_Bontinck_SA3

import Lennert_Bontinck_SA3.Business_Logic.{Address, Client, Product, ProductWithQuantity, Purchase, StockHouse, StockHouseManager}
import Lennert_Bontinck_SA3.Communication_Logic.Messages.{AddProductToStockHouseDummy, AddStockHouse, PurchasePlaced}
import Lennert_Bontinck_SA3.Communication_Logic.{ClientService, ProcessingService, StockHouseManagerService}
import akka.actor.{ActorRef, ActorSystem, Props}

object Main extends App {

  // Todo: this should run example code. Make sure that your solution supports at least 5 products being requested concurrently,
  //  and write some code demonstrating this capability of your solution. Additionally, your
  //  Client class should include an attribute indicating whether a client has subscribed to
  //  Prime services. Your implementation should prioritise purchases from clients that have
  //  subscribed to Prime. Purchases from other clients should be processed normally

  //---------------------------------------------------------------------------
  //| START SETTING UP ENVIRONMENT
  //---------------------------------------------------------------------------
  val debugReadingTime: Int = 100

  // Make an actor system
  val actorSystem: ActorSystem = ActorSystem("LennertBontinckSystem")

  // Create some of the actors
  val stockHouseManager: StockHouseManager = StockHouseManager()
  val stockHouseManagerActor: ActorRef = actorSystem.actorOf(Props(new StockHouseManagerService(stockHouseManager)), name = "StockHouseManagerService")
  val processingService: ActorRef = actorSystem.actorOf(Props(new ProcessingService(stockHouseManagerActor)), name = "processingService")

  // Make some addresses
  val address1: Address = Address(1.1, 1.1)
  val address2: Address = Address(2.2, 2.2)
  val address3: Address = Address(3.3, 3.3)
  val address4: Address = Address(4.4, 4.4)
  val address5: Address = Address(5.5, 5.5)
  val address6: Address = Address(6.6, 6.6)
  val address7: Address = Address(7.7, 7.7)
  val address8: Address = Address(8.8, 8.8)

  // Make some clients
  val client1: Client = Client("Lennert", address1, primeMember = false)
  val client2: Client = Client("Bontinck", address2, primeMember = false)
  val client3: Client = Client("Joske", address3, primeMember = false)
  val client4: Client = Client("Willfried", address4, primeMember = false)
  val client5: Client = Client("Coen", address5, primeMember = false)
  val client6: Client = Client("Johan", address6, primeMember = false)
  val client7: Client = Client("Kris", address7, primeMember = false)
  val client8: Client = Client("Peter", address8, primeMember = false)

  // Create some client actors
  val clientActor1: ActorRef = actorSystem.actorOf(Props(new ClientService(client1, processingService)), name = client1.name)
  val clientActor2: ActorRef = actorSystem.actorOf(Props(new ClientService(client2, processingService)), name = client2.name)
  val clientActor3: ActorRef = actorSystem.actorOf(Props(new ClientService(client3, processingService)), name = client3.name)
  val clientActor4: ActorRef = actorSystem.actorOf(Props(new ClientService(client4, processingService)), name = client4.name)
  val clientActor5: ActorRef = actorSystem.actorOf(Props(new ClientService(client5, processingService)), name = client5.name)
  val clientActor6: ActorRef = actorSystem.actorOf(Props(new ClientService(client6, processingService)), name = client6.name)
  val clientActor7: ActorRef = actorSystem.actorOf(Props(new ClientService(client7, processingService)), name = client7.name)
  val clientActor8: ActorRef = actorSystem.actorOf(Props(new ClientService(client8, processingService)), name = client8.name)

  // Create some stock houses
  val stockHouse1: StockHouse = StockHouse(address1)
  val stockHouse2: StockHouse = StockHouse(address2)
  val stockHouse3: StockHouse = StockHouse(address3)
  val stockHouse4: StockHouse = StockHouse(address4)
  val stockHouse5: StockHouse = StockHouse(address5)
  val stockHouse6: StockHouse = StockHouse(address6)
  val stockHouse7: StockHouse = StockHouse(address7)
  val stockHouse8: StockHouse = StockHouse(address8)

  // Add stock houses to stock house manager by sending him messages
  stockHouseManagerActor ! AddStockHouse(stockHouse1)
  stockHouseManagerActor ! AddStockHouse(stockHouse2)
  stockHouseManagerActor ! AddStockHouse(stockHouse3)
  stockHouseManagerActor ! AddStockHouse(stockHouse4)
  stockHouseManagerActor ! AddStockHouse(stockHouse5)
  stockHouseManagerActor ! AddStockHouse(stockHouse6)
  stockHouseManagerActor ! AddStockHouse(stockHouse7)
  stockHouseManagerActor ! AddStockHouse(stockHouse8)
  stockHouseManagerActor ! AddStockHouse(stockHouse8) // sent a duplicate to test actor behaviour

  // Artificial pause for debug reading
  Thread.sleep(debugReadingTime)

  // Create some Products to add
  val graphicsCardProduct: Product = Product("NVIDIA GTX 3080")
  val playstationProduct: Product = Product("Sony PS5")
  val macbookProduct: Product = Product("Apple MacBook Pro 15")
  val imacProduct: Product = Product("Apple iMac")
  val iphoneProduct: Product = Product("Apple iPhone")
  val samsungPhoneProduct: Product = Product("Samsung Galaxy S21 Ultra")
  val hpLaptop: Product = Product("HP Omen 15")

  // Add products to stock houses
  // NOTE: uses "dummy" function to reach stock house from stockHouseManagerActor -> only done for purpose of easier init.
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse1, ProductWithQuantity(graphicsCardProduct, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse1, ProductWithQuantity(playstationProduct, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse1, ProductWithQuantity(macbookProduct, 5))

  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse2, ProductWithQuantity(imacProduct, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse2, ProductWithQuantity(iphoneProduct, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse2, ProductWithQuantity(samsungPhoneProduct, 5))

  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse3, ProductWithQuantity(hpLaptop, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse3, ProductWithQuantity(graphicsCardProduct, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse3, ProductWithQuantity(playstationProduct, 5))

  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse4, ProductWithQuantity(macbookProduct, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse4, ProductWithQuantity(imacProduct, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse4, ProductWithQuantity(iphoneProduct, 5))

  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse5, ProductWithQuantity(samsungPhoneProduct, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse5, ProductWithQuantity(hpLaptop, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse5, ProductWithQuantity(graphicsCardProduct, 5))

  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse6, ProductWithQuantity(playstationProduct, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse6, ProductWithQuantity(macbookProduct, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse6, ProductWithQuantity(imacProduct, 5))

  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse7, ProductWithQuantity(iphoneProduct, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse7, ProductWithQuantity(samsungPhoneProduct, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse7, ProductWithQuantity(hpLaptop, 5))

  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse8, ProductWithQuantity(graphicsCardProduct, 3))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse8, ProductWithQuantity(playstationProduct, 4))
  stockHouseManagerActor ! AddProductToStockHouseDummy(stockHouse8, ProductWithQuantity(macbookProduct, 5))

  // Artificial pause for debug reading
  Thread.sleep(debugReadingTime)

  //---------------------------------------------------------------------------
  //| END SETTING UP ENVIRONMENT
  //---------------------------------------------------------------------------
  //| START PLACING PURCHASES
  //---------------------------------------------------------------------------

  // Make some purchases
  val productList1: List[ProductWithQuantity] = List(
    ProductWithQuantity(macbookProduct, 2),
    ProductWithQuantity(playstationProduct, 3))

  clientActor1 ! PurchasePlaced(Purchase(productList1, client1))
}
