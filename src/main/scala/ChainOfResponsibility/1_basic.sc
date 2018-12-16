//Good definition
//https://refactoring.guru/design-patterns/chain-of-responsibility


case class Event(level: Int, title: String)

//Base handler class
abstract class Handler {
  val successor: Option[Handler]
  def handleEvent(event: Event): Unit
}


case class Level1Handler (successor: Option[Handler]) extends Handler {
  override def handleEvent(event: Event): Unit =
    if (event.level ==1)
      handle(event)
    else
      successor.fold(println("Level1Handler: This event cannot be handled."))(_.handleEvent(event))

  private def handle (event:Event): Unit = println("Level1Handler handling: " + event.title)
}


case class Level2Handler (successor: Option[Handler]) extends Handler {
  override def handleEvent(event: Event): Unit =
    if (event.level ==2)
      handle(event)
    else
      successor.fold(println("Level2Handler: This event cannot be handled."))(_.handleEvent(event))

  private def handle (event:Event): Unit = println("Level1Handler handling: " + event.title)
}


object chainOfResponsibility {
  val level2 = Level2Handler(None)
  val level1 = Level1Handler(Some(level2))

  def handleEvent(event: Event): Unit = level1.handleEvent(event)

}


val event1 = Event(1, "event 1")
val event2 = Event(2, "event 2")
val event3 = Event(3, "event 3")


chainOfResponsibility.handleEvent(event1)
chainOfResponsibility.handleEvent(event2)
chainOfResponsibility.handleEvent(event3)