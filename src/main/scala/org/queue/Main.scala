package org.queue

import com.twitter.finagle.Service
import com.twitter.finagle.http.Http
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{DefaultHttpResponse, HttpVersion, HttpResponseStatus, HttpRequest, HttpResponse}
import java.net.{SocketAddress, InetSocketAddress}
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.builder.ServerBuilder
import HttpVersion._
import HttpResponseStatus._

object Main extends App {
  val address: SocketAddress = new InetSocketAddress(6969)

  val server: Server = ServerBuilder()
    .codec(Http())
    .bindTo(address)
    .name("Scala Queue")
    .build(new QueueService)
}