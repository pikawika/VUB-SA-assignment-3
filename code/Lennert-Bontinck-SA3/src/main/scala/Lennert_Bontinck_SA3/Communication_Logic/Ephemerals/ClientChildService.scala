package Lennert_Bontinck_SA3.Communication_Logic.Ephemerals

import Lennert_Bontinck_SA3.Communication_Logic.Messages.{OrderDelayed, OrderShipped}
import akka.actor.{Actor, ActorLogging}

import java.util.UUID

/** Ephemeral child actor for the client, is responsible for managing a singular purchase. */
class ClientChildService(corrID: UUID) extends Actor with ActorLogging {

  //---------------------------------------------------------------------------
  //| START RECEIVE FUNCTION
  //---------------------------------------------------------------------------

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case OrderShipped(`corrID`) =>
      // No real behaviour is needed per assignment
      log.info("ClientChildService " + corrID + ": got OrderShipped message, I'm happy and terminate myself.")
      context.stop(self)
    case OrderShipped(wrongID) =>
      // Got wrong corrID, terminate per default behaviour seen in lecture
      log.info("ClientChildService " + corrID + ": got wrong corrID: " + wrongID)
      context.stop(self)
    case OrderDelayed(`corrID`) =>
      // No real behaviour is needed per assignment
      log.info("ClientChildService " + corrID + ": got OrderDelayed message, I'm sad and terminate myself.")
      context.stop(self)
    case OrderDelayed(wrongID) =>
      // Got wrong corrID, terminate per default behaviour seen in lecture
      log.info("ClientChildService " + corrID + ": got wrong corrID: " + wrongID)
      context.stop(self)
  }

  //---------------------------------------------------------------------------
  //| END RECEIVE FUNCTION
  //---------------------------------------------------------------------------


}
