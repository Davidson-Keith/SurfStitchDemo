package com.keithdavidson.surfstitchdemo.db;

import com.keithdavidson.surfstitchdemo.db.Product;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class ProductTest {
    @Test
    public void testShouldConstruct(){
        Product product = new Product(1, "CBR600", "600CC street bike");
        Assert.assertEquals(1, product.getId());
        Assert.assertThat(product.getName(), Matchers.equalTo("CBR600"));
    }
}
