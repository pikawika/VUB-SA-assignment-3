package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Communication_Logic.Helper_Classes.NamedActor

import java.util.UUID

/** Case class that represents a message to give the found nearest stock houses.
 * CorrID used since working with ephemeral child actors. */
case class NearestStockHousesFound(stockHouseNamedActors: List[NamedActor],
                                   corrID: UUID)
