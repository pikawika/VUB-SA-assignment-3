package Lennert_Bontinck_SA3.Business_Logic

/** Case class that represents a warehouse having:
 * - address: Address */
case class StockHouse(address: Address) {

  //---------------------------------------------------------------------------
  //| START PRIVATE VARS
  //---------------------------------------------------------------------------
  // Keep products in stock as private var
  private var products: Set[ProductWithQuantity] = Set()

  //---------------------------------------------------------------------------
  //| END PRIVATE VARS
  //---------------------------------------------------------------------------
  //| START STOCK CHECKING FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to check if warehouse has a specific product and the desired quantity in stock. */
  def isProductWithQuantityInStock(requestedProduct: ProductWithQuantity): Boolean = {
    products.exists(inStock =>
      inStock.product.name == requestedProduct.product.name &&
        inStock.quantity - requestedProduct.quantity >= 0)
  }

  //---------------------------------------------------------------------------
  //| END STOCK CHECKING FUNCTIONS
  //---------------------------------------------------------------------------
  //| START STOCK MANAGEMENT FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to remove specific quantity of a product from the stock.
   * Returns boolean specifying if operation succeeded (might fail due to non available product/quantity). */
  def removeProductWithQuantityFromStock(requestedProduct: ProductWithQuantity): Boolean = {
    if (isProductWithQuantityInStock(requestedProduct)) {
      val productFromStock = products.filter(_.product.name == requestedProduct.product.name).head
      productFromStock.subtractQuantity(requestedProduct.quantity)
      // Operation is successful
      true
    } else {
      // Operation failed
      false
    }
  }

  /** Function to add specific quantity of a product to the stock. */
  def addProductWithQuantityToStock(newProduct: ProductWithQuantity): Unit = {
    val productExists = products.exists(_.product.name == newProduct.product.name)

    if (productExists) {
      // add stock quantity to existing stock
      val productFromStock = products.filter(_.product.name == newProduct.product.name).head
      productFromStock.addQuantity(newProduct.quantity)
    } else {
      // just add new product
      products = products + newProduct
    }
  }


  //---------------------------------------------------------------------------
  //| END STOCK MANAGEMENT FUNCTIONS
  //---------------------------------------------------------------------------

}
