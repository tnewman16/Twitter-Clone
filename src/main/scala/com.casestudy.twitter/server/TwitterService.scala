package com.casestudy.twitter.server

import java.util.Date

import cats.effect.IO
import com.casestudy.twitter.Algebra
import com.casestudy.twitter.interpreters._
import io.circe.Json
import org.http4s.HttpService
import org.http4s.dsl.io._
import org.http4s.circe._

object TwitterService {
    val interpreter = DoobieStreamInterpreter
    val stringInterpreter: Algebra[IO, String] = DoobieStringInterpreter

    val service: HttpService[IO] = {
        HttpService[IO] {
            case GET -> Root / "users" / username =>
                Ok(interpreter.fetchUser(username))
            case GET -> Root / "users" / username / "tweets" =>
                Ok(interpreter.fetchTweets(username))
            case GET -> Root / "users" / username / "followers" =>
                Ok(interpreter.fetchFollowers(username))
            case GET -> Root / "users" / username / "following" =>
                Ok(interpreter.fetchFollowing(username))
            case GET -> Root / "users" / username / "feed" =>
                Ok(interpreter.fetchFeed(username))
            case GET -> Root / "users" / username / "follow" / followeeUsername =>
                Ok(interpreter.follow(username, followeeUsername))
            case GET -> Root / "users" / username / "unfollow" / followeeUsername =>
                Ok(interpreter.unFollow(username, followeeUsername))
            case GET -> Root / "users" / username / "unfollow" / followeeUsername =>
                Ok(DoobieStringInterpreter.unFollow(username, followeeUsername))
            case req @ POST -> Root / "users" / username / "tweet" =>
                for {
                    json <- req.as[Json]
                    content = json.hcursor.downField("content").as[String]
                    response <- content match {
                        case Right(c) => Ok(interpreter.tweet(username, new Date(), c))
                        case _ => BadRequest()
                    }
                } yield response
            case DELETE -> Root / "users" / username / "tweets" / IntVar(id) =>
                Ok(interpreter.deleteTweet(username, id))
            case GET -> Root / "tweets" / IntVar(id) =>
                Ok(interpreter.fetchTweet(id))

            // endpoints ending with "/string" return responses as Strings
            case GET -> Root / "users" / username / "string" =>
                Ok(stringInterpreter.fetchUser(username))
            case GET -> Root / "users" / username / "tweets" / "string" =>
                Ok(stringInterpreter.fetchTweets(username))
            case GET -> Root / "users" / username / "followers" / "string" =>
                Ok(stringInterpreter.fetchFollowers(username))
            case GET -> Root / "users" / username / "following" / "string" =>
                Ok(stringInterpreter.fetchFollowing(username))
            case GET -> Root / "users" / username / "feed" / "string" =>
                Ok(stringInterpreter.fetchFeed(username))
            case GET -> Root / "users" / username / "follow" / followeeUsername / "string" =>
                Ok(stringInterpreter.follow(username, followeeUsername))
            case GET -> Root / "tweets" / IntVar(id) / "string" =>
                Ok(stringInterpreter.fetchTweet(id))
        }
    }
}
