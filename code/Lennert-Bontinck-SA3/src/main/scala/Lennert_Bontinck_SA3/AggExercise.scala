package agg

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object AggExercise extends App {

  // Validity Messages
  trait Validity
  case object ValidPassport extends Validity
  case object InvalidPassport extends Validity
  case object VisaGranted extends Validity
  case object VisaRefused extends Validity
  case object ValidTickets extends Validity
  case object InvalidTickets extends Validity

  // Messages
  case class Visa(passport: Passport, replyTo: ActorRef)
  case class Passport(name: String, endYear: Int, replyTo: ActorRef)
  case class PlaneTicket(passport: Passport, dayOfWeekToPlane: String, replyTo: ActorRef)
  case class RequestTrip(username: String, replyTo: ActorRef)
  case class ResultProcess(message: String)

  // Actor to handle the visas
  class VisaActor extends Actor with ActorLogging {
    def receive: Receive = {
      case Visa(passport, replyTo) =>
        log.info("Processing Visa ...")
        log.info(s"The person ${passport.name} requested a Visa ...")
        if (passport.endYear > 2020 )
          replyTo ! VisaGranted
        else
          replyTo ! VisaRefused
    }
  }

  // Actor to handle the passports
  class PassportActor extends Actor with ActorLogging {
    def receive: Receive = {
      case Passport(name, _, replyTo) =>
        log.info("Processing Passport ...")
        log.info(s"The person $name is requesting a passport ...")
        if (name.nonEmpty)
          replyTo ! ValidPassport
        else replyTo ! InvalidPassport
    }
  }

  // Actor to handle the plane tickets
  class PlaneTicketActor extends Actor with ActorLogging {
    def receive: Receive = {
      case PlaneTicket(_, dayOfWeekToPlane, replyTo) =>
        log.info("Processing Tickets ...")
        if (dayOfWeekToPlane.equals("Friday"))
          replyTo ! InvalidTickets
        else replyTo ! ValidTickets
    }
  }

  // Actor as middle-man that 'aggregates' all missing parts
  class TravelRequest(visa: ActorRef, passport: ActorRef, tickets: ActorRef) extends Actor {
    def receive: Receive  = {
      case RequestTrip(username, replyTo) =>
        val aggregator: ActorRef = context.actorOf(Props(new ProcessingAggregator(username, replyTo)), name = "Processing")
        val passportObject: Passport = Passport("Camilo", 2024, aggregator)

        passport ! passportObject
        visa ! Visa(passportObject, aggregator)
        tickets ! PlaneTicket(passportObject, "Tuesday", aggregator)
    }
  }

  // The Aggregator of the parts
  class ProcessingAggregator(username: String, replyTo: ActorRef) extends Actor with ActorLogging {

    var validDocuments: Int = 0

    def receiveResult: Receive = {
      case ValidPassport =>
        log.info("Passport received and valid !!!")
        validDocuments += 1
      case InvalidPassport => log.error("Passport received but not valid")
      case VisaGranted =>
        log.info("Visa granted. Congrats !!!")
        validDocuments += 1
      case VisaRefused => log.info("Sorry the country refused your request :(")
      case ValidTickets =>
        log.info("Tickets received and valid")
        validDocuments += 1
      case InvalidTickets => log.info("There's something wrong with the tickets")
    }

    def checkCompletedProcess: Receive  = {
      case _ => log.info(s"Receiving parts ...")
        if (validDocuments == 3) {
          log.info("Documents approved")
          replyTo ! ResultProcess(s"The user $username is allowed to travel")
          context.stop(self)
        }
    }

    def receive: Receive = receiveResult andThen checkCompletedProcess
  }

  // The Website to request a trip to a certain country
  class WebsiteTrip(travelRequest: ActorRef) extends Actor with ActorLogging {
    travelRequest ! RequestTrip("camilo", self)

    def receive: Receive = {
      case ResultProcess(msg) => log.info(msg)
    }
  }

  val actorSystem: ActorSystem = ActorSystem("AkkaApplication")
  val visaActor: ActorRef = actorSystem.actorOf(Props[VisaActor], name = "Visa")
  val passportActor: ActorRef = actorSystem.actorOf(Props[PassportActor], name = "Passport")
  val ticketsActor: ActorRef = actorSystem.actorOf(Props[PlaneTicketActor], name = "PlaneTickets")

  val requestTravel: ActorRef = actorSystem.actorOf(Props(new TravelRequest(visaActor, passportActor, ticketsActor)), name = "Request")
  val websiteTrip: ActorRef = actorSystem.actorOf(Props(new WebsiteTrip(requestTravel)), name = "Website")
}
