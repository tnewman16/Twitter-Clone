package com.casestudy.twitter.interpreters

import java.util.Date

import cats.effect.IO
import com.casestudy.twitter.Algebra
import com.casestudy.twitter.model._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

object DoobieInterpreter extends Algebra[IO] {
    val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql:twitter",
        "postgres",
        ""
    )

    override def tweet(username: String, date: Date, content: String): IO[Int] =
        sql"INSERT INTO tweets (user, date, content) VALUES (${username}, ${new Date()}, $content)"
          .update.run.transact(xa)

    override def deleteTweet(username: String, tweetId: Int): IO[Int] =
        sql"DELETE FROM tweets WHERE username = ${username} AND id = ${tweetId}"
          .update.run.transact(xa)

    override def follow(followerUsername: String, followingUsername: String): IO[Int] =
        sql"INSERT INTO following (follower, following) VALUES (${followerUsername}, ${followingUsername})"
          .update.run.transact(xa)

    override def unFollow(followerUsername: String, followingUsername: String): IO[Int] =
        sql"DELETE FROM following WHERE follower = ${followerUsername} AND following = ${followingUsername}"
          .update.run.transact(xa)

    override def fetchUser(username: String): IO[User] =
        sql"SELECT username, email, firstName, lastName, birthday from users WHERE username = $username"
          .query[User].unique.transact(xa)

    override def fetchTweet(id: Int): IO[Tweet] =
        sql"SELECT * from tweets WHERE id = $id"
          .query[Tweet].unique.transact(xa)

    // TODO: maybe return streams instead?
    override def fetchTweets(username: String): IO[Seq[Tweet]] =
        sql"SELECT * from tweets WHERE username = ${username}"
          .query[Tweet].to[Seq].transact(xa)

    override def fetchFeed(username: String): IO[Seq[Tweet]] =
        sql"SELECT * from tweets WHERE username IN (SELECT followee FROM following WHERE follower = ${username})"
          .query[Tweet].to[Seq].transact(xa)

    override def fetchFollowing(username: String): IO[Seq[User]] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.followee AND following.follower = ${username}"
          .query[User].to[Seq].transact(xa)

    override def fetchFollowers(username: String): IO[Seq[User]] =
        sql"SELECT username, email, firstName, lastName, birthday from users INNER JOIN following ON users.username = following.follower AND following.followee = ${username}"
          .query[User].to[Seq].transact(xa)
}
