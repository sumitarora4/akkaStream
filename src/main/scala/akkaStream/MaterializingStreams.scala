package akkaStream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import scala.util.Success
import scala.util.Failure
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Keep

object MaterializingStreams extends App {
  
  implicit val system = ActorSystem("MaterializingStreams")
  implicit  val materializer = ActorMaterializer()
  
  val simpleGraph = Source(1 to 10).to(Sink.foreach(println))
//  val simpleMarializedValue = simpleGraph.run()
  
  val source = Source(1 to 10)
  val sink = Sink.reduce[Int]((a,b) => a+b)
  val sumFuture = source.runWith(sink)
  
//   import scala.concurrent.ExecutionContext.Implicits.global
   import system.dispatcher
   
  sumFuture.onComplete{
    case Success(value) => println("the sum of value "+value)
    case Failure(ex) => println(s"couldnt be computed $ex")
  }
  
  // choosing materialized value  
  val simpleSource = Source(1 to 10)
  val simpleFlow = Flow[Int].map(x => x+1)
  val simpleSink = Sink.foreach[Int](println)
  
  val graph=  simpleSource.viaMat(simpleFlow)(Keep.right).toMat(simpleSink)(Keep.right)
  
  graph.run().onComplete{
    case Success(_)=> println("Stream Processiong finished")
    case Failure(ex) => println(s"couldnt be computed $ex")
  }
  
  // sugars
 //Source(1 to 10).runWith(Sink.reduce(_ + _)) //Note: // source.runWith(Sink.reduce)(keep.right) this always keeps right materialized value 
                                                     //  it is also equvivalent to source.to(Sink.reduce)(keep.right) 
 val sum =  Source(1 to 10).runReduce(_ + _)
 sum.onComplete{
    case Success(_)=> println("Stream Processiong finished2")
    case Failure(ex) => println(s"couldnt be computed $ex")
  }
  
 
 // backwards
 Sink.foreach[Int](println).runWith(Source.single(42))  // Note: source...to(sink...).run() always keeps left materialized value
  
 //both ways
 // In following way flow run with both components source and sink
 Flow[Int].map(x=> x+2).runWith(simpleSource, simpleSink)
 
 
 /* Exercise:
  * -1)  return the last element of the source(use Sink.last)
  */
 
 
 val newSource = Source(1 to 10)
 val newSink = Sink.last
// val newGraph = newSource.toMat(newSink)(Keep.right).run() // not working dont know why ??
                                                           // I think holding a future[Nothing] value in a variable will not work
                                                             
 val f1 = Source(1 to 10).toMat(Sink.last)(Keep.right).run() // however this will work
 f1.onComplete{
    case Success(value)=> println("last element="+value)
    case Failure(ex) => println(s"couldnt be computed $ex")
  }
  
 // or
 val f2 = Source(1 to 10).runWith(Sink.last)
 
 /*
  *  2) Compute the total word count out of a stream of sentences
  *  - using map, flow, reduce
  */
 
 val sentenceSource = Source(List("Akka is awesome", "I love streams", "Materialized values killing"))
 val wordCountSink = Sink.fold[Int, String](0)((currentWords, newSentence) => currentWords + newSentence.split(" ").length)
 val g1 = sentenceSource.toMat(wordCountSink)(Keep.right).run()
 g1.onComplete{
    case Success(value)=> println("total word count="+value)
    case Failure(ex) => println(s"couldnt be computed $ex")
  }
 // or
 val g2 = sentenceSource.runWith(wordCountSink)
 // or
 val g3 = sentenceSource.runFold(0)((currentWords, newSentence) => currentWords + newSentence.split(" ").length)
 
 // or by using flow 
 val wordCountFlow = Flow[String].fold(0)((currentWords, newSentence) => currentWords + newSentence.split(" ").length)
 val g4 = sentenceSource.via(wordCountFlow).toMat(Sink.head)(Keep.right).run()
 
 //or
 val g5 = sentenceSource.viaMat(wordCountFlow)(Keep.left).toMat(Sink.head)(Keep.right).run()
 
 //or
 val g6 = sentenceSource.via(wordCountFlow).runWith(Sink.head)
 
 //or
 val g7 = wordCountFlow.runWith(sentenceSource, Sink.head)._2
 
 
 
}
