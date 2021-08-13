package Lennert_Bontinck_SA3.Business_Logic

/** Case class that represents a product with a quantity having:
 * - product: Product
 * - quantity: Int */
case class ProductWithQuantity(product: Product,
                               quantity: Int) {


  //---------------------------------------------------------------------------
  //| START QUANTITY MANAGEMENT FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to add quantity to ProductWithQuantity object.
   * If negative or zero is supplied object remains the same. */
  def addQuantity(quantityToAdd: Int): ProductWithQuantity =
    if (quantityToAdd > 0) {
      copy(product = product,
        quantity = quantity + quantityToAdd)
    } else {
      this
    }


  /** Function to subtract quantity to ProductWithQuantity object.
   * If negative or zero is supplied object remains the same.
   * If subtraction would result in negative quantity, object remains the same. */
  def subtractQuantity(quantityToSubtract: Int): ProductWithQuantity =
    if (quantityToSubtract > 0 && quantity - quantityToSubtract >= 0) {
      copy(product = product,
        quantity = quantity - quantityToSubtract)
    } else {
      this
    }

  //---------------------------------------------------------------------------
  //| END QUANTITY MANAGEMENT FUNCTIONS
  //---------------------------------------------------------------------------
}
