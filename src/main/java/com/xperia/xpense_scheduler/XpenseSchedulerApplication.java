package com.xperia.xpense_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.xperia.xpense_scheduler",
		"org.xperia.repository",
		"org.xperia.service",
		"org.xperia.client"
})
public class XpenseSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(XpenseSchedulerApplication.class, args);
	}

}
