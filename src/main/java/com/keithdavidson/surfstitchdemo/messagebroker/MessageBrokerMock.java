package com.keithdavidson.surfstitchdemo.messagebroker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.javafaker.Beer;
import com.github.javafaker.Faker;
import com.keithdavidson.surfstitchdemo.db.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mocks a Message Broker which produces messages containing JSON formatted Product's,
 * which are written to the Message Queue.
 */
public class MessageBrokerMock implements IMessageBroker, Runnable {
    private static final Logger log = LoggerFactory.getLogger(MessageBrokerMock.class);

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();
    MessageTranslator messageTranslator = new MessageTranslator();

    public MessageBrokerMock() {
    }

    public void run() {
        runConflictingIDs();
        runConsecutiveIDs();
    }

    /**
     * Mock the production of MESSAGE_COUNT number of messages, all with consecutive ProductID's
     */
    private void runConsecutiveIDs(){
        int MESSAGE_COUNT = 10; // number of messages to mock
        Faker faker = new Faker();
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            Beer beer = faker.beer();
            String message = messageTranslator.getJsonMessage(new Product(i, beer.name(), beer.style()));
            log.debug("message = " + message);
            try {
                messageQueue.put(message);
            } catch (InterruptedException e) {
                // Reset interrupt flag and exit thread gracefully.
                log.error("Unexpected interrupt: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Mock the production of two messages with identical conflicting ProductID's
     */
    private void runConflictingIDs() {
        String message1 = messageTranslator.getJsonMessage(new Product(1, "CBR600", "600CC street bike"));
        String message2 = messageTranslator.getJsonMessage(new Product(1, "XR250", "250CC dirt bike"));
        log.debug("message1 = " + message1);
        log.debug("message2 = " + message2);
        try {
            messageQueue.put(message1);
            messageQueue.put(message2);
        } catch (InterruptedException e) {
            log.error("Unexpected interrupt: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public BlockingQueue<String> getQueue() {
        return messageQueue;
    }
}
