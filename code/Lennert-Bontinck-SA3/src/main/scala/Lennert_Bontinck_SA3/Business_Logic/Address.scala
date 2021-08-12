package Lennert_Bontinck_SA3.Business_Logic

/** Case class that represents an address as a point with cartesian coordinates having:
 * - x: Double
 * - y: Double */
case class Address(x: Double,
                   y: Double) {

  /** Function to calculate distance to different address.
   * Calculates square root of sum of squared difference between x and y of both addresses */
  def distanceTo(otherAddress: Address): Double =
    math.sqrt( math.pow(x - otherAddress.x, 2) + math.pow(y - otherAddress.y, 2)  )
}