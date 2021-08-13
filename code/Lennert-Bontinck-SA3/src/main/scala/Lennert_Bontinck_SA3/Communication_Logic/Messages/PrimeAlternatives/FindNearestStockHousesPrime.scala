package Lennert_Bontinck_SA3.Communication_Logic.Messages.PrimeAlternatives

import Lennert_Bontinck_SA3.Business_Logic.Address
import akka.actor.ActorRef

import java.util.UUID

/** Case class that represents a request message to find the nearest stock houses from a given address.
 * CorrID used since working with ephemeral child actors. */
case class FindNearestStockHousesPrime(address: Address, replyTo: ActorRef, corrID: UUID, amountOfStockHouses: Int = 5)
