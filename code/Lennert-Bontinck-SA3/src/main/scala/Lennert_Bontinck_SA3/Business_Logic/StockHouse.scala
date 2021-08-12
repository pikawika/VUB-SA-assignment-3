package Lennert_Bontinck_SA3.Business_Logic

/** Case class that represents a warehouse having:
 * - products: list of ProductWithQuantity objects: List[Product]
 * - address: Address */
case class StockHouse(products: List[Product],
                      address: Address)
