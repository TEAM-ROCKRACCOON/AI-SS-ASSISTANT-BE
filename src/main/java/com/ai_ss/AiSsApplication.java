package com.ai_ss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AiSsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiSsApplication.class, args);
	}

}
