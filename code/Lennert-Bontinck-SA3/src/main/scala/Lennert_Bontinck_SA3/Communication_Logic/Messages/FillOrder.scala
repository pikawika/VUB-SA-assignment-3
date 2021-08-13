package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.ProductWithQuantity

import java.util.UUID

/** Case class that represents a request message asking a stock house to fulfill as many products of order as possible.
 * CorrID used since working with ephemeral child actors. */
case class FillOrder(productsWithQuantity: Set[ProductWithQuantity], corrID: UUID)
