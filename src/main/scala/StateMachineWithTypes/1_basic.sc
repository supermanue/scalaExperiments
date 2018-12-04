//State machines with data types help on reducing the number of errors
//https://speakerdeck.com/iravid/boring-usecases-for-exciting-types

case class InitialState()
case class State2()
case class FinalState()

case class State[S](name: String)

//we put the F here so we can use for comprehension to sequence operations
trait StateMachine[F[_]] {
  def initialize(name:String): F[State[InitialState]]
  def firstStep(state: State[InitialState]): F[State[State2]]
  def end(state:State[State2]): F[State[FinalState]]
}

import cats.effect.IO
//so we can use the trait in the code
val api: StateMachine[IO]
import api._

val done: IO[State[FinalState]] = for {
  firstState <- initialize("my state machine")
  secondState <- firstStep(firstState)
  endState <- end(secondState)
} yield endState


/*
ESTO DA ERROR EN TIEMPO DE COMPILACION
val done2: IO[State[FinalState]] = for {
  firstState <- initialize("my state machine")
  endState <- end(firstState)
} yield endState
*/

/*
y esto tb
val done: IO[State[FinalState]] = for {
  firstState <- initialize("my state machine")
  secondState <- firstStep(firstState)
} yield secondState
 */