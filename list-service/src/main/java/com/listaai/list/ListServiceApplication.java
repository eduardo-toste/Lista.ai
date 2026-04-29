package com.listaai.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration.class)
@SpringBootApplication
public class ListServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListServiceApplication.class, args);
	}

}
