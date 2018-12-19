name := "scalaExperiments"

version := "0.1"

scalaVersion := "2.12.7"

//cats  IO
libraryDependencies += "org.typelevel" %% "cats-effect" % "1.0.0"

//monocle
val monocleVersion = "1.5.0" // 1.5.0-cats based on cats 1.0.x
libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % "test"
)

//scalaz IO
libraryDependencies += "org.scalaz" %% "scalaz-ioeffect" % "2.10.1"

//scalacheck
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

//scalatest (ejemplo en scalacheck)
libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"


//circe
val circeVersion = "0.10.0"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)


//refined
libraryDependencies ++= Seq(
  "eu.timepit" %% "refined"                 % "0.9.3",
  "eu.timepit" %% "refined-cats"            % "0.9.3", // optional
  "eu.timepit" %% "refined-eval"            % "0.9.3", // optional, JVM-only
  "eu.timepit" %% "refined-jsonpath"        % "0.9.3", // optional, JVM-only
  "eu.timepit" %% "refined-pureconfig"      % "0.9.3", // optional, JVM-only
  "eu.timepit" %% "refined-scalacheck"      % "0.9.3", // optional, scalacheck 1.14
  "eu.timepit" %% "refined-scalacheck_1.13" % "0.9.3", // optional, alternative to above with scalacheck 1.13
  "eu.timepit" %% "refined-scalaz"          % "0.9.3", // optional
  "eu.timepit" %% "refined-scodec"          % "0.9.3", // optional
  "eu.timepit" %% "refined-scopt"           % "0.9.3", // optional
  "eu.timepit" %% "refined-shapeless"       % "0.9.3"  // optional
)
