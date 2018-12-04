//TODO EN DESARROLLO
// en build.sbt: libraryDependencies += "org.typelevel" %% "cats-effect" % "1.0.0"

import cats.effect.IO

val oneStr: String = "one"

//pure: meter una variable de cualquier tipo en un IO
val oneIO = IO.pure(oneStr)

//apply (constructor): meter una función en un IO.
// OJO: la función no se ejecuta inmediatamente: es la gracia!
val purifiedPrint = IO(println(oneStr))


//unsafeRunSync: ejecuta la función de manera síncrona.
purifiedPrint.unsafeRunSync()


//composición de funciones
val purifiedPrint2 = IO(println("two"))

val composed = for {
  a <- purifiedPrint
  b <- purifiedPrint2
} yield ()

//aquí se ejecutan de manera atómica
composed.unsafeRunSync()