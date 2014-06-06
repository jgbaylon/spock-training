package com.scopely.spock

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class UnrollSpec extends Specification {

    @Shared Person person1
    @Shared Person person2

    // property access
    @Unroll
    def "#person is #person.age years old" () {
        expect:
            "Joseph Baylon" == person.name // same as person1.getName()
            31 == person.age               // same as person1.getAge()
        where:
            person << [person1]
    }

    // zero-arg method call
    @Unroll
    def "#person.name.toUpperCase()" () {
        expect:
            "Xin Chen" == person.name
            23 == person.age
        where:
            person << [person2]
    }

    // cannot have method arguments
    @Unroll
    def "#person.name.split(' ')[1]" () {
        expect:
            "Xin Chen" == person.name
            23 == person.age
        where:
            person << [person2]
    }

    // cannot use operators
    @Unroll
    def "#person.age / 2" () {
        expect:
            "Xin Chen" == person.name
            23 == person.age
        where:
            person << [person2]
    }

    // additional data variables can be introduced to hold more complex expression
    @Unroll
    def "#lastName" () {
        expect:
            "Joseph Baylon" == person.name
            31 == person.age
        where:
            person << [person1]
            lastName = person.name.split(' ')[1]
    }

    def setupSpec() {
        person1 = new Person()
        person1.setName("Joseph Baylon")
        person1.setAge(32)

        person2 = new Person()
        person2.setName("Xin Chen")
        person2.setAge(24)
    }

}
