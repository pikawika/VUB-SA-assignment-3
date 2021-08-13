package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.ProductWithQuantity

/** Case class that represents a message to let client know what item is in stock. */
case class InStock(product: ProductWithQuantity)