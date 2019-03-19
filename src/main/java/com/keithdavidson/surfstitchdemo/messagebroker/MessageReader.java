package com.keithdavidson.surfstitchdemo.messagebroker;

import com.keithdavidson.surfstitchdemo.db.Product;
import com.keithdavidson.surfstitchdemo.db.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Reads JSON formatted messages off the message queue produced by the IMessageBroker,
 * and writes them to the Product table of the DB providing there is no conflict in
 * ProductID. Any messages with a ProductID that already exists has it's details logged
 * instead.
 */
public class MessageReader implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MessageReader.class);

    private static final ReentrantLock lock = new ReentrantLock();

    private final BlockingQueue<String> messageQueue;
    private final ProductRepository productRepository;
    private final MessageTranslator messageTranslator;


    public MessageReader(IMessageBroker broker, ProductRepository productRepository) {
        messageQueue = broker.getQueue();
        this.productRepository = productRepository;
        messageTranslator = new MessageTranslator();
    }

    public void run() {
        try {
            do {
                Product product = readMessage();
                saveIfNoIDConflict(product);
            } while (true);
        } catch (InterruptedException e) {
            log.error("Unexpected InterruptException: " + e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    private Product readMessage() throws InterruptedException {
        String message = messageQueue.take();
        log.debug("message read: " + message);
        return messageTranslator.getProduct(message);
    }

    /**
     * Writes the product to the DB if the ProductID doesn't already exist. If the ProductID
     * already exists, then writes the details to the log.
     *
     * Uses a Lock to turn the read/write operation into an effectively atomic operation, preventing
     * primary key conflicts from multiple threads simultaneously writing to the table. This
     * works because this is the only part of the entire code that writes to the Product table
     * in the DB. There is probably a better way to do this, probably via a stored function at
     * the DB level, or via some Hibernate functionalily, but the lack of time to complete this
     * project has led to this solution.
     * @param product to be saved to DB
     */
    private void saveIfNoIDConflict(Product product) {
        if(product == null){
            return; // message failed to parse. Was probably in the wrong format.
        }
        try {
            lock.lock();
            if (productRepository.existsById(product.getId())) {
                log.warn("Conflicting ID, product not saved to DB: " + product);
                return;
            }
            productRepository.saveAndFlush(product);
        }finally {
            lock.unlock();
        }
    }
}
