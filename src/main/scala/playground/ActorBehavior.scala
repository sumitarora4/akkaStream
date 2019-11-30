package playground

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props


object ActorBehavior extends App{
  
  /*
   * My Solution
   */
  
  /*class CounterActor extends Actor{
    
    var countVal: Int = 0 
    
    def receive: Receive = {
      
      case "Increment" => countVal +=2
      case "Decrement" => countVal -=1
      case "Print" => println(s"[Counter Actor] value is: $countVal")
      
    }
    
  }
  
   val system = ActorSystem("ActorBehavior")
    val counterActor = system.actorOf(Props[CounterActor],"counteractor")
    counterActor ! "Increment"
    counterActor ! "Decrement"
    counterActor ! "Print"*/
    
    
    /*
     * For best practices use case class and case object 
     * to call an Actor handler
     * 
     */
    
    object Counter{
    case object Increment
    case object Decrement
    case object Print
   }
   
   
   class CounterActor extends Actor{
     
     import Counter._
     
     var counterVar: Int = 0
     
      def receive:Receive ={
       case Increment => counterVar +=3
        case Decrement => counterVar -=1
         case Print => println(s"[Counter Actor] Current value: $counterVar")
     }
     
   }
   
   val system = ActorSystem("CounterActorDemo")
   val counterActor = system.actorOf(Props[CounterActor],"counterActor")
   
   import Counter._
   
   (0 to 3).foreach(_=>counterActor ! Increment)
   (0 to 3).foreach(_=>counterActor ! Decrement)
   counterActor ! Print
   
    
  
}