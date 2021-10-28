package com.devsessions.servicebusrequestreply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.devsessions.config")
public class ServicebusRequestReplyApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServicebusRequestReplyApplication.class, args);
	}

}
