package org.queue

import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http._
import HttpVersion._
import HttpResponseStatus._
import com.twitter.util.Future
import scala.collection.mutable.Queue
import org.jboss.netty.handler.codec.http.multipart._

class QueueService extends Service[HttpRequest, HttpResponse] {
  val q = Queue[String]()
  
  def apply(request: HttpRequest) = {
    request.getUri match {
      case "/post" => post(request)
      case "/get" => get
      case x => println(x)
    }

    Future(new DefaultHttpResponse(HTTP_1_1, OK))
  }
  
  def post(request: HttpRequest) = if(request.getMethod == HttpMethod.POST) {
    val decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request)
    val value = decoder.getBodyHttpData("message").asInstanceOf[Attribute].getValue
    
    q.enqueue(value)
  }
  
  def get {
    println(q.dequeue)
  }
}