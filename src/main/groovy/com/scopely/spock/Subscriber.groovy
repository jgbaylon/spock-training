package com.scopely.spock

public interface Subscriber {

    void receive(String... message)

    String getStatus()

    void setStatus(String status)

}