import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

sealed trait Foo
case class Bar(xs: Vector[String]) extends Foo
case class Qux(i: Int, d: Option[Double]) extends Foo

val foo: Foo = Qux(13, Some(14.0))

val json = foo.asJson.noSpaces
println(json)

val decodedFoo = decode[Foo](json)
println(decodedFoo)


val foo2: Foo = Bar(Vector("first", "second"))

val json2 = foo2.asJson.noSpaces
println(json2)

val decodedFoo2 = decode[Foo](json2)
println(decodedFoo2)