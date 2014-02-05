package rxbehaviors

import rx.lang.scala.Observable
import rx.lang.scala.Observer
import rx.lang.scala.Subscription

object FSM {
  def apply(initialState: State): FSM =
    new FSM(initialState, Map.empty)
}

class FSM(initialState: State, transitions: Map[State, List[Transition]]) {
  var currentState = initialState
  var transitionSubscriptions: List[Subscription] = List.empty

  def withTransition(trigger: Observable[Unit], source: State, target: State) : FSM = {
    val emptyList: List[Transition] = List.empty
    val current = transitions.get(source).getOrElse(emptyList)
    new FSM(initialState, transitions + (source -> (Transition(trigger, target) :: current)))
  }

  def activate = {
    currentState.activate
    subscribeToTransitions(currentState)
    this
  }

  def deactivate = {
    currentState.deactivate
    unsubscribeToTransitions
    this
  }

  private def switchState(destination: State) {
    currentState.deactivate
    unsubscribeToTransitions
    currentState = destination
    subscribeToTransitions(currentState)
    currentState.activate
  }

  private def subscribeToTransitions(state: State) {
    val transition = 
      for (t <- transitions.get(state) getOrElse List.empty) 
        yield t.trigger.subscribe( Unit => switchState(t.target) )

    transitionSubscriptions = transition.toList 
  }

  private def unsubscribeToTransitions =
    transitionSubscriptions.foreach(_.unsubscribe)
}
