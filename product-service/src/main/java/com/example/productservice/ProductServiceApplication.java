package com.example.productservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}

@RestController
@RequestMapping("/account/{id}/")
@Slf4j
class ProductController {

    @GetMapping("/product")
    public Collection<Product> getProduct(@PathVariable String id) {
        log.info("TESTING Product");
        // generate a random number from 100 to 3000
        int random = (int) (Math.random() * 2900 + 100);
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(
                new Product("123", "Product 1"),
                new Product("456", "Product 2"),
                new Product("789", "Product 3")
        );
    }
}

record Product(String sku, String name) {
}
