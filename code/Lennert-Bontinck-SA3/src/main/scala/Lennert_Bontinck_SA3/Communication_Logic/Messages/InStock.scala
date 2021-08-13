package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.ProductWithQuantity

import java.util.UUID

/** Case class that represents a message letting know that an item was taken from stock and used to fulfill order.
 * CorrID used since working with ephemeral child actors. */
case class InStock(productsWithQuantity: Set[ProductWithQuantity], stockHouseName: String, corrID: UUID)
