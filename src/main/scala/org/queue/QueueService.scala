package org.queue

import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http._
import HttpVersion._
import HttpResponseStatus._
import com.twitter.util.Future
import scala.collection.mutable.Queue
import org.jboss.netty.handler.codec.http.multipart._
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.util.CharsetUtil

class QueueService extends Service[HttpRequest, HttpResponse] {
  val q = Queue[String]()

  def apply(request: HttpRequest) = {
    request.getUri match {
      case "/post" => post(request)
      case "/get" => get
      case x => Future(new DefaultHttpResponse(HTTP_1_1, OK))
    }
  }

  def post(request: HttpRequest) = if (request.getMethod == HttpMethod.POST) {
    try {
      val decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request)
      val value = decoder.getBodyHttpData("message").asInstanceOf[Attribute].getValue

      q.enqueue(value)
      Future(new DefaultHttpResponse(HTTP_1_1, OK))
    } catch {
      case e: Exception => Future(new DefaultHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR))
    }
  } else Future(new DefaultHttpResponse(HTTP_1_1, METHOD_NOT_ALLOWED))

  def get = {
    try {
      val resp = new DefaultHttpResponse(HTTP_1_1, OK)
      resp.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json")
      resp.setContent(ChannelBuffers.copiedBuffer(q.dequeue, CharsetUtil.UTF_8))

      Future(resp)
    } catch {
      case e: NoSuchElementException => {
        val resp = new DefaultHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR)
        resp.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json")
        resp.setContent(ChannelBuffers.copiedBuffer("""{"error": "Queue is empty"}""", CharsetUtil.UTF_8))
        Future(resp)
      }
    }
  }
}