package Lennert_Bontinck_SA3.Business_Logic

/** Case class that represents a purchase having:
 * - products: list of ProductWithQuantity objects: List[ProductWithQuantity] */
case class Purchase(products: List[ProductWithQuantity], client: Client) {

  //---------------------------------------------------------------------------
  //| START PRIVATE VARS
  //---------------------------------------------------------------------------

  /** Keep a list of items that are not yet fulfilled for the purchase */
  var remainingProducts = products.toSet

  //---------------------------------------------------------------------------
  //| END PRIVATE VARS
  //---------------------------------------------------------------------------
  //| START PURCHASED ITEMS MANAGEMENT
  //---------------------------------------------------------------------------

  /** Updates remaining products to fulfill order with a provided set of products and quantities. */
  def updateRemainingProducts(suppliedProductsWithQuantity: Set[ProductWithQuantity]): Unit = {
    for (remainingProduct <- remainingProducts) {
      val itemIsProvided: Boolean = suppliedProductsWithQuantity.exists(_.product.name == remainingProduct.product.name)

      if(itemIsProvided) {
        // remove item from remaining products
        remainingProducts = remainingProducts - remainingProduct

        // determine if it was only partial fulfillment because then it needs re-adding with new quantity
        val providedProduct = suppliedProductsWithQuantity.filter(_.product.name == remainingProduct.product.name).head
        val remainingQuantity = remainingProduct.quantity - providedProduct.quantity
        if(remainingQuantity > 0) {
          remainingProducts = remainingProducts + ProductWithQuantity(remainingProduct.product, remainingQuantity)
        }
      }
    }
  }

  def isPurchaseFulfilled: Boolean = {
    remainingProducts.isEmpty
  }

}
