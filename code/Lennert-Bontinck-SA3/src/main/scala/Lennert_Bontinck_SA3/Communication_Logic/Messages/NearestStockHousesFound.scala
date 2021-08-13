package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Communication_Logic.NamedActor

/** Case class that represents a message to give the found nearest stock houses. */
case class NearestStockHousesFound(stockHouseNamedActors: List[NamedActor])
