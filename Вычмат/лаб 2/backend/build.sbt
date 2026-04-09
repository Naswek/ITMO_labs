name := "Lab1"

version := "0.1.0"

scalaVersion := "2.13.12"

enablePlugins(AssemblyPlugin)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
  "com.typesafe.akka" %% "akka-stream"      % "2.8.5",
  "com.typesafe.akka" %% "akka-http"        % "10.5.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.3",
  "ch.megard" %% "akka-http-cors" % "1.2.0"
)

assembly / mainClass := Some("Main")

assembly / assemblyMergeStrategy := {
  case PathList(xs @ _*) if xs.last == "reference.conf" =>
    MergeStrategy.concat

  case PathList(xs @ _*) if xs.last == "application.conf" =>
    MergeStrategy.concat

  case PathList("META-INF", xs @ _*) =>
    MergeStrategy.discard

  case _ => MergeStrategy.first
}