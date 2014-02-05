name := "ReactiveBehavior"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-deprecation", "-feature")

unmanagedSourceDirectories in Compile += baseDirectory.value / "examples"

libraryDependencies ++= Seq(
  "com.netflix.rxjava" % "rxjava-scala" % "0.16.0"
)
