package com.scopely.spock

import spock.lang.Specification

class MyFirstSpec extends Specification {

    def "let's try this!" () {
        expect:
            Math.max(1, 2) == 3
    }

}
