package com.ahancer.rr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan
public class ReachrabbitServerApplication {
	
	
//	@Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("*").allowedOrigins("*").allowedMethods("*");
//            }
//        };
//    }

	
//	@Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        //SecurityFilter securityFilter = new SecurityFilter();
//        registrationBean.setFilter(securityFilter);
//        registrationBean.setOrder(2);
//        return registrationBean;
//    }

//    @Bean
//    public FilterRegistrationBean contextFilterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        RequestContextFilter contextFilter = new RequestContextFilter();
//        registrationBean.setFilter(contextFilter);
//        registrationBean.addUrlPatterns("/api/*");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
	

	

	public static void main(String[] args) {
		SpringApplication.run(ReachrabbitServerApplication.class, args);
	}
}
