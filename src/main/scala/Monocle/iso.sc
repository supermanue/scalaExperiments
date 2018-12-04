//An Iso is an optic which converts elements of type S into elements of type A without loss.

/*
val monocleVersion = "1.5.0" // 1.5.0-cats based on cats 1.0.x

libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % "test"
)
 */

import monocle.Iso
import monocle.law.discipline.IsoTests


//vamos a convertir entre esto y una tupla
case class Person(name: String, age: Int)

val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)) { case (name, age) => Person(name, age) }
//DESMENUZAMOS ESTO
// apply[S, A] (get: S => A)(reverseGet: A => S)
//aquí S es una Person, A es una fupla

//get: aplicar la función que hemos definido arriba
val one = Person("manuel", 35)
val res = personToTuple.get(one)

//reverseGet: aplicar la contraria
val tuplePerson = ("Alia", 28)
val res2 = personToTuple.reverseGet(tuplePerson)


//TEST: ver que cumple las IsoLaws. Se prueba con IsoTests
