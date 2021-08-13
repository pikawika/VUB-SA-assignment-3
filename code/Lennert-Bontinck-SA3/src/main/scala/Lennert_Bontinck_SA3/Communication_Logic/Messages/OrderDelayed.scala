package Lennert_Bontinck_SA3.Communication_Logic.Messages

import java.util.UUID

/** Case class that represents a message to let know that an order is delayed.
 * CorrID used since working with ephemeral child actors. */
case class OrderDelayed(corrID: UUID)
