package rxbehaviors

import scala.concurrent.duration._
import scala.language.postfixOps
import rx.lang.scala.Observable
import rx.lang.scala.Subject

object SimpleFSM {

  def main(args: Array[String]) {
    // Create subject to be used as a transition trigger
    val switchStateCmd = Subject[Unit]

    // Create initial state
    val initial = State("Initial")
      .withBehavior(Observable.interval(1000 millis), {n: Long => println("initial state: " + n)})

    // Create another state
    val next = State("Next")
      .withBehavior(Observable.interval(1000 millis), {n: Long => println("next state: = " + n)})

    // Create the FSM
    val fsm = FSM(initial)
      .withTransition(switchStateCmd, initial, next)
      .withTransition(switchStateCmd, next, initial)

    // Activate the FSM
    fsm.activate
     
    while (true) {
      // Read from keyboard
      val c = Console.readChar
  
      c match {
        // s (followed by enter) to switch state
        case 's' => switchStateCmd.onNext()
        // q (followed by enter) to quit
        case 'q' => {
          fsm.deactivate
          System.exit(0)
        }
        case _ : Char => () // Ignore other keys
      }
    }
  }
  
}

