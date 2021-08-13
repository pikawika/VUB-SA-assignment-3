package Lennert_Bontinck_SA3.Communication_Logic.Ephemerals

import Lennert_Bontinck_SA3.Communication_Logic.Messages.{OrderDelayed, OrderShipped}
import akka.actor.Actor

import java.util.UUID

class ClientChildService(corrID: UUID) extends Actor {

  //---------------------------------------------------------------------------
  //| START RECEIVE FUNCTION
  //---------------------------------------------------------------------------

  /** Receive function which processes incoming messages (Actor specific function). */
  def receive: Receive = {
    case OrderShipped() =>
    //TODO
    case OrderDelayed() =>
    //TODO
  }

  //---------------------------------------------------------------------------
  //| END RECEIVE FUNCTION
  //---------------------------------------------------------------------------
  //| START XXX
  //---------------------------------------------------------------------------



}
