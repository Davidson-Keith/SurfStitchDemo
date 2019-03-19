package com.keithdavidson.surfstitchdemo.messagebroker;

import com.keithdavidson.surfstitchdemo.db.Product;
import org.junit.Assert;
import org.junit.Test;

public class MessageTranslatorTest {
    @Test
    public void testShouldTranslateFromProductToJson(){
        Product product = new Product(1, "CBR600", "600CC street bike");
        String json = new MessageTranslator().getJsonMessage(product);
        Assert.assertEquals("{\"ProductID\":1,\"ProductName\":\"CBR600\",\"ProductDescription\":\"600CC street bike\"}", json);
    }

    @Test
    public void testShouldTranslateFromJsonToProduct(){
        String json = "{\"ProductID\":1,\"ProductName\":\"CBR600\",\"ProductDescription\":\"600CC street bike\"}";
        Product product = new MessageTranslator().getProduct(json);
        Assert.assertEquals(product.getName(), "CBR600");
    }

    @Test
    public void testShouldReturnNullWithIncorrectJson(){
        String json = "{\"id\":1,\"name\":\"CBR600\",\"desc\":\"600CC street bike\"}";
        Product product = new MessageTranslator().getProduct(json);
        Assert.assertNull(product);
    }
}
