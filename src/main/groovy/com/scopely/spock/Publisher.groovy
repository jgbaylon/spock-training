package com.scopely.spock

class Publisher {

    List<Subscriber> subscribers = []

    void send(String message) {
        subscribers.each { Subscriber subscriber ->
            subscriber.receive(message)
            subscriber.setStatus("ok")
            println subscriber.getStatus()
        }
    }

}
