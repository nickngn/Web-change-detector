package com.mpay.changedwebsitecontentdetector;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpay.changedwebsitecontentdetector.service.ConfigService;
import com.mpay.changedwebsitecontentdetector.service.ContentService;

@SpringBootApplication
@EnableScheduling
public class ChangedWebsiteContentDetectorApplication {
	
	public static void main(String[] args) throws IOException {
		ContentService.initDirectories();
		SpringApplication.run(ChangedWebsiteContentDetectorApplication.class, args);
	}
	
	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    mailSender.setUsername(ConfigService.getConfig().getSender());
	    mailSender.setPassword(ConfigService.getConfig().getSenderPassword());
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    mailSender.setJavaMailProperties(props);
	     
	    return mailSender;
	}
	
}
