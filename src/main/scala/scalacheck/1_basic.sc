//https://github.com/rickynils/scalacheck/blob/master/doc/UserGuide.md
//libería que genera datos para los tests

//libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

//esto está rojo pero funciona (¿?)
import org.scalacheck.Prop.forAll


//con forAll defines una propiedad que tienen que pasar todos
val propConcatLists = forAll { (l1: List[Int], l2: List[Int]) =>
  l1.size + l2.size == (l1 ::: l2).size }

propConcatLists.check


//generator: genera entradas para una case class
import org.scalacheck.Gen._
import org.scalacheck._
import org.scalacheck.Arbitrary.arbitrary

//prueba con una iso
import monocle.Iso
case class Person(name: String, age: Int)
val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)) { case (name, age) => Person(name, age) }

val genPerson = for {
  name <- arbitrary[String]
  age <- arbitrary[Int]
} yield Person(name, age)

//genera uno para ver qué tal
genPerson.sample

// y ejecuta con todos
val correctIso = forAll(genPerson){ person =>
  person == personToTuple.reverseGet(personToTuple.get(person))
}

correctIso.check

//el uso de generadores sirve para evitar situaciones como esta
val propShortList = forAll { (l1: List[Int], l2: List[Int]) =>
  l1.size < 100 }
propShortList.check


//se pueden agrupar
object StringSpecification extends Properties("String") {

  property("startsWith") = forAll { (a: String, b: String) =>
    (a+b).startsWith(a)
  }

  property("endsWith") = forAll { (a: String, b: String) =>
    (a+b).endsWith(b)
  }

  property("substring") = forAll { (a: String, b: String) =>
    (a+b).substring(a.length) == b
  }

  property("substring") = forAll { (a: String, b: String, c: String) =>
    (a+b+c).substring(a.length, a.length+b.length) == b
  }
}

//esto hace falta hacerlo aquí porque es un script, de normal el main este no hace falta
StringSpecification.main(Array[String]())

