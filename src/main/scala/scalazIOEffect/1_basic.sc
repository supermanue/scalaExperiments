//libraryDependencies += "org.scalaz" %% "scalaz-ioeffect" % "<version>"

//A value of type IO[E, A] describes an effect that may
// - fail with an E,
// - run forever,
// - or produce a single A.


import scalaz.ioeffect.IO

//meter un valor puro en un IO:: point
val liftedString: IO[Void, String] = IO.point("Hello World")

//meter un valor impuro sincrono que NO  va a cascar: sync
val nanoTime: IO[Void, Long] = IO.sync(System.nanoTime())

//meter un valor impuro asíncrono que puede lanzar excepción: syncException
def readInt(name: String): IO[Exception, Int] =
  IO.syncException(name.toInt)

//meter un fallo: fail
val failure: IO[String, Unit] = IO.fail("Oh noes!")

//paralelismo: par
val textAndTime = liftedString.par(nanoTime)

//TODO
//ejecutar ???
val exec = liftedString.attempt
val resultCode = exec.map(attemptResult =>
  attemptResult.fold(_=> -1, _ => 0)
)
