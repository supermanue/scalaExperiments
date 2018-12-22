case class Event(level: Int, title: String)

//Base handler class
trait Handler[E,T] {
  def condition(event:E): Boolean
  def handle (event: E): T
}

trait GenericHandler[E,T] extends Handler[E,T] {
  val successor: Handler[E,T]
  def condition (event: E): Boolean
  def handleFun (event: E): T

  def handle(event: E): T =
    if (condition (event))
      handleFun(event)
    else
      successor.handle(event)
}

trait GenericStopHandler[E,T] extends Handler[E,T] {
  def condition(event:E): Boolean= true
  def handle (event:E): T
}

trait Handler1 extends GenericHandler[Event, String]{
  def condition (event: Event): Boolean = event.level == 1
  def handleFun (event: Event): String = "Level1Handler handling: " + event.title
}

trait Handler2 extends GenericHandler[Event, String]{
  def condition (event: Event): Boolean = event.level ==2
  def handleFun (event: Event): String = "Level2Handler handling: " + event.title
}

trait StopHandler extends GenericStopHandler[Event, String]{
  def handle(event: Event):  String = new String("This is error")
}


object chainOfResponsibility {
  object Handler1 extends Handler1{val successor = Handler2}
  object Handler2 extends Handler2{val successor = Stop}
  object Stop extends StopHandler

  def handle(event: Event): String = Handler1.handle(event)

}

val event1 = Event(1, "event 1")
val event2 = Event(2, "event 2")
val event3 = Event(3, "event 3")


println(chainOfResponsibility.handle(event1))
println(chainOfResponsibility.handle(event2))
println(chainOfResponsibility.handle(event3))