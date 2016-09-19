package com.ahancer.rr;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ahancer.rr.filter.AuthorizationFilter;

@Configuration
@EnableWebMvc
public class HandlerInterceptorConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new AuthorizationFilter());
	    //registry.addInterceptor(new TransactionInterceptor()).addPathPatterns("/person/save/*");
	}
}
