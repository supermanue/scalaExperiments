/*
A Lens is an optic used to zoom inside a Product
Lenses have two type parameters generally called S and A: Lens[S, A]
where S represents the Product and A an element inside of S.

Básicamente son getters/setters funcionales y cómodos de usar
 */

import monocle.Lens


case class Address(strNumber: Int, streetName: String)

val strNumber = Lens[Address, Int](_.strNumber)(n => a => a.copy(strNumber = n))
//DESMENUZAMOS ESTO
//apply[S, A](get: S => A)(set: A => S => S)
//aquí S es Address, A es un entero
//NOTA: lo que devuelve el SET es una copia!!

//como hacerlo así es un poco coñazo, hay macros
import monocle.macros.GenLens
val strNumberWithMacro = GenLens[Address](_.strNumber)


val address = Address(10, "High Street")

//get
val number = strNumber.get(address)

//set: un nuevo elemento donde la dirección se ha actualizado
val newAddress = strNumber.set(5)(address)

//modify: get + set
val newAddress2 = strNumber.modify(_+1)(address)

//modifyF: es como el traverse. Le pasas una función de A=>F[A] y te devuelve un F[S]
//aqui pasamos una función que de un entero da una lista de entero, y nos devuelve una lista de direcciones
import scalaz.std.list._ // to get Functor[List] instance
val newMaybeAddress = strNumber.modifyF(n =>  List(n - 1, n + 1))(address)


