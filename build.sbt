name := """http-server"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "commons-io"        % "commons-io"      % "2.5",
  "junit"             % "junit"           % "4.12"  % "test",
  "com.novocode"      % "junit-interface" % "0.11"  % "test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
