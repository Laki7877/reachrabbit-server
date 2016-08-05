package com.ahancer.rr;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class ReachrabbitServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReachrabbitServerApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("th", "TH", "Th"));
		return slr;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("messages/messages");  // name of the resource bundle 
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}
	
	@Bean
	public Module datatypeHibernateModule() {
	  return new Hibernate5Module()
			  .enable(Feature.FORCE_LAZY_LOADING);
	}
}
