package playground

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef

object ActorCapabilities extends App{
  
  class SimpleActor extends Actor{
    
    def receive:Receive ={
      
      case "Hi" => sender() ! "Hello There" // replying to a message
      case message: String => println(s"[${self}] I have received: $message")
      case number: Int => println(s"[Simple Actor] I have received Number: $number")
      case SpecialMessage(contents) => println(s"[Simple Actor] I have received something special: $contents")
      case SendMessageToYourself(content) => self ! content
      case SayHiTo(ref) => ref ! "Hi"
      case WirelessPhoneMessage(content, ref) => ref forward (content + "s")
      
    }
  }
  
  val system = ActorSystem("actorCapablitiyDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")
  
  
  /*
   * 1- Capabilities of Actor:
   * Message can be of any type
   * messages must be immutable
   * messages must be serializable
   * 
   */
  simpleActor ! "hello Sumit"
  simpleActor ! 43
  
  case class SpecialMessage(contents: String)
  
  simpleActor ! SpecialMessage("My Special message for you buddy!")
  
  // 2- actor have info about their context and references
  // context.self === 'this'
  
  case class SendMessageToYourself(content: String)
  
  simpleActor ! SendMessageToYourself("I am an actor and I am proud of it")
  
  
  // 3- actors can REPLY to messages
  
  val alice = system.actorOf(Props[SimpleActor],"alice")
  val bob = system.actorOf(Props[SimpleActor],"bob")
  
  case class SayHiTo(ref: ActorRef)
  
  alice ! SayHiTo(bob)
  
  // 4- dead letters
  alice ! "Hi"
  
  // 5- forwarding messages
  // forwarding = sending a mesage with original sender
  
  
  case class WirelessPhoneMessage(content: String, ref: ActorRef)
  
  alice ! WirelessPhoneMessage("Hey EveryOne", bob) //original sender is the default "noSender"
  
  
  
  
  
  
  
  
}