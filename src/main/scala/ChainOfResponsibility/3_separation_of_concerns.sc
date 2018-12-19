case class Event(level: Int, title: String)

//Base handler class
trait Handler[E, T] {

  //quizá todos estos val no deberían existir y ser parámetros
  // de la función handleEvent, pero eso da un montón de problemas por otro lado
  val successor: Option[Handler[E, T]]
  val finalState: T
  val condition: E=> Boolean
  val handle: E => T

  final def handleEvent(event: E): T =
    if (condition (event))
      handle(event)
    else
      successor.fold(finalState)(_.handleEvent(event))
}

case class GenericHandler(
  successor: Option[Handler[Event, String]],
  finalState: String,
  condition: Event=>Boolean,
  handle: Event=> String) extends Handler[Event, String]


//TODO lo que me preocupa ahora es cómo inicializar esto
//aqui se mezcla lógica de negocio con inicialización, es muy muy chungo

object chainOfResponsibility {
  val level2 = GenericHandler(None, "Level 2: could not handle",
    event => event.level ==2,
    event =>"Level2Handler handling: " + event.title)

  val level1 = GenericHandler(Some(level2),
    "level1: could not handle",
    event => event.level ==1,
    event =>"Level1Handler handling: " + event.title)

  def handleEvent(event: Event): String = level1.handleEvent(event)

}

val event1 = Event(1, "event 1")
val event2 = Event(2, "event 2")
val event3 = Event(3, "event 3")


println(chainOfResponsibility.handleEvent(event1))
println(chainOfResponsibility.handleEvent(event2))
println(chainOfResponsibility.handleEvent(event3))