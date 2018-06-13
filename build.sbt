name := "Twitter"

version := "0.1"

scalaVersion := "2.12.6"

val catsVersion    = "1.0.1"
val http4sVersion  = "0.18.8"
val circeVersion   = "0.9.3"
val doobieVersion  = "0.5.3"
val specs2Version  = "4.0.3"
val logbackVersion = "1.2.3"

libraryDependencies ++= Seq(
    "org.typelevel"   %% "cats-core"           % catsVersion,
    "org.typelevel"   %% "cats-free"           % catsVersion,
    "org.http4s"      %% "http4s-blaze-server" % http4sVersion,
    "org.http4s"      %% "http4s-circe"        % http4sVersion,
    "org.http4s"      %% "http4s-dsl"          % http4sVersion,
    "io.circe"        %% "circe-generic"       % circeVersion,
    "io.circe"        %% "circe-literal"       % circeVersion,
    "org.tpolecat"    %% "doobie-core"         % doobieVersion,
    "org.tpolecat"    %% "doobie-postgres"     % doobieVersion,
    "org.tpolecat"    %% "doobie-specs2"       % doobieVersion,
    "org.specs2"      %% "specs2-core"         % specs2Version % "test",
    "ch.qos.logback"  %  "logback-classic"     % logbackVersion
)

scalacOptions ++= Seq(
    "-Ypartial-unification"
)