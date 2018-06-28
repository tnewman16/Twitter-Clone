package com.casestudy.twitter

import cats.effect.IO
import fs2.Stream

object types {
    type StreamIO[A] = Stream[IO, A]
}
