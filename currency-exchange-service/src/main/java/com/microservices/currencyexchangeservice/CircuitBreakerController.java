package com.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {

	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api")
	// Configuração do resilience. Em sample-api deve ser o mesmo nome declarado no application.properties resilience4j.retry.instances.sample-api.maxRetryAttempts=5
//	@Retry(name="sample-api", fallbackMethod = "hardcodeResponse") // fallbackMethod chamará a função abaixo que retorna uma message
	// Configuração do CircuitBreaker, caso não ocorra resposta ele retorna a função hardcodeResponse
//	@CircuitBreaker(name="default", fallbackMethod = "hardcodeResponse")
	// Limita o número de chamadas a cada 10s => 10000 chamadas
//	@RateLimiter(name = "default") 
	@Bulkhead(name = "default") 
	public String sampleApi() {
		logger.info("Chamada recebida Sample API");
//		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummp", String.class);
//		return forEntity.getBody();
		
		return "Sample API";
	}
	
	// Metodo que irá retornar uma resposta, fallback
	public String hardcodeResponse(Exception ex) {
		return "Fallback response";
	}
}
