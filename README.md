# Twitter-Clone
A Twitter backend written in Scala using the Tagless Final interpreter pattern.

## Getting Started

To get a copy of the project localy, simply clone this repository using the following command:
```sh
git clone https://github.com/tnewman16/Twitter-Clone.git
```

### Prerequisites

To run this project locally, you must the following technologies installed on your machine:

* [Scala](https://www.scala-lang.org/download/)
* [sbt](https://www.scala-sbt.org/1.0/docs/Setup.html)
* [PostgreSQL](https://www.postgresql.org/download/)

### Initializing the test data

First we will create a PostgreSQL database that will be used to store the test data. Steps for doing this can be found on [doobie's website](https://tpolecat.github.io/doobie/docs/01-Introduction.html).

In this case, we will create a user named `postgres` and a database named `twitter`, like so:
```sh
psql -c 'create user postgres createdb'
psql -c 'create database twitter;' -U postgres
```

In order to get the test data into the `twitter` database, we will use the [`init.sql`](/sql/init.sql) script found in the [`/sql`](/sql) folder:
```sh
psql -c '\i init.sql' -d twitter -U postgres
```

**NOTE:** If you wish to reset the database at any time, execute the [`delete.sql`](/sql/delete.sql) and [`init.sql`](/sql/init.sql) scripts in consecutive order:
```sh
psql -c '\i delete.sql' -d twitter -U postgres
psql -c '\i init.sql' -d twitter -U postgres
```

To ensure that everything is working correctly, simply run the [`Test.scala`](/src/main/scala/com.casestudy.twitter/Test.scala). This should output basic information from the database to the console.

### Running the server

To host the Twitter server locally, we have to run the [`TwitterServer.scala`](src/main/scala/com.casestudy.twitter/server/TwitterServer.scala) file. This will spin up the `http4s` server locally on port `8081`.

Once the server is up and running, open up a browser and try hitting one one of the valid endpoints, such as `http://localhost:8081/users/tnewman/feed`. You should then see a JSON object being returned as the response.

**NOTE:** A frontend written in [React](https://reactjs.org) has been created for this project, but has not yet been connected with the backend.

## Built With

* [Scala](https://www.scala-lang.org) - programming language
* [sbt](https://www.scala-sbt.org) - project build tools
* [PostgreSQL](https://www.postgresql.org) - SQL data storage
* [Cats](https://typelevel.org/cats/) - abstractions for functional programming
* [http4s](https://http4s.org) - HTTP server
* [Circe](https://circe.github.io/circe/) - JSON representation and parsing
* [doobie](https://tpolecat.github.io/doobie/) - JDBC layer for database access

## Authors

* **Tyler Newman**
  * [GitHub](https://github.com/tnewman16)
  * [LinkedIn](https://www.linkedin.com/in/tnewman16/)

See also the list of [contributors](https://github.com/tnewman16/Twitter-Clone/contributors) who participated in this project.
