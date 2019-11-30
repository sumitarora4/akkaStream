package akkaStream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Flow
import scala.concurrent.Future

object FirstPrinciple extends App{
  
  implicit val system = ActorSystem("FirstPrincipleDemo")
  implicit  val materializer = ActorMaterializer()
  
  val source = Source(1 to 10) 
  val sink  = Sink.foreach[Int](println)
  
  val graph = source.to(sink)
//  graph.run()
 
  
  val flow = Flow[Int].map(x => x+1)
  val sourceWithFlow = source.via(flow)
  val flowWithSink = flow.to(sink)
  
//  sourceWithFlow.to(sink).run()
//  source.to(flowWithSink).run()
//  source.via(flow).to(sink).run()
  
  // nulls are NOT allowed. and this will give null pointer exception while run 
//  val illegalSource = Source.single[String](null)
//  illegalSource.to(Sink.foreach[String](println)).run()
  // use option instead
  
  // various kinds of sources
  val finiteSource = Source.single(1)
  val anotherFiniteSource = Source(List(1,2,3))
  val emptySource = Source.empty[Int]
  val infiniteSource = Source(Stream.from(1))

  import scala.concurrent.ExecutionContext.Implicits.global
  val futureSource = Source.fromFuture(Future(32))
  
  // various kinds of sinks
  
  val theMostBoringSink = Sink.ignore
  val forEachSink = Sink.foreach[String](println)
  val headSink = Sink.head[Int] // retrieves head and then closes the stream
  val foldSink = Sink.fold[Int, Int](0)((a,b) => a+b)

  // kinds of flow - ususally map to collection operator
  val mapFlow = Flow[Int].map(x => x*2)
  val takeFlow = Flow[Int].take(5)
   // do NOT have flapMap
  
  // graph
  val doubleFlowGraph = source.via(mapFlow).via(takeFlow).to(sink)
  doubleFlowGraph.run()
  
  
  // syntactic sugars
  
  val mapSource = Source(1 to 10).map(x => x+2) // which is equvivalent to Source(1 to 10).via(Flow[Int].map(x => x+2))
  // run streams directly
  
  mapSource.runForeach(println)  // which is equivalent to mapSource.to(Sink.foreach[Int](println)).run()
  
  /*
   * Exercise: Create a stream that takes a name of persons, then you will keep first 2 names with the length > 5 characters
   */
  
  
  val nameSource = Source(List("Sumit","KUmaradfs","Ravindra", "Faxvitesdwersd"))
  val nameFlow = Flow[String].filter(x => x.length() > 5 )
  val nameTakeFlow = Flow[String].take(2)
  
  
  val nameFlowGraph = nameSource.via(nameFlow).via(nameTakeFlow).to(forEachSink)
  nameFlowGraph.run()
  
  // shorthand of above
//    nameFlowGraph.filter(_.length >5).take(2).runForeach(println)
  
//  Source.single("hello Stream").to(Sink.foreach(println)).run()
}