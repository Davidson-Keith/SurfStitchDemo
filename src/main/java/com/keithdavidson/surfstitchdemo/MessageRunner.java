package com.keithdavidson.surfstitchdemo;

import com.keithdavidson.surfstitchdemo.db.ProductRepository;
import com.keithdavidson.surfstitchdemo.messagebroker.MessageBrokerMock;
import com.keithdavidson.surfstitchdemo.messagebroker.MessageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A Spring CommandLineRunner which runs the application. Creates a thread to run the
 * MessageBrokerMock, which mocks sending messages to the Message Queue.
 * Creates a number of threads that read from the queue and write to the Product table
 * of the DB.
 */
@Component
public class MessageRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(MessageRunner.class);

    private final ProductRepository productRepository;

    public MessageRunner(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            MessageBrokerMock messageBrokerMock = new MessageBrokerMock();
            MessageReader reader1 = new MessageReader(messageBrokerMock, productRepository);
            MessageReader reader2 = new MessageReader(messageBrokerMock, productRepository);
            MessageReader reader3 = new MessageReader(messageBrokerMock, productRepository);
            executorService.execute(messageBrokerMock);
            executorService.execute(reader1);
            executorService.execute(reader2);
            executorService.execute(reader3);
            devCheckResults(); // remove or comment out this for production
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Just some testing output during dev. Shows everything in the DB after all our mock data has processed.
     */
    private void devCheckResults() {
        try {
            Thread.sleep(3000); // wait 5 seconds for the mock data to process.
        } catch (InterruptedException e) {
        }
        System.out.println("All products saved to DB:");
        productRepository.findAll().forEach(System.out::println);
    }
}
