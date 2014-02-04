This library implements a finite state machine built on top of RxScala. The goal is to provide a way to implement Rx applications that needs to switch behavior over time. A behavior in this library is typically an observable + an action (or observer) that should be "activated" when the behavior is activated.

Currently the library should be considered a draft or a basis for discussion more than anything else. It lacks many things both to be a complete FSM and to be a good Rx citizen. Hopefully it implements enough to get the idea accross though.

I would love to get some feedback! You can find me on twitter [@tobiasfuruholm](http://twitter.com/tobiasfuruholm)

## Example usage (simple FSM with two states)

    import rxbehavior
    import scala.concurrent.duration._
    import scala.language.postfixOps
    import rx.lang.scala.Observable
    import rx.lang.scala.Subject

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


