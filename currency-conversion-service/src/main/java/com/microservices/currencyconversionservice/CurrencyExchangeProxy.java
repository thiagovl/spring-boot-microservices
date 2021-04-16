package com.microservices.currencyconversionservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="currency-exchange", url = "localhost:8000")
@FeignClient(name="currency-exchange") // Balanceamento de carga
public interface CurrencyExchangeProxy {

	/**
	 * 
	 * @param from
	 * @param to
	 * @return os valores são retornados de acordo com os campos
	 * do CurrencyConversion automaticamente.
	 * Está função é a mesma que está presente no controller em CurrencyExchangeController
	 * do microservice CurrencyExchange
	 * no @GetMapping("/currency-exchange/from/{from}/to/{to}")
	 */
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion retrieveExchangeValue(
			@PathVariable String from,
			@PathVariable String to);
}
