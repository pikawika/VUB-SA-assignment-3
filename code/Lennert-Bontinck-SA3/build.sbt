name := "Lennert-Bontinck-SA3"

// Scala version from WPO
version := "1.0"
scalaVersion := "2.12.7"
lazy val akkaVersion = "2.5.18"

// Needed libraries, same versions from WPO
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
)
