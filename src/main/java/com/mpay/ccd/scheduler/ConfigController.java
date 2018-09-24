package com.mpay.ccd.scheduler;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpay.ccd.exception.ParseJsonException;
import com.mpay.ccd.service.ConfigService;
import com.mpay.ccd.service.EmailService;

@RestController
public class ConfigController {
	
	private Logger logger = LoggerFactory.getLogger(getClass()); 
	
	@Autowired
	private EmailService sender;
	
	@GetMapping("/update-config")
	public String updateConfig() {
		try {
			ConfigService.checkConfig();
			return "Ok";
		} catch (FileNotFoundException e) {
			logger.error("Khong tim thay file config", e);
		} catch (ParseJsonException e) {
			logger.error("File config khong dung dinh dang", e);
		}
		
		return "Error";
	}
	
	@GetMapping("/example-send-mail") 
	public String checkSendingMail() {
		sender.sendSimpleMessage("Gui cho Hung dep trai", "This is the content of mail sent to hung dep trai to test check sending mail");
		return "Ô kê";
	}
	
	@GetMapping("/check-sending-email") 
	public String checkSendingMail2() {
		sender.sendSimpleMessage("Gui cho Hung dep trai", "This is the content of mail sent to hung dep trai to test check sending mail");
		return "Sending email service is okay.";
	}
}
