package com.mpay.changedwebsitecontentdetector.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
    public JavaMailSender emailSender;
 
    public void sendSimpleMessage(String subject, String text) {
    	SimpleMailMessage message = new SimpleMailMessage(); 
    	String[] receivers = ConfigService.getConfig().getReceivers().toArray(new String[] {});
		message.setTo(receivers); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
    
    public void sendAttachedMessage(String subject, String text, File file) throws MessagingException {
    	MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String[] receivers = ConfigService.getConfig().getReceivers().toArray(new String[] {});
        helper.setTo(receivers); 
    	helper.setSubject(subject); 
    	helper.setText(text);
    	FileSystemResource fileSystemResource = new FileSystemResource(file);
    	helper.addAttachment("Changed file", fileSystemResource);
        emailSender.send(message);
    }
	
}
