package com.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import com.webflux.server.TCPServer;


@SpringBootApplication
public class Application {
	public Application(TCPServer tcpServer) {
		this.tcpServer = tcpServer;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private final TCPServer tcpServer;

	@Bean
	public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
		return new ApplicationListener<ApplicationReadyEvent>() {
			@Override
			public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
				tcpServer.start();
			}
		};
	}
}
