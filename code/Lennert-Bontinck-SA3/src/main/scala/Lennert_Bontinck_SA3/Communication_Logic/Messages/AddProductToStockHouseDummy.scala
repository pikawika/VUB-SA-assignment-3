package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.{ProductWithQuantity, StockHouse}

/** Case class that represents a request message to add a product with quantity to a stock house.
 * NOTE: this is a "dummy" function only to be used for easier initialisation of stock houses. */
case class AddProductToStockHouseDummy(stockHouse: StockHouse, productWithQuantity: ProductWithQuantity)
