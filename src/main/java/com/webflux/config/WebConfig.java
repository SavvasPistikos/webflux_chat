package com.webflux.config;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.handler.UserHandler;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

	@Autowired
	UserHandler userHandler;


	@Bean
	public RouterFunction<ServerResponse> routerFunctionA() {
		return route()
				.GET("/test", request -> userHandler.getNothing(request))
				.build();
	}
}
