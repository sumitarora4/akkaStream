package playground

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.ActorSystem
import playground.ParentChild.ParentOperation.CreateChild

object ParentChild extends App {

  object ParentOperation {
    case class CreateChild(name: String)
    case class TellChild(message: String)
  }

  class Parent extends Actor {

    import ParentOperation._

    //    var child: ActorRef = null // need to remove mutable variable

    def receive: Receive = {

      case CreateChild(name) =>
        println(s"${self.path} creating child")
        val childActorRef = context.actorOf(Props[Child], name) // creating child actor
        //        child = childActorRef // need to remove mutable variable assignment
        context.become(withChild(childActorRef))
    }

    def withChild(childRef: ActorRef): Receive = { 
      case TellChild(message) =>
        if (childRef != null) childRef forward message

    }
  }

  class Child extends Actor {
    def receive: Receive = {
      case message => println(s"${self.path} I got: $message")
    }
  }
  
  
  val system = ActorSystem("ParentChildDemo")
  val parentActor = system.actorOf(Props[Parent], "ParentActor")
  
  import ParentOperation._
  parentActor ! CreateChild("childActor")
  parentActor ! TellChild("Hi Kid how are you!")
  
  
  /*
   * locate any actor or Actor Selection
   */
 
  val childSelection = system.actorSelection("/user/ParentActor/childActor")
  childSelection ! "I found you childActor"
}