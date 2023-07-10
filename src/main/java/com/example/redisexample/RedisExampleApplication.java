package com.example.redisexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RedisExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisExampleApplication.class, args);
	}

}
