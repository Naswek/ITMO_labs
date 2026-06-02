name := "Lab1"

version := "0.1.0"

scalaVersion := "3.3.0"

enablePlugins(AssemblyPlugin)

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor-typed_3" % "2.8.5",
  "com.typesafe.akka" % "akka-stream_3"      % "2.8.5",
  "com.typesafe.akka" % "akka-http_3"        % "10.5.3",
  "com.typesafe.akka" % "akka-http-spray-json_3" % "10.5.3",
  "org.scalatestplus" %% "scalacheck-1-17" % "3.2.18.0" % Test,
  "org.scalatest"     %% "scalatest"       % "3.2.18"   % Test,
  ("ch.megard" % "akka-http-cors_3" % "1.2.0")
    .excludeAll(
      ExclusionRule("com.typesafe.akka", "akka-http-core_2.13"),
      ExclusionRule("com.typesafe.akka", "akka-http_2.13"),
      ExclusionRule("com.typesafe.akka", "akka-parsing_2.13")
    )
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