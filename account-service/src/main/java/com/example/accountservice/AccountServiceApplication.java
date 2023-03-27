package com.example.accountservice;

import co.elastic.apm.attach.ElasticApmAttacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        ElasticApmAttacher.attach();
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}

@RestController
@Slf4j
class AccountController {

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable String id) {
        log.info("TESTING Account");
        // generate a random number from 100 to 3000
        int random = (int) (Math.random() * 2900 + 100);
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Account(id, "Account " + id);
    }
}

record Account(String id, String name) {}
