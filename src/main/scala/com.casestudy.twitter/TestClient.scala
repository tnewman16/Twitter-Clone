package com.casestudy.twitter

import org.http4s._
import org.http4s.client.blaze._
import cats.effect._
import org.http4s.circe._
import io.circe.literal._
import jawnfs2._

object TestClient extends App {
    implicit val f = io.circe.jawn.CirceSupportParser.facade

    def postTweetRequest(username: String, content: String) =
        Request[IO](Method.POST, Uri.fromString(s"http://0.0.0.0:8081/users/$username/tweet").toOption.get)
          .withBody(json"""{"content": $content}""")

    def getFeedRequest(username: String) =
        Request[IO](Method.GET, Uri.fromString(s"http://0.0.0.0:8081/users/$username/feed").toOption.get)

    val req = getFeedRequest("tnewman")
    val sr = for {
        client <- Http1Client.stream[IO]()
        res <- client.streaming(req)(resp => resp.body.chunks.parseJsonStream)
        x = client.shutdownNow
    } yield res
    sr.compile.toList.map(println(_)).unsafeRunSync()
}
