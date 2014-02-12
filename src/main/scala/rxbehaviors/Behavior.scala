package rxbehaviors

import rx.lang.scala.Observable
import rx.lang.scala.Observer
import rx.lang.scala.Subscription

trait Behavior {
  def activate(): Unit
  def deactivate(): Unit
}

object Behavior {
  def apply[T](observable: Observable[T], action: T => Unit): Behavior = {
    new Impl(observable, action)
  }

  class Impl[T](observable: Observable[T], action: T => Unit) extends Behavior {
    val connectable = observable.publish
    val subscription = connectable.subscribe(action)
    var activeSubscription: Subscription = _

    def activate: Unit = {
      activeSubscription = connectable.connect
    }

    def deactivate: Unit = {
      activeSubscription.unsubscribe
    }
  }

}
