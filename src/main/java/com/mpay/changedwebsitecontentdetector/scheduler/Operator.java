package com.mpay.changedwebsitecontentdetector.scheduler;

import java.io.IOException;
import java.nio.file.Path;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.mpay.changedwebsitecontentdetector.exception.URLException;
import com.mpay.changedwebsitecontentdetector.object.ConfigObject;
import com.mpay.changedwebsitecontentdetector.object.LinkObject;
import com.mpay.changedwebsitecontentdetector.service.ConfigService;
import com.mpay.changedwebsitecontentdetector.service.ContentService;
import com.mpay.changedwebsitecontentdetector.service.EmailService;
import com.mpay.changedwebsitecontentdetector.service.RequestService;

@Service
public class Operator {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RequestService requestSender;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private EmailService emailSender;
	

	@Scheduled(fixedDelayString="${time}")
	public void doCheckContentAndNotify() {
		ConfigObject config = ConfigService.getConfig();
		for (LinkObject link : config.getLinks()) {
			try {
				requestCompareAndSendEmail(link);
			} catch (URLException e) {
				logger.error("Url khong dung dinh dang: {}", link, e);
				String emailContent = "Url \n" + link.getLink() + " \nkhong dung dinh dang.";
				emailSender.sendNotifyMail(link, emailContent);
			} catch (RestClientException e) {
				logger.error("Khong ket noi duoc den url: {}", link.getLink());
				String emailContent = "Url \n" + link.getLink() + " \nkhong hoat dong.";
				emailSender.sendNotifyMail(link, emailContent);
			} catch (IOException e) {
				logger.error("Khong doc/ghi duoc file cua url: {}", link.getLink(), e);
				String emailContent = "Khong ghi duoc noi dung website cua Url: \n" + link.getLink();
				emailSender.sendNotifyMail(link, emailContent);
			} catch (MessagingException e) {
				logger.error("Khong gui duoc mail", link.getLink(), e);
			}
		}
	}
	
	private void requestCompareAndSendEmail(LinkObject link) 
			throws RestClientException, URLException, IOException, MessagingException {
		String newContent = requestSender.requestToLink(link);
		String storedContent = contentService.getLastSavedContentOfTitle(link.getTitle());
		if (newContent == null) newContent = "";
		if (storedContent.equals("FILE_NEVER_STORED_BEFORE")) 
			contentService.storeFileAsLatest(link.getTitle(), newContent);
		 else {
			String diff = contentService.getDifferent(storedContent, newContent);
			if (diff != "NO_DIFFERENCE") {
				Path path = contentService.storeFileAsDifference(link.getTitle(), diff);
				contentService.storeFileAsOldVersion(link.getTitle());
				contentService.storeFileAsLatest(link.getTitle(), newContent);
				emailSender.sendNotifyMail(link, path, diff);
			}
		}
	}
	
	/**
	 * Send email daily at 8:30 every day
	 */
	@Scheduled(cron="0 30 8 * * *")
	public void sendEmailDaily() {
		emailSender.sendSimpleMessage("(CCD) Báo cáo hoạt động của Phần mềm kiểm tra nội dung website",
				"CCD đang hoạt động bình thường.");
	}
	

}
