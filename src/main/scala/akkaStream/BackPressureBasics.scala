package akkaStream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Flow
import akka.stream.OverflowStrategy

object BackPressureBasics extends App {

  implicit val system = ActorSystem("BackPressureBasics")
  implicit val materializer = ActorMaterializer()

  val fastSource = Source(1 to 1000)
  val slowSink = Sink.foreach[Int] {
    x =>
      // long processing
      Thread.sleep(1000)
      println(s"Sink $x")
  }

//  fastSource.async.to(slowSink).run()

  val simpleFlow = Flow[Int].map { x =>
    println(s"Incoming $x")
    x + 1
  }
  
  fastSource.async
  .via(simpleFlow).async
  .to(slowSink)
//  .run()
  
  /*
   *  reactions to backpressure (in order):
   *  - try to slow down if possible
   *  - buffer elements utill theres more demand
   *  - drop down elements from the buffer if it overflows
   *  - tear down/ kill the whole stream(failure)
   */
  
  val bufferedFlow = simpleFlow.buffer(10, overflowStrategy = OverflowStrategy.dropHead)
  fastSource.async
  .via(bufferedFlow).async
  .to(slowSink)
  .run()
  
  

}