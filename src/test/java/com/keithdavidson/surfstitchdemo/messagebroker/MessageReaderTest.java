package com.keithdavidson.surfstitchdemo.messagebroker;

import com.keithdavidson.surfstitchdemo.db.Product;
import com.keithdavidson.surfstitchdemo.db.ProductRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageReaderTest {
    @Autowired
    ProductRepository productRepository;

    IMessageBroker broker;
    IMessageBroker broker2;

    @Before
    public void setup(){
        broker = new IMessageBroker() {
            @Override
            public BlockingQueue<String> getQueue() {
                BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();
                try {
                    messageQueue.put(new MessageTranslator().getJsonMessage(new Product(1, "CBR600", "600CC street bike")));
                } catch (InterruptedException e) {
                }
                return messageQueue;
            }
        };
        broker2 = new IMessageBroker() {
            @Override
            public BlockingQueue<String> getQueue() {
                BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();
                try {
                    messageQueue.put(new MessageTranslator().getJsonMessage(new Product(1, "CBR600", "600CC street bike")));
                    messageQueue.put(new MessageTranslator().getJsonMessage(new Product(1, "CBR1000", "1000CC street bike")));
                } catch (InterruptedException e) {
                }
                return messageQueue;
            }
        };
    }

    @Test
    public void testShouldReadMessageAndWriteToDB(){
        MessageReader reader = new MessageReader(broker, productRepository);
        new Thread(reader).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        Product product = productRepository.findById(1L).get();
        Assert.assertThat(product.getName(), Matchers.equalTo("CBR600"));
    }

    @Test
    public void testShouldNotWriteMessageWithConfictingIDToDB(){
        MessageReader reader = new MessageReader(broker2, productRepository);
        new Thread(reader).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        long count = productRepository.count();
        Assert.assertEquals(1, count); // 2 messages read, 1 written

    }
}
