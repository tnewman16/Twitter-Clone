package com.casestudy.twitter.interpreters

import java.util.Date

import cats.effect.IO
import com.casestudy.twitter.Algebra
import com.casestudy.twitter.model._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

object DoobieStringInterpreter extends Algebra[IO, String] {
    val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://[::1]/twitter",
        "postgres",
        ""
    )

    override def tweet(username: String, date: Date, content: String): IO[String] =
        sql"INSERT INTO tweets (user, date, content) VALUES (${username}, ${new Date()}, $content)"
          .update.run.transact(xa).map(_.toString)

    override def deleteTweet(username: String, tweetId: Int): IO[String] =
        sql"DELETE FROM tweets WHERE username = ${username} AND id = ${tweetId}"
          .update.run.transact(xa).map(_.toString)

    override def follow(followerUsername: String, followingUsername: String): IO[String] =
        sql"INSERT INTO following (follower, following) VALUES (${followerUsername}, ${followingUsername})"
          .update.run.transact(xa).map(_.toString)

    override def unFollow(followerUsername: String, followingUsername: String): IO[String] =
        sql"DELETE FROM following WHERE follower = ${followerUsername} AND following = ${followingUsername}"
          .update.run.transact(xa).map(_.toString)

    override def fetchUser(username: String): IO[String] =
        sql"SELECT username, email, firstName, lastName, birthday from users WHERE username = $username"
          .query[User].unique.transact(xa).map(_.toString)

    override def fetchTweet(id: Int): IO[String] =
        sql"SELECT * from tweets WHERE id = $id"
          .query[Tweet].unique.transact(xa).map(_.toString)

    // TODO: maybe return streams instead?
    override def fetchTweets(username: String): IO[String] =
        sql"SELECT * from tweets WHERE username = ${username}"
          .query[Tweet].to[Seq].transact(xa).map(_.toString)

    override def fetchFeed(username: String): IO[String] =
        sql"SELECT * from tweets WHERE username IN (SELECT followee FROM following WHERE follower = ${username})"
          .query[Tweet].to[Seq].transact(xa).map(_.toString)

    override def fetchFollowing(username: String): IO[String] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.followee AND following.follower = ${username}"
          .query[User].to[Seq].transact(xa).map(_.toString)

    override def fetchFollowers(username: String): IO[String] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.follower AND following.followee = ${username}"
          .query[User].to[Seq].transact(xa).map(_.toString)
}
