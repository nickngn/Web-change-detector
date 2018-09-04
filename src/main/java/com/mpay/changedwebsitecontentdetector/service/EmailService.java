package com.mpay.changedwebsitecontentdetector.service;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mpay.changedwebsitecontentdetector.object.LinkObject;

@Service
public class EmailService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private final String SUBJECT_TEMPLATE = "Phat hien thay doi noi dung website ";

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
    	helper.addAttachment(file.getName(), fileSystemResource);
        emailSender.send(message);
    }
    
    public void sendNotifyMail(LinkObject link, String emailContent) {
    	sendSimpleMessage(SUBJECT_TEMPLATE + link.getTitle(), emailContent);
    }
    
    public void sendNotifyMail(String emailContent) {
    	sendSimpleMessage(SUBJECT_TEMPLATE, emailContent);
    }

	public void sendNotifyMail(LinkObject link, Path differenceFile, String difference) throws MessagingException {
		String content = makeContent(link, difference);
		sendAttachedMessage(SUBJECT_TEMPLATE + link.getTitle(), content, differenceFile.toFile());
		logger.info("Sent notification mail");
	}
	
	private String makeContent(LinkObject linkObject, String difference) {
		return "Phát hiện ra sự thay đổi của website: " + linkObject.getTitle() + "\n"
		+ "Có đường dẫn là: " + linkObject.getLink() + "\n"
		+ "Vào lúc: " + new Date().toString();
	}
	
}
