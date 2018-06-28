package com.casestudy.twitter

import java.util.Date

trait Algebra[F[_], A] {
    def tweet(user: String, date: Date, text: String): F[A]
    def deleteTweet(user: String, tweetId: Int): F[A]
    def follow(followerUsername: String, followingUsername: String): F[A]
    def unFollow(followerUsername: String, followingUsername: String): F[A]
    def fetchUser(username: String): F[A]
    def fetchTweet(id: Int): F[A]
    def fetchTweets(user: String): F[A]
    def fetchFeed(user: String): F[A]
    def fetchFollowing(user: String): F[A]
    def fetchFollowers(user: String): F[A]
}
