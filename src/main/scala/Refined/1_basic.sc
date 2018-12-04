import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.{MatchesRegex, Url, ValidInt}

val i1: Int Refined Positive = 5
val i2: Int Refined Positive = 5

val i3 = i2.value + i1.value

val address: String Refined Url = "http://www.google.com"

println(address.value)

type Size = String Refined ValidInt
val size: Size = "23"
val sizeInt = size.value.toInt

//does not compile
//val size2: String Refined ValidInt = "j"
//val size2Int = size.value.toInt

def readSize (size: Size): Int = size.value.toInt

val first = readSize("17")
//does not compile
//val second = readSize("j")

// let's pretend this is known at runtime
val sizeRuntime: String = "23"
val wrongSizeRuntime: String = "23.234"

val verifiedSize: Either[String, Size] =
  refineV[ValidInt](sizeRuntime)

val wrongVerifiedSize: Either[String, Size] =
  refineV[ValidInt](wrongSizeRuntime)

verifiedSize.fold(_=> -1, readSize(_))
wrongVerifiedSize.fold(_=> -1, readSize(_))
