package com.foodon.foodon;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneId;
import java.util.TimeZone;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class FoodonApplication {

	@PostConstruct
	void postConstruct() { TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); }

	public static void main(String[] args) {
		SpringApplication.run(FoodonApplication.class, args);
	}

}
