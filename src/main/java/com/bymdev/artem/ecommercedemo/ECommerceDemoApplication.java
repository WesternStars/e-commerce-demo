package com.bymdev.artem.ecommercedemo;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceDemoApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ECommerceDemoApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.run(args);
	}

}
