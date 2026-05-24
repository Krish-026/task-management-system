package com.taskmanager.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TaskSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskSystemApplication.class, args);
	}

}
