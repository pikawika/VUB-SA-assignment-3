package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.ProductWithQuantity

/** Case class that represents a request message to add a product with quantity to a stock house. */
case class AddProductToStockHouse(productWithQuantity: ProductWithQuantity)
