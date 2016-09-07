package com.ahancer.rr.services;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Service
public class EmailService {
	
	@Value("${email.mailgun.apiKey}")
    private String mailgunApiKey;

    @Value("${email.mailgun.host}")
    private String mailgunHost;
    
    @Value("${email.mailgun.from}")
    private String mailgunFrom;
    
    public boolean send(String to,String subject, String html) {
    	    	
    	Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", mailgunApiKey));

        WebResource webResource = client.resource("https://api.mailgun.net/v3/" + mailgunHost +  "/messages");

        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", mailgunFrom);
        formData.add("to", to);
        formData.add("subject", subject);
        formData.add("html", html);

        ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
        String output = clientResponse.getEntity(String.class);

        System.out.println(output);

        return true;

    }

}
