// Scala Futures vs IO
// based on https://www.beyondthelines.net/programming/futures-vs-tasks/

//Serial code
object SerialProg {
  def taskA(): Unit = {
    debug("Starting taskA")
    Thread.sleep(1000) // wait 1secs
    debug("Finished taskA")
  }

  def taskB(): Unit = {
    debug("Starting taskB")
    Thread.sleep(2000) // wait 2secs
    debug("Finished taskB")
  }

  def debug(message: String): Unit = {
    val now = java.time.format.DateTimeFormatter.ISO_INSTANT
      .format(java.time.Instant.now)
      .substring(11, 23) // keep only time component
    val thread = Thread.currentThread.getName()
    println(s"$now [$thread] $message")
  }

  def main(args: Array[String]): Unit = {
    debug("Starting Main")
    taskA()
    taskB()
    debug("Finished Main")
  }


}

//this one executes taskA, then taskB, all in the same Thread
val serialProg = SerialProg
serialProg.main(Array(""))



import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

//this is what executes the code in parallel
//An ExecutionContext can execute program logic asynchronously, typically but not necessarily on a thread pool.
import scala.concurrent.ExecutionContext.Implicits.global



//Parallel code with futures
object FutureProg {
  def taskA(): Future[Unit] = Future {
    debug("Starting taskA")
    Thread.sleep(1000) // wait 1secs
    debug("Finished taskA")
  }

  def taskB(): Future[Unit] = Future {
    debug("Starting taskB")
    Thread.sleep(2000) // wait 2secs
    debug("Finished taskB")
  }

  def debug(message: String): Unit = {
    val now = java.time.format.DateTimeFormatter.ISO_INSTANT
      .format(java.time.Instant.now)
      .substring(11, 23) // keep only time component
    val thread = Thread.currentThread.getName()
    println(s"$now [$thread] $message")
  }

  def main(args: Array[String]): Unit = {
    debug("Starting Main")
    val futureA = taskA()
    val futureB = taskB()
    debug("Continuing Main")
    // wait for both future to complete before exiting
    Await.result(futureA zip futureB, Duration.Inf)
  }
}

//this one creates a thread for taskOne, other for task2, and then executes in parallel
//then, main task waits until both have finished
val futureProg = FutureProg
futureProg.main(Array(""))


//MEMOIZATION
//characteristic of Futures is that they remember their results.
// They run their computation only once and remember the result
// of the computation so that if they’re call multiple time they
// can hand-out the results directly.
val total = Future.successful {
  println("Computing 2 + 2")
  2 + 2
}
println(total)
println(total) //this only returns "4", it does not print

