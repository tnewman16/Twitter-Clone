package com.casestudy.twitter

import com.casestudy.twitter.model._
import io.circe.{Encoder, Json}
import io.circe.literal._
import io.circe.syntax._

object implicits {
    lazy implicit val userEncoder: Encoder[User] = (u: User) =>
        json"""{
              "username": ${u.username},
              "email": ${u.email},
              "firstName": ${u.firstName},
              "lastName": ${u.lastName},
              "birthday": ${u.birthday.toString}
              }"""

    lazy implicit val tweetEncoder: Encoder[Tweet] = (t: Tweet) =>
        json"""{
              "id": ${t.id},
              "user": ${t.user},
              "date": ${t.date.toString},
              "content": ${t.content}
              }"""

    lazy implicit val usersEncoder: Encoder[Seq[User]] = (s: Seq[User]) =>
        Json.fromValues(s.map(_.asJson))

    lazy implicit val tweetsEncoder: Encoder[Seq[Tweet]] = (s: Seq[Tweet]) =>
        Json.fromValues(s.map(_.asJson))
}
