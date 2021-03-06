package com.ahancer.rr.services.impl;

import java.util.concurrent.Future;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.ahancer.rr.services.EmailService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Component
public class EmailServiceImpl implements EmailService {
	
	@Value("${email.mailgun.apiKey}")
    private String mailgunApiKey;

    @Value("${email.mailgun.host}")
    private String mailgunHost;
    
    @Value("${email.mailgun.from}")
    private String mailgunFrom;
    
    @Async
    public Future<Boolean> send(String to,String subject, String body) {
    	
    	Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", mailgunApiKey));

        WebResource webResource = client.resource("https://api.mailgun.net/v3/" + mailgunHost +  "/messages");

        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", mailgunFrom);
        formData.add("to", to);
        formData.add("subject", subject);
        formData.add("html", body);

        ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
        if(200 != clientResponse.getStatus()) {
        	String output = clientResponse.getEntity(String.class);
        	System.out.println(output);
        	return new AsyncResult<>(false);
        }

        return new AsyncResult<>(true);

    }

}
