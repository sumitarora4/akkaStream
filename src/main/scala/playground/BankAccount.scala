package playground

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef

object BankAccount extends App{
  
  object Operation{
    
    case class Deposit(amount: Int, ref: ActorRef)
    case class Withdraw(amount: Int, ref: ActorRef)
    case object Statement
  }
  class BankAccountActor extends Actor{
     import Operation._
    var balance: Int = 0
   
    def receive:Receive ={
      
      case message: String => println(s"[${self}] I have received: $message")
      case Deposit(amount, ref) => 
        
          balance += amount
          ref ! "Deposit Scuccessful"
        
      case Withdraw(amount, ref) =>
        
      if(balance <= 0 || balance <= amount){
        
          ref ! "Withdraw Failed:"+ balance
        }
        else{
          balance -= amount
          ref ! "Withdraw Scuccessful"+ balance
        }
        
      case Statement => println(s"[bankAccountActor] current balance: $balance ")
        
    }
    
  }
  
  val system = ActorSystem("BankAccountActorSystem")
  val bankAccountActor = system.actorOf(Props[BankAccountActor], "bankAccountActor")
  val messageActor = system.actorOf(Props[BankAccountActor],"messageActor")
  
  import Operation._
  bankAccountActor ! Deposit(0,messageActor )
  bankAccountActor ! Withdraw(5, messageActor)
  bankAccountActor ! Statement
  
  
}