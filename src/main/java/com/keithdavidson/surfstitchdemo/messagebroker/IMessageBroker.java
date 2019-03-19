package com.keithdavidson.surfstitchdemo.messagebroker;

import java.util.concurrent.BlockingQueue;

/**
 * Interface to connect a Message Broker to a Message Consumer.
 */
public interface IMessageBroker {
    public BlockingQueue<String> getQueue();
}
