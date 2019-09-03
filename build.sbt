name := "play-example"

version := "1.0.0"

scalaVersion := "2.12.3"
offline:= true

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
  
libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.194"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.46"
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.0.2"
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
//libraryDependencies += "org.quartz-scheduler" % "quartz" % "2.2.1" 
libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.5.13"
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
