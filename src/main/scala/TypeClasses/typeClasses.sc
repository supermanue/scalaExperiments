/*
 type class:  programming technique that lets you add new behavior to closed data types without using inheritance,
 and without having access to the original source code of those types
 */


/*

Type classes consist of three components:

-  The type class, which is defined as a trait that takes at least one generic parameter (a generic “type”)
- Instances of the type class for types you want to extend
- Interface methods you expose to users of your new API
 */


// existing data types
sealed trait Animal
final case class Dog(name: String) extends Animal
final case class Cat(name: String) extends Animal
final case class Bird(name: String) extends Animal


// type class
trait BehavesLikeHuman[A] {
  def speak(a: A): Unit
}

// instances.
//aquí implemento la función que quiero para un tipo concreto.
// Defino el funcionamiento con un Dog como entrada
object BehavesLikeHumanInstances {
  // only for `Dog`
  //I tag the instance as implicit so it can be easily pulled into the code that I’ll write in the next steps.
  implicit val dogBehavesLikeHuman = new BehavesLikeHuman[Dog] {
    def speak(dog: Dog): Unit = {
      println(s"I'm a Dog, my name is ${dog.name}")
    }
  }

}

// Interface methods you expose to users of your new API
//esto es una implicit class normal: añadir una funcionalidad a una clase existente.
// la gracia está en que la funcionalidad que añado es "ejecutar lo que he definido en la Instancia anterior"

object BehavesLikeHumanSyntax {
  implicit class BehavesLikeHumanOps[A](value: A) {
    def speak(implicit behavesLikeHumanInstance: BehavesLikeHuman[A]): Unit = {
      behavesLikeHumanInstance.speak(value)
    }
  }
}

import BehavesLikeHumanInstances.dogBehavesLikeHuman
import BehavesLikeHumanSyntax.BehavesLikeHumanOps

val rover = Dog("Rover")
rover.speak
