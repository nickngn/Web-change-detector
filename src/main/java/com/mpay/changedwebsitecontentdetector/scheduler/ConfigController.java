package com.mpay.changedwebsitecontentdetector.scheduler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpay.changedwebsitecontentdetector.exception.ParseJsonException;
import com.mpay.changedwebsitecontentdetector.service.ConfigService;
import com.mpay.changedwebsitecontentdetector.service.EmailService;
import com.mpay.changedwebsitecontentdetector.utils.IOUtils;

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
	public String checkSendingMail() throws MessagingException, IOException{
		String link = IOUtils.getFilePath("html", "website_content", "ca-nhan");
		Path path = FileSystems.getDefault().getPath(link);
		sender.sendAttachedMessage("Gui cho Hung dep trai", "THis is the content of mail sent to hung dep trai", path.toFile());
		return "Ô kê";
	}
}
