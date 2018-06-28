package com.casestudy.twitter.interpreters

import java.util.Date

import cats.effect.IO
import com.casestudy.twitter.Algebra
import com.casestudy.twitter.model._
import com.casestudy.twitter.implicits._
import com.casestudy.twitter.types._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import io.circe.literal._
import io.circe.syntax._
import io.circe._
import fs2._

object DoobieStreamInterpreter extends Algebra[StreamIO, Json]{
    val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://[::1]/twitter",
        "postgres",
        ""
    )

    override def tweet(username: String, date: Date, content: String): Stream[IO, Json] =
        Stream.eval(sql"INSERT INTO tweets (username, date, content) VALUES (${username}, ${new Date()}, $content)"
          .update.run.transact(xa).map(r => json"""{"result": $r}"""))

    override def deleteTweet(username: String, tweetId: Int): Stream[IO, Json] =
        Stream.eval(sql"DELETE FROM tweets WHERE username = ${username} AND id = ${tweetId}"
          .update.run.transact(xa).map(r => json"""{"result": $r}"""))

    override def follow(followerUsername: String, followingUsername: String): Stream[IO, Json] =
        Stream.eval(sql"INSERT INTO following (follower, following) VALUES (${followerUsername}, ${followingUsername})"
          .update.run.transact(xa).map(r => json"""{"result": $r}"""))

    override def unFollow(followerUsername: String, followingUsername: String): Stream[IO, Json] =
        Stream.eval(sql"DELETE FROM following WHERE follower = ${followerUsername} AND following = ${followingUsername}"
          .update.run.transact(xa).map(r => json"""{"result": $r}"""))

    override def fetchUser(username: String): Stream[IO, Json] =
        Stream.eval(sql"SELECT username, email, firstName, lastName, birthday from users WHERE username = $username"
          .query[User].unique.transact(xa).map(_.asJson))

    override def fetchTweet(id: Int): Stream[IO, Json] =
        Stream.eval(sql"SELECT * from tweets WHERE id = $id"
          .query[Tweet].unique.transact(xa).map(_.asJson))

    override def fetchTweets(username: String): Stream[IO, Json] =
        sql"SELECT * from tweets WHERE username = ${username} ORDER BY date ASC"
          .query[Tweet].stream.map(_.asJson).transact(xa)

    override def fetchFeed(username: String): Stream[IO, Json] =
        sql"SELECT * from tweets WHERE username IN (SELECT followee FROM following WHERE follower = ${username}) ORDER BY date ASC"
          .query[Tweet].stream.map(_.asJson).transact(xa)

    override def fetchFollowing(username: String): Stream[IO, Json] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.followee AND following.follower = ${username}"
          .query[User].stream.map(_.asJson).transact(xa)

    override def fetchFollowers(username: String): Stream[IO, Json] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.follower AND following.followee = ${username}"
          .query[User].stream.map(_.asJson).transact(xa)
}
