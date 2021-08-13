package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.Address

/** Case class that represents a request message to find the nearest stock houses from a given address. */
case class FindNearestStockHouses(address: Address, amountOfStockHouses: Int = 5)
