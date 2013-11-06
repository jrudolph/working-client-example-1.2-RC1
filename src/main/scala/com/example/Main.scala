package com.example

import akka.actor._
import spray.client.pipelining._
import scala.util.{Failure, Success}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import scala.concurrent.duration._
import spray.util._

object Main extends App {

  implicit val system = ActorSystem("simple-spray-client")
  import system.dispatcher
  val pipeline = sendReceive

  //this value will fail
  val s= "https://news.google.com/news/feeds?q=chicago+illinois+business+news&client=safari&rls=en&bav=on.2,or.r_cp.r_qf.&bvm=bv.47380653,d.dmQ&biw=1909&bih=1005&um=1&ie=UTF-8&output=rss"

 //this value will succeed
  //val s="http://rss.nytimes.com/services/xml/rss/nyt/Science.xml"

  val responseFuture = pipeline(Get(s))

  responseFuture onComplete {
    case Success(response) => {
     println( "GOT RESPONSE "+response)
     shutdown()
    }

    case Failure(error) =>  shutdown()
  }

  def shutdown(): Unit = {
    IO(Http).ask(Http.CloseAll)(1.second).await
    system.shutdown()
  }

}
