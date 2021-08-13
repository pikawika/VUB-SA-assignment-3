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
  //| START ORDER FULFILLMENT MESSAGES
  //---------------------------------------------------------------------------

  def provideAvailableProductsForPurchase(requestedProductsWithQuantity: Set[ProductWithQuantity]): Set[ProductWithQuantity] = {
    // Keep track of the products supplied for filling the order
    var collectedProducts: Set[ProductWithQuantity] = Set()

    for (requestedProduct <- requestedProductsWithQuantity) {
      val isAvailableInStock: Boolean = products.exists(pwc => pwc.product.name == requestedProduct.product.name
        && pwc.quantity > 0)

      if(isAvailableInStock) {
        val productFromStock: ProductWithQuantity = products.filter(_.product.name == requestedProduct.product.name).head
        val canFullFilFully: Boolean = productFromStock.quantity - requestedProduct.quantity >= 0

        if(canFullFilFully) {
          // Collect the products by removing them from the stock
          productFromStock.subtractQuantity(requestedProduct.quantity)
          // Add requested product to collected products since it is fulfilled completely
          collectedProducts = collectedProducts + requestedProduct
        } else {
          // Determine available amount
          val availableQuantityToDeliver: Int = productFromStock.quantity
          // Remove all available amount from stock, i.e. remove product from stock entirely
          products = products - productFromStock
          // Add requested product with lesser amount to collected products (partially fulfilled)
          collectedProducts = collectedProducts + ProductWithQuantity(productFromStock.product, availableQuantityToDeliver)
        }
      }
    }

    // Return the collected products from the stock
    collectedProducts
  }

}
