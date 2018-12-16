//TODO: SEPARATION OF CONCERNS! CASE CLASSES ARE MIXING DATA AND LOGIC, THIS IS AN ERROR


//Base handler class
abstract class Handler[E, T] {
  val successor: Option[Handler[E, T]]
  val finalState: T
  def condition (event: E): Boolean
  def handle(e: E): T

  final def handleEvent(event: E): T =
    if (condition (event))
      handle(event)
    else
      successor.fold(finalState)(_.handleEvent(event))
}

case class Event(level: Int, title: String)

case class Level1Handler (successor: Option[Handler[Event, String]], finalState: String) extends Handler[Event, String] {
  def condition (event: Event) = event.level ==1
  def handle (event:Event): String = "Level1Handler handling: " + event.title
}

case class Level2Handler (successor: Option[Handler[Event, String]], finalState: String) extends Handler[Event, String] {
  def condition (event: Event) = event.level ==2
  def handle (event:Event): String = "Level1Handler handling: " + event.title
}


object chainOfResponsibility {
  val level2 = Level2Handler(None, "Level 2: could not handle")
  val level1 = Level1Handler(Some(level2), "level1: could not handle")

  def handleEvent(event: Event): String = level1.handleEvent(event)

}


val event1 = Event(1, "event 1")
val event2 = Event(2, "event 2")
val event3 = Event(3, "event 3")


println(chainOfResponsibility.handleEvent(event1))
println(chainOfResponsibility.handleEvent(event2))
println(chainOfResponsibility.handleEvent(event3))