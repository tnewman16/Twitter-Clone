package com.casestudy.twitter.server

import java.util.Date

import cats.effect.IO
import com.casestudy.twitter.interpreters._
import io.circe.Json
import org.http4s.HttpService
import org.http4s.dsl.io._
import org.http4s.circe._

object TwitterService {
    val service: HttpService[IO] = {
        HttpService[IO] {
            // basic endpoints always send responses as JSON
            case GET -> Root / "users" / username =>
                Ok(DoobieJsonInterpreter.fetchUser(username))
            case GET -> Root / "users" / username / "tweets" =>
                Ok(DoobieJsonInterpreter.fetchTweets(username))
            case GET -> Root / "users" / username / "followers" =>
                Ok(DoobieJsonInterpreter.fetchFollowers(username))
            case GET -> Root / "users" / username / "following" =>
                Ok(DoobieJsonInterpreter.fetchFollowing(username))
            case GET -> Root / "users" / username / "feed" =>
                Ok(DoobieJsonInterpreter.fetchFeed(username))
            case GET -> Root / "users" / username / "follow" / followeeUsername =>
                Ok(DoobieJsonInterpreter.follow(username, followeeUsername))
            case GET -> Root / "users" / username / "unfollow" / followeeUsername =>
                Ok(DoobieJsonInterpreter.unFollow(username, followeeUsername))
            case GET -> Root / "users" / username / "unfollow" / followeeUsername =>
                Ok(DoobieStringInterpreter.unFollow(username, followeeUsername))
            case req @ POST -> Root / "users" / username / "tweet" =>
                for {
                    json <- req.as[Json]
                    content = json.hcursor.downField("content").as[String]
                    response <- content match {
                        case Right(c) => Ok(DoobieJsonInterpreter.tweet(username, new Date(), c))
                        case _ => BadRequest()
                    }
                } yield response
            case DELETE -> Root / "users" / username / "tweets" / IntVar(id) =>
                Ok(DoobieJsonInterpreter.deleteTweet(username, id))
            case GET -> Root / "tweets" / IntVar(id) =>
                Ok(DoobieJsonInterpreter.fetchTweet(id))

            // endpoints ending with "/string" return responses as strings
            case GET -> Root / "users" / username / "string" =>
                Ok(DoobieStringInterpreter.fetchUser(username))
            case GET -> Root / "users" / username / "tweets" / "string" =>
                Ok(DoobieStringInterpreter.fetchTweets(username))
            case GET -> Root / "users" / username / "followers" / "string" =>
                Ok(DoobieStringInterpreter.fetchFollowers(username))
            case GET -> Root / "users" / username / "following" / "string" =>
                Ok(DoobieStringInterpreter.fetchFollowing(username))
            case GET -> Root / "users" / username / "feed" / "string" =>
                Ok(DoobieStringInterpreter.fetchFeed(username))
            case GET -> Root / "users" / username / "follow" / followeeUsername / "string" =>
                Ok(DoobieStringInterpreter.follow(username, followeeUsername))
            case GET -> Root / "tweets" / IntVar(id) / "string" =>
                Ok(DoobieStringInterpreter.fetchTweet(id))
        }
    }
}
