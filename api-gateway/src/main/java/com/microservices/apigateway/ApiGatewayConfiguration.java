package com.microservices.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
	
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		
		 // Página de destino
			
		return builder.routes()
				// Exemplo de como adicionar cabeçalho
				.route(p -> p.path("/get") // Obtendo uma URL
					.filters(f -> f
						.addRequestHeader("MyHeader", "MyURL") // Adicionando ao Header um valor
						.addRequestParameter("Param", "MyValue")) // Adicionando um Parametro
						.uri("http://httpbin.org:80")) // Será direcionado para está página
				
				// Rota para /currency-exchange
				.route(p -> p.path("/currency-exchange/**") // Recebendo /currency-exchange na URL http://localhost:8765/currency-exchange
						.uri("lb://currency-exchange")) // Será direcionado para o Eureka que irá direcionar para o service /currency-exchange
				
				// Rota para /currency-conversion
				.route(p -> p.path("/currency-conversion/**") // URL do Microservice /currency-conversion/**
						.uri("lb://currency-conversion")) // Será direcionado para o Eureka que direcionará para o service /currency-conversion
				
				// Rota para /currency-conversion-feign
				.route(p -> p.path("/currency-conversion-feign/**") // URL do Microservice /currency-conversion-feign/**
						.uri("lb://currency-conversion")) // Será direcionado para o Eureka que direcionará para o service /currency-conversion
				
				// Rota para /currency-conversion-new - REDIRECIONAMENTO
				.route(p -> p.path("/currency-conversion-new/**") 
						.filters(f -> f.rewritePath( // Redireciona a URL solicitada
								"/currency-conversion-new/(?<segment>.*)", // Recuperando os demais parametros e passando para segment
								"/currency-conversion-feign/${segment}")) // Adicionando o segment recuperado e adicionando
						.uri("lb://currency-conversion"))				
				
				.build();
	}

}
