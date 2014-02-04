package rxbehavior

import rx.lang.scala.Observable

object State {
  def apply[T](name: String): State = {
    new State(name, List.empty)
  }
}

class State(name: String, behaviors : List[Behavior]) {

  def withBehavior[T](observable: Observable[T], action: T => Unit): State =
    new State(name, Behavior(observable, action) :: behaviors)

  def activate() =
    behaviors.foreach(_.activate)

  def deactivate() =
    behaviors.foreach(_.deactivate)  
}
