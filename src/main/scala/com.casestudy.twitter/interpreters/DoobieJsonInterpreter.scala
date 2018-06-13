package com.casestudy.twitter.interpreters

import java.util.Date

import cats.effect.IO
import com.casestudy.twitter.Algebra
import com.casestudy.twitter.model._
import com.casestudy.twitter.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import io.circe.literal._
import io.circe.syntax._
import io.circe._

object DoobieJsonInterpreter extends Algebra[IO]{
    val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql:twitter",
        "postgres",
        ""
    )

    override def tweet(username: String, date: Date, content: String): IO[Json] =
        sql"INSERT INTO tweets (username, date, content) VALUES (${username}, ${new Date()}, $content)"
          .update.run.transact(xa).map(r => json"""{"result": $r}""")

    override def deleteTweet(username: String, tweetId: Int): IO[Json] =
        sql"DELETE FROM tweets WHERE username = ${username} AND id = ${tweetId}"
          .update.run.transact(xa).map(r => json"""{"result": $r}""")

    override def follow(followerUsername: String, followingUsername: String): IO[Json] =
        sql"INSERT INTO following (follower, following) VALUES (${followerUsername}, ${followingUsername})"
          .update.run.transact(xa).map(r => json"""{"result": $r}""")

    override def unFollow(followerUsername: String, followingUsername: String): IO[Json] =
        sql"DELETE FROM following WHERE follower = ${followerUsername} AND following = ${followingUsername}"
          .update.run.transact(xa).map(r => json"""{"result": $r}""")

    override def fetchUser(username: String): IO[Json] =
        sql"SELECT username, email, firstName, lastName, birthday from users WHERE username = $username"
          .query[User].unique.transact(xa).map(_.asJson)

    override def fetchTweet(id: Int): IO[Json] =
        sql"SELECT * from tweets WHERE id = $id"
          .query[Tweet].unique.transact(xa).map(_.asJson)

    // TODO: maybe return streams instead?
    override def fetchTweets(username: String): IO[Json] =
        sql"SELECT * from tweets WHERE username = ${username} ORDER BY date DESC"
          .query[Tweet].to[Seq].transact(xa).map(_.asJson)

    override def fetchFeed(username: String): IO[Json] =
        sql"SELECT * from tweets WHERE username IN (SELECT followee FROM following WHERE follower = ${username}) ORDER BY date DESC"
          .query[Tweet].to[Seq].transact(xa).map(_.asJson)

    override def fetchFollowing(username: String): IO[Json] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.followee AND following.follower = ${username}"
          .query[User].to[Seq].transact(xa).map(_.asJson)

    override def fetchFollowers(username: String): IO[Json] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.follower AND following.followee = ${username}"
          .query[User].to[Seq].transact(xa).map(_.asJson)
}
