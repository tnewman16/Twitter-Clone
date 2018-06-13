package com.casestudy.twitter

import com.casestudy.twitter.interpreters._

object Test extends App {
    import DoobieJsonInterpreter._

    val username = "tnewman"
    val data = for {
        user <- fetchUser(username)
        followers <- fetchFollowers(username)
        following <- fetchFollowing(username)
        tweets <- fetchTweets(username)
        feed <- fetchFeed(username)
    } yield {
        println(s"User:       $user")
        println(s"Following:  $following")
        println(s"Followers:  $followers")
        println(s"My Tweets:  $tweets")
        println(s"My Feed:    $feed")
    }
    data.unsafeRunSync
}
