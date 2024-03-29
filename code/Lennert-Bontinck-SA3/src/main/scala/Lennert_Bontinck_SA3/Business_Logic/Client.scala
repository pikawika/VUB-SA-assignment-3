package Lennert_Bontinck_SA3.Business_Logic

/** Case class that represents a client having:
 * - name: name of the client - String
 * - address: point with cartesian coordinates - Address
 * - primeMember: whether or not client is prime member - Boolean */
case class Client(name: String,
                  address: Address,
                  primeMember: Boolean) {

  //---------------------------------------------------------------------------
  //| START PRIVATE VARS
  //---------------------------------------------------------------------------

  /** The purchases of a client. */
  private var purchases: Set[Purchase] = Set()

  //---------------------------------------------------------------------------
  //| END PRIVATE VARS
  //---------------------------------------------------------------------------
  //| START PURCHASE FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to add a purchase for a client. */
  def addPurchase(purchase: Purchase): Unit = {
    purchases = purchases + purchase
  }

  //---------------------------------------------------------------------------
  //| END PURCHASE FUNCTIONS
  //---------------------------------------------------------------------------
  //| START PRIME MEMBERSHIP FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to update prime membership state. */
  def updatePrimeMembership(newPrimeStatus: Boolean): Client = {
    copy(name = name,
      address = address,
      primeMember = newPrimeStatus)
  }


  //---------------------------------------------------------------------------
  //| END PRIME MEMBERSHIP FUNCTIONS
  //---------------------------------------------------------------------------
}




