package com.scopely.spock

import spock.lang.Specification

class InteractionSpec extends Specification {

    Publisher publisher = new Publisher()
    def subscriber = Mock(Subscriber)
    Subscriber subscriber2 = Mock() // Here, the mock’s type is inferred from the variable type on the left-hand side of the assignment.

    def "should send messages to all subscribers" () {
        when:
            publisher.send("hello")
        then:
            1 * subscriber.receive("hello")
            1 * subscriber2.receive("hello")
    }

    def "cardinality" () {
        when:
            publisher.send("hello")
        then:
            // try each one by removing the comment (//) to the left
            1 * subscriber.receive("hello")      // exactly one call
//            0 * subscriber.receive("hello")      // zero calls
//            (1..3) * subscriber.receive("hello") // between one and three calls (inclusive)
//            (1.._) * subscriber.receive("hello") // at least one call
//            (_..3) * subscriber.receive("hello") // at most three calls
//            _ * subscriber.receive("hello")      // any number of calls, including zero
//                                                 // (rarely needed; see 'Strict Mocking')
    }

    def "target constraint" () {
        when:
            publisher.send("hello")
        then:
            1 * subscriber.receive("hello") // a call to 'subscriber'
            1 * _.receive("hello")          // a call to any mock object
    }

    def "method constraint" () {
        given:
            Publisher publisher = new Publisher()
            publisher.subscribers << subscriber
        when:
            publisher.send("hello")
        then:
            // try each one by removing the comment (//) to the left
            1 * subscriber.receive("hello") // a method named 'receive'
//            1 * subscriber./r.*e/("hello")  // a method whose name matches the given regular expression
//                                            // (here: method name starts with 'r' and ends in 'e')
        and:
            1 * subscriber.status // same as: 1 * subscriber.getStatus()
            1 * subscriber.setStatus("ok") // NOT: 1 * subscriber.status = "ok"
    }

    def "argument constraints" () {
        given:
            Publisher publisher = new Publisher()
            publisher.subscribers << subscriber
        when:
            publisher.send("hello")
        then:
            // try each one by removing the comment (//) to the left
            1 * subscriber.receive("hello")     // an argument that is equal to the String "hello"
//            1 * subscriber.receive(!"hello")    // an argument that is unequal to the String "hello"
//            1 * subscriber.receive()            // the empty argument list (would never match in our example)
//            1 * subscriber.receive(_)           // any single argument (including null)
//            1 * subscriber.receive(*_)          // any argument list (including the empty argument list)
//            1 * subscriber.receive(!null)       // any non-null argument
//            1 * subscriber.receive(_ as String) // any non-null argument that is-a String
//            1 * subscriber.receive({ it.size() > 3 }) // an argument that satisfies the given predicate
//                                                      // (here: message length is greater than 3)
    }

    def "" () {

    }

    def setup() {
        publisher.subscribers << subscriber // << is a Groovy shorthand for List.add()
        publisher.subscribers << subscriber2
    }

}
