package com.casestudy.twitter.server

import cats.effect.IO
import fs2.StreamApp
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object TwitterServer extends StreamApp[IO] {
    import scala.concurrent.ExecutionContext.Implicits.global

    def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, StreamApp.ExitCode] = ServerStream.stream
}

object ServerStream {
    def twitterService: HttpService[IO] = TwitterService.service

    def stream(implicit ec: ExecutionContext): fs2.Stream[IO, StreamApp.ExitCode] =
        BlazeBuilder[IO]
          .bindHttp(8081, "0.0.0.0")
          .mountService(twitterService, "/")
          .serve
}