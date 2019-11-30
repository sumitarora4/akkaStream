package playground

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

object ActorCounterBehavior extends App{
  
  object counterOperation {
    
    case object Increment
    case object Decrement
    case object Print
  }
  
  class Counter extends Actor{
    
    import counterOperation._
    def receive: Receive = CounterReceive(0)
    
      def CounterReceive(count: Int): Receive ={
      case Increment => 
        println(s"$count Incrementing ")
        context.become(CounterReceive(count+1))
      case Decrement => 
        println(s"$count Decrementing")
        context.become(CounterReceive(count-1))
      case Print => println(s"[Counter Receive] Final Value:  $count")
        
    }
      
  }
  
  val system = ActorSystem("ActorCounterDemo")
  val counterActor = system.actorOf(Props[Counter], "counterActor")
  
  import counterOperation._
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Decrement
  counterActor ! Decrement
  counterActor ! Print
  
}