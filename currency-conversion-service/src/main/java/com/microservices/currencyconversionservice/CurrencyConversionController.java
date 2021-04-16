package com.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity
			) {
		
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		/** RestTemplate() - faz chamadas REST API
		 *  getForEntity() - faz uma solicitação e recebe de volta
		 */
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity
			/**
			 * Comunicação com outro microserviço
			 * "from" e "to" - são dados enviados pela URL
			 */
			("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
					/**
					 * Passa os dados enviados para o model CurrencyConversion
					 */
					CurrencyConversion.class, uriVariables);
		
		CurrencyConversion currencyConversion = responseEntity.getBody();
		
		return new CurrencyConversion(
				10001L, 
				from, 
				to, 
				quantity, 
				currencyConversion.getConversionMultiple(), 
				quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getEnvironment() + " REST Template"
		); 
	}
	
	/**
	 * Outra maneira de fazer o metodo acima utilizando o Feign
	 * com o Proxy
	 */
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity
			) {
		
		
		/**
		 * Utilizando o CurrencyExchangeProxy
		 */
		CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConversion(
				10001L, 
				from, 
				to, 
				quantity, 
				currencyConversion.getConversionMultiple(), 
				quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getEnvironment() + " FEIGN"
		); 
	}
}
