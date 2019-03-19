package com.keithdavidson.surfstitchdemo.db;

import com.keithdavidson.surfstitchdemo.db.Product;
import com.keithdavidson.surfstitchdemo.db.ProductRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    public void testRepositoryShouldSaveAndFind(){
        this.productRepository.save(new Product(1, "CBR600", "600CC street bike"));
        Product product = productRepository.findById(1L).get();
        Assert.assertThat(product.getName(), Matchers.equalTo("CBR600"));
    }

    @Test
    public void testRepositoryShouldSaveAndFindAll(){
        this.productRepository.save(new Product(1, "CBR600", "600CC street bike"));
        this.productRepository.save(new Product(2, "XR250", "250CC enduro bike"));
        List<Product> products = productRepository.findAll();
        Assert.assertEquals(2, products.size());
    }

    @Test
    public void testShouldExistsByID(){
        this.productRepository.save(new Product(2, "XR250", "250CC enduro bike"));
        boolean exists = productRepository.existsById(2L);
        Assert.assertTrue(exists);
    }

}
