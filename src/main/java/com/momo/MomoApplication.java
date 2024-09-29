package com.momo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MomoApplication {  //모모 메인

	public static void main(String[] args) {
		SpringApplication.run(MomoApplication.class, args);
	}

}
