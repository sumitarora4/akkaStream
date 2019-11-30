package playground

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
 
object ChangingActorBehavior extends App {
  
  object FussyKid {
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }
  
  /*class FussyKid extends Actor {    
    import FussyKid._
    import Mom._
    
    var state = HAPPY
    
    def receive:Receive ={
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(messaage) => 
        if (state == HAPPY) sender()! KidAccept
        else sender()! KidReject
   }
  }*/
  
  
  
//  with no mutable state of variables like var state = HAPPY
  class StateLessFessyKid extends Actor{
    import Mom._
    import FussyKid._
    
    def receive: Receive = happyReceiver
      
      def happyReceiver: Receive = {
        case Food(VEGETABLE) =>  context.become(sadReceiver, false)
        case Food(CHOCOLATE)  =>
        case Ask(_) => sender() ! KidAccept
      }
      
      def sadReceiver: Receive = {
        case Food(VEGETABLE) =>  context.become(sadReceiver, false) // false will stack actor handler on the top, while 
                                                                    // true will replace current actor handler from the stack
        
        case Food(CHOCOLATE)  => context.unbecome() // this will popup last actor handler from stack
        case Ask(_) => sender() ! KidReject
      }
    
  }
  
  object Mom{
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String)
    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate" 
  }
  
  class Mom extends Actor {
    
    import Mom._
    import FussyKid._
    
    def receive:Receive ={
      
      case MomStart(kidRef) => 
        kidRef ! Food(VEGETABLE)
         kidRef ! Food(VEGETABLE)
          kidRef ! Food(CHOCOLATE)
           kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("do you want to play")
        
      case KidAccept => println("kid is happy")
      case KidReject => println("kid is sad")
    }
  }
  
  val system = ActorSystem("ChangingActorBehavior")
  val momActor = system.actorOf(Props[Mom], "momActor")
//  val fussyKidActor = system.actorOf(Props[FussyKid], "fussyKidActor")
  
  val stateLessFessyKidActor = system.actorOf(Props[StateLessFessyKid], "StateLessFessyKidActor")
  
  import Mom._
 // momActor ! MomStart(fussyKidActor)
  
  momActor ! MomStart(stateLessFessyKidActor)
  
}