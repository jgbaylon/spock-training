package com.scopely.spock

import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class DataDrivenSpec extends Specification {

    @Shared sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")

    def "let's try this!" () {
        expect:
            Math.max(1, 2) == 3
    }

    def "maximum of two numbers" () {
        expect:
            // exercise math method for a few different inputs
            Math.max(1, 3) == 3
            Math.max(7, 4) == 7
            Math.max(0, 0) == 0
    }

    @Unroll
    def "maximum of two numbers - max(#a, #b) == #c" () {
        expect:
            Math.max(a, b) == c
        where:
            a | b | c
            1 | 3 | 3
            7 | 4 | 4
            0 | 0 | 0
    }

    @Unroll
    def "maximum of two numbers - max(#a, #b) == #c - double pipe" () {
        expect:
            Math.max(a, b) == c
        where:
            a | b || c
            1 | 3 || 3
            7 | 4 || 4
            0 | 0 || 0
    }

    @Unroll
    def "maximum of two numbers - max(#a, #b) == #c - as data pipes" () {
        expect:
            Math.max(a, b) == c
        where:
            a << [1, 7, 0]
            b << [3, 4, 0]
            c << [3, 4, 0]
    }

    def "single column" () {
        expect:
            a == a
        where:
            a | _
            1 | _
            7 | _
            0 | _
    }

    def "single column - as data pipe" () {
        expect:
            a == a
        where:
            a << [1, 7, 0]
    }

    def "maximum of two numbers - multi variable data pipes" () {
        expect:
            Math.max(a, b) == c
        where:
            [a, b, _, c] << sql.rows("select a, b, c from maxdata")
    }

    @Unroll
    def "maximum of two numbers - max(#a, #b) == #c - data variable assignment" () {
        expect:
            Math.max(a, b) == c
        where:
            a = 3
            b = Math.random() * 100
            c = a > b ? a : b
    }

    def "maximum of two numbers - multi variable data pipes - data variable assignment" () {
        expect:
            Math.max(a, b) == c
        where:
            row << sql.rows("select * from maxdata")
            // pick apart columns
            a = row.a
            b = row.b
            c = row.c
    }

    @Unroll
    def "maximum of two numbers - max(#a, #b) == #c - combination" () {
        expect:
            Math.max(a, b) == c
        where:
            a | _
            3 | _
            7 | _
            0 | _

            b << [5, 0, 0]

            c = a > b ? a : b
    }

    def setupSpec() {
        sql.execute("create table maxdata (id int primary key, a int, b int, c int)")
        sql.execute("insert into maxdata values (1, 3, 7, 7), (2, 5, 4, 5), (3, 9, 9, 9)")
    }

}
