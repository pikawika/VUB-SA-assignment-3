package Lennert_Bontinck_SA3.Business_Logic

/** Case class that provides a manager for stock houses */
case class StockHouseManager() {

  //---------------------------------------------------------------------------
  //| START PRIVATE VARS
  //---------------------------------------------------------------------------
  /** Keep products in stock as private var */
  private var stockHouses: Set[StockHouse] = Set()

  //---------------------------------------------------------------------------
  //| END PRIVATE VARS
  //---------------------------------------------------------------------------
  //| START STOCK HOUSE CHECKING FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to find specified nearest stock houses from a given address.
   * - address: Address to find nearest stock houses for - Address
   * - amountOfStockHouses (default 5): amount of nearest stock houses to find - Int */
  def findNearestStockHouses(addressOfRequester: Address, amountOfStockHouses: Int = 5): List[StockHouse] = {
    // sort on distance and take amount specified
    stockHouses.toList.sortBy(_.address.distanceTo(addressOfRequester)).take(amountOfStockHouses)
  }

  //---------------------------------------------------------------------------
  //| END STOCK HOUSE CHECKING FUNCTIONS
  //---------------------------------------------------------------------------
  //| START STOCK HOUSE MANAGEMENT FUNCTIONS
  //---------------------------------------------------------------------------

  /** Function to add specific stock house to the manager.
   * If stock house already existed (same address), nothing happens and false is returned, else true is returned. */
  def addStockHouse(newStockHouse: StockHouse): Boolean = {
    // check if stock house already exists
    val stockHouseExists = stockHouses.exists(_.address == newStockHouse.address)

    // only add if stock house is not yet managed.
    if (!stockHouseExists) {
      stockHouses = stockHouses + newStockHouse
    }

    // return true if stock house didn't exist and thus has been added
    !stockHouseExists
  }

  //---------------------------------------------------------------------------
  //| END STOCK HOUSE MANAGEMENT FUNCTIONS
  //---------------------------------------------------------------------------

}
