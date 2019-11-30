package playground

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef

object ChildActorExercise extends App {
  
 
  
  object Requester{
    case class sendTo(master: ActorRef)
    case object count
    
  }
  
  class Requester extends Actor {
    import Requester._
    import WordCountMaster._
    
     def receive: Receive ={
      
       case sendTo(wordCountMasterActor) => 
         println(s"${self.path}: sending message to word count master")
         wordCountMasterActor ! Initialize(1)
         wordCountMasterActor ! WordCountTask("Akka is Awesome")
       case count: Int => println(s"${self.path}: count: $count")
    }
  }
  
   object WordCountMaster {
    
    case class Initialize(nChildren: Int)
    case class WordCountTask(text: String)
    case class WordCountReply(count: Int)
  }
   
  class WordCountMaster extends Actor{
    
    import WordCountMaster._
    def receive: Receive ={
            
      case Initialize(nchilder) => 
        println(s"creating worker actor")
        val wordCountWorkerActor = context.actorOf(Props[WordCountWorker], "wordCountWorkerActor")
        context.become(withWorker(wordCountWorkerActor))
        
       
        
    }
    
    def withWorker(workerRef: ActorRef): Receive = {
       case WordCountTask(textMessage) => 
         val originalSender = sender()
         workerRef ! textMessage
       
        case WordCountReply(count) => 
         println(s"${self.path} word count is : $count")
         sender()! count
       

       
    }
  }
  
  class WordCountWorker extends Actor {
    import WordCountMaster._
    def receive: Receive = {
      
      case textMesssage: String => 
        println(s"text is: $textMesssage")
        val count =    textMesssage.split(" ").length     
        sender() ! WordCountReply(count)
        
        
    }
  }
  
  import Requester._
  val system = ActorSystem("ChildActorExerciseDemo")
  val requesterActor = system.actorOf(Props[Requester],"requesterActor")
  
  val wordCountMasterActor = system.actorOf(Props[WordCountMaster],"wordCountMasterActor")
  
  requesterActor ! sendTo(wordCountMasterActor)
  
  
}