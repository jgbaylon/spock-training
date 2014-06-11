package com.scopely.spock

import spock.lang.Specification

class InteractionSpec extends Specification {

    Publisher publisher1 = new Publisher()
    def subscriber1 = Mock(Subscriber)
    Subscriber subscriber2 = Mock() // Here, the mock’s type is inferred from the variable type on the left-hand side of the assignment.
    Auditing auditing = Mock()

    def "should send messages to all subscribers" () {
        when:
            publisher1.send("hello")
        then:
            1 * subscriber1.receive("hello")
            1 * subscriber2.receive("hello")
    }

    def "cardinality" () {
        when:
            publisher1.send("hello")
        then:
            // try each one by removing the comment (//) to the left
            1 * subscriber1.receive("hello")      // exactly one call
//            0 * subscriber1.receive("hello")      // zero calls
//            (1..3) * subscriber1.receive("hello") // between one and three calls (inclusive)
//            (1.._) * subscriber1.receive("hello") // at least one call
//            (_..3) * subscriber1.receive("hello") // at most three calls
//            _ * subscriber1.receive("hello")      // any number of calls, including zero
//                                                 // (rarely needed; see 'Strict Mocking')
    }

    def "target constraint" () {
        when:
            publisher1.send("hello")
        then:
            1 * subscriber1.receive("hello") // a call to 'subscriber1'
            1 * _.receive("hello")          // a call to any mock object
    }

    def "method constraint" () {
        given:
            Publisher publisher = new Publisher()
            publisher.subscribers << subscriber1
        when:
            publisher.send("hello")
        then:
            // try each one by removing the comment (//) to the left
            1 * subscriber1.receive("hello") // a method named 'receive'
//            1 * subscriber1./r.*e/("hello")  // a method whose name matches the given regular expression
//                                            // (here: method name starts with 'r' and ends in 'e')
        and:
            1 * subscriber1.status // same as: 1 * subscriber1.getStatus()
            1 * subscriber1.setStatus("ok") // NOT: 1 * subscriber1.status = "ok"
    }

    def "argument constraints" () {
        given:
            Publisher publisher = new Publisher()
            publisher.subscribers << subscriber1
        when:
            publisher.send("hello")
        then:
            // try each one by removing the comment (//) to the left
            1 * subscriber1.receive("hello")     // an argument that is equal to the String "hello"
//            1 * subscriber1.receive(!"hello")    // an argument that is unequal to the String "hello"
//            1 * subscriber1.receive()            // the empty argument list (would never match in our example)
//            1 * subscriber1.receive(_)           // any single argument (including null)
//            1 * subscriber1.receive(*_)          // any argument list (including the empty argument list)
//            1 * subscriber1.receive(!null)       // any non-null argument
//            1 * subscriber1.receive(_ as String) // any non-null argument that is-a String
//            1 * subscriber1.receive({ it.size() > 3 }) // an argument that satisfies the given predicate
//                                                      // (here: message length is greater than 3)
//            1 * subscriber1._(*_)     // any method on subscriber1, with any argument list
//            1 * subscriber1._         // shortcut for and preferred over the above
//
//            1 * _._                  // any method call on any mock object
//            1 * _                    // shortcut for and preferred over the above
    }

    def "argument constraints - var args" () {
        given:
            Publisher publisher = new Publisher()
            publisher.subscribers << subscriber1
        when:
            publisher.send("hello", "goodbye")
        then:
            1 * subscriber1.receive("hello", "goodbye")     // arguments that is equal to the String "hello" and "goodbye"
    }

    def "strick mocking" () {
        given:
            Publisher publisher = new Publisher()
            publisher.subscribers << subscriber1
        when:
            publisher.publish("hello")
        then:
            1 * subscriber1.receive("hello") // demand one 'receive' call on `subscriber1`
            _ * auditing._                  // allow any interaction with 'auditing'
            0 * _                           // don't allow any other interaction
    }

    def "declaring interactions at mock creation" () {
        given:
            Publisher publisher = new Publisher()
            Subscriber subscriber = Mock {
                1 * receive("hello")
                1 * receive("goodbye")
                1 * receive("adieu")
            }
            publisher.subscribers << subscriber
        expect:
            publisher.send("hello", "goodbye", "adieu")
    }

    def "grouping interactions with same target" () {
        given:
            Publisher publisher = new Publisher()
            Subscriber subscriber = Mock()
            publisher.subscribers << subscriber
        when:
            publisher.send("hello", "goodbye", "adieu")
        then:
            with(subscriber) {
                1 * receive("hello")
                1 * receive("goodbye")
                1 * receive("adieu")
            }
    }

    def "mixing interactions and conditions" () {
        given:
            Publisher publisher = new Publisher()
            Subscriber subscriber = Mock()
            publisher.subscribers << subscriber
        when:
            publisher.send("hello")
        then:
            1 * subscriber.receive("hello")
            publisher.messageCount == 1
    }

    def "explicit interaction blocks" () {
        given:
            Publisher publisher = new Publisher()
            Subscriber subscriber = Mock()
            publisher.subscribers << subscriber
        when:
            publisher.send("hello")
        then:
            interaction {
                def message = "hello"
                1 * subscriber.receive(message)
            }
    }

    def "scope of interactions" () {
        given:
            Publisher publisher = new Publisher()
            Subscriber subscriber = Mock()
            publisher.subscribers << subscriber
        when:
            publisher.send("message1")
        then:
            subscriber.receive("message1")
        when:
            publisher.send("message2")
        then:
            subscriber.receive("message2")
    }

    def setup() {
        publisher1.subscribers << subscriber1 // << is a Groovy shorthand for List.add()
        publisher1.subscribers << subscriber2
        publisher1.auditingList << auditing
    }

}
