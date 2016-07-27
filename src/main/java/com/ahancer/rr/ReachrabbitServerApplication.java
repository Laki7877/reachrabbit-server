package com.ahancer.rr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class ReachrabbitServerApplication {
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(ReachrabbitServerApplication.class, args);
	}
}
