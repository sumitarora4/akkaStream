package playground

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props

object ActorIntro extends App{
  
  // part-1 actor system instialization
  val actorSytem = ActorSystem("FirstActorSystem")
   println(actorSytem.name)
   
   // part-2 create actor
   // word count actor
   
   class WordCountActor extends Actor{
    
    // internal data
    var totalWords = 0
    
    // behavior 
    def receive: PartialFunction[Any, Unit] = {
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords += message.split(" ").length
        println(s"[totalWords] : $totalWords")
      
      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }
    
    // part-3 instantiate an actor
    
    val wordCounter = actorSytem.actorOf(Props[WordCountActor], "wordCounter")
    val anotherWordCounter = actorSytem.actorOf(Props[WordCountActor], "anotherwordCounter")
    
    // part-4 communicate
    wordCounter ! "I'm learning Akka and it's pretty damn cool"
    anotherWordCounter ! "a differenct message"
    

    /*
     * creating actor with construture argument of a class
     */
    
    object Person {
      def props(name: String)= Props(new Person(name))
    }
    
    class Person(name: String) extends Actor{      
      def receive: Receive= {
        case "hi" => print(s"Hi I'm $name")
        case _=>
      }
    }
    
    val person = actorSytem.actorOf(Person.props("Sumit"))
    person ! "hi"
    
} 
