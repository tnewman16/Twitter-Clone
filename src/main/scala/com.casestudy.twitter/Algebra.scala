package com.casestudy.twitter

import java.util.Date

trait Algebra[F[_]] {
    def tweet(user: String, date: Date, text: String): F[_]
    def deleteTweet(user: String, tweetId: Int): F[_]
    def follow(followerUsername: String, followingUsername: String): F[_]
    def unFollow(followerUsername: String, followingUsername: String): F[_]
    def fetchUser(username: String): F[_]
    def fetchTweet(id: Int): F[_]
    def fetchTweets(user: String): F[_]
    def fetchFeed(user: String): F[_]
    def fetchFollowing(user: String): F[_]
    def fetchFollowers(user: String): F[_]
}
