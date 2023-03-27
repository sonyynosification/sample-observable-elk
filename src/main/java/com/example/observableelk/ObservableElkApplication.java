package com.example.observableelk;

import co.elastic.apm.attach.ElasticApmAttacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.ObservationTextPublisher;
import io.micrometer.observation.aop.ObservedAspect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@Slf4j
public class ObservableElkApplication {

	public static void main(String[] args) {
		ElasticApmAttacher.attach();
		SpringApplication.run(ObservableElkApplication.class, args);
	}


	@Bean
	public ObservationHandler<Observation.Context> observationTextPublisher() {
		return new ObservationTextPublisher(log::info);
	}

	@Bean
	public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
		return new ObservedAspect(observationRegistry);
	}

}

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
class AccountController {

	private final AccountService accountService;
	private final ProductService productService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/{id}")
	public AccountResponse getAccountProducts(@PathVariable String id) throws JsonProcessingException {
		// disable SerializationFeature.FAIL_ON_EMPTY_BEANS
//		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		final Account account = accountService.getAccount(id);
		final List<Product> products = productService.getProducts(id);


//		return objectMapper.writeValueAsString(new AccountResponse(account, products));
		return new AccountResponse(account, products);
	}
}

@Service
class AccountService {
	private final RestTemplate restTemplate = new RestTemplate();
	Account getAccount(String id) {
		return restTemplate.getForObject("http://localhost:8081/account/{id}", Account.class, id);
	}
}

@Service
class ProductService {
	private final RestTemplate restTemplate = new RestTemplate();
	List<Product> getProducts(String accountId) {
		return restTemplate.getForObject("http://localhost:8082/account/{accountId}/product", List.class, accountId);
	}
}

record Account(String id, String name) {}
//record Product(String sku, String name) {}
//record AccountResponse(Account account, List<Product> products) {}

@AllArgsConstructor
@Getter
@Setter
class Product {
	String sku;
	String name;
}
@AllArgsConstructor
@Getter
@Setter
class AccountResponse {
	Account account;
	List<Product> products;
}