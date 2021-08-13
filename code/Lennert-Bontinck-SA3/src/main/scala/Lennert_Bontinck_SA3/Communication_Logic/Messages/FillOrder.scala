package Lennert_Bontinck_SA3.Communication_Logic.Messages

import Lennert_Bontinck_SA3.Business_Logic.ProductWithQuantity

/** Case class that represents a request message asking a stock house if products requested are available in stock. */
case class FillOrder(productsWithQuantity: List[ProductWithQuantity])