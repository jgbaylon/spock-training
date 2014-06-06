package com.scopely.spock

class Person {

    String name // getName() and setName(..) are generated
    Integer age // getAge() and setAge(..) are generated

    @Override
    String toString() {
        return name
    }

}
