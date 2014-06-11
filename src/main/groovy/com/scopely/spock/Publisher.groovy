package com.scopely.spock

class Publisher {

    List<Subscriber> subscribers = []
    List<Auditing> auditingList = []
    int messageCount = 0

    void send(String message) {
        subscribers.each { Subscriber subscriber ->
            subscriber.receive(message)
            subscriber.setStatus("ok")
            println subscriber.getStatus()
            messageCount++;
        }
    }

    void send(String message1, String message2) {
        subscribers.each { Subscriber subscriber ->
            subscriber.receive(message1, message2)
            subscriber.setStatus("ok")
            println subscriber.getStatus()
            messageCount++
        }
    }

    void send(String message1, String message2, String message3) {
        subscribers.each { Subscriber subscriber ->
            subscriber.receive(message1)
            subscriber.receive(message2)
            subscriber.receive(message3)
            messageCount++
        }
    }

    void publish(String message) {
        send(message)
        auditingList.each { Auditing auditing ->
            auditing.audit()
        }
    }

}
