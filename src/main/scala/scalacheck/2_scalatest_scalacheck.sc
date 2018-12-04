//libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"

//Scalatest incluye una librería como scalacheck, "Checkers"


import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._


//esto se metería en una clase normal de test
class MySuite extends JUnitSuite with Checkers {
  //@Test
  def testConcat() {
    check((a: List[Int], b: List[Int]) => a.size + b.size == (a ::: b).size)
  }
}

//esto es para el script
Checkers.check((a: List[Int], b: List[Int]) => a.size + b.size == (a ::: b).size)

//prueba con una iso
import monocle.Iso
case class Person(name: String, age: Int)
val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)) { case (name, age) => Person(name, age) }

Checkers.check{(a: String, b: Int) =>
  val person = Person(a,b)
  person == personToTuple.reverseGet(personToTuple.get(person))
}


//también se pueden usar los métodos del scalacheck a pelo
//copiados del 1_basic
val genPerson = for {
  name <- arbitrary[String]
  age <- arbitrary[Int]
} yield Person(name, age)

val correctIso = forAll(genPerson){ person =>
  person == personToTuple.reverseGet(personToTuple.get(person))
}

correctIso.check