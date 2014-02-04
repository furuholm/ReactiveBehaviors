package rxbehavior

import rx.lang.scala.Observable

case class Transition(trigger: Observable[Unit], target: State)
