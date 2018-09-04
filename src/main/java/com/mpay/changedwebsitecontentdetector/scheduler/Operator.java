package com.mpay.changedwebsitecontentdetector.scheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

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
	
	private boolean isChanged = false;

	@Scheduled(fixedDelayString="${time}")
	public void doCheckContentAndNotify() {
		String change = "";
		isChanged = false;
		ConfigObject config = ConfigService.getConfig();
		for (LinkObject link : config.getLinks()) {
			try {
				change = requestThenCompareAndSendEmail(link);
			} catch (URLException e) {
				logger.error("Url khong dung dinh dang: {}", link, e);
				isChanged = true;
				change += "Url \n" + link.getLink() + " \nkhong dung dinh dang.";
			} catch (RestClientException e) {
				logger.error("Khong ket noi duoc den url: {}", link.getLink());
				isChanged = true;
				change += "Url \n" + link.getLink() + " \nkhong hoat dong.";
			} catch (IOException e) {
				logger.error("Khong doc/ghi duoc file cua url: {}", link.getLink(), e);
				isChanged = true;
				change += "Khong ghi duoc noi dung website cua Url: \n" + link.getLink();
			} 
		}
		
		if(isChanged) {
			emailSender.sendNotifyMail(change);
		}
	}
	
	private String requestThenCompareAndSendEmail(LinkObject link) 
			throws RestClientException, URLException, IOException {
		String change;
		String newContent = requestSender.requestToLink(link);
		String storedContent = contentService.getLastSavedContentOfTitle(link.getTitle());
		if (newContent == null) newContent = "";
		if (storedContent.equals("FILE_NEVER_STORED_BEFORE")) {
			contentService.storeFileAsLatest(link.getTitle(), newContent);
			change = "\nBat dau quan sat: " + link.getTitle() 
				+ "\nDuong dan: " + link.getLink()
				+ "\nVao luc: " + new Date().toString();
			logger.info(change);
		}
		 else {
			String diff = contentService.getDifferent(storedContent, newContent);
			if (diff != "NO_DIFFERENCE") {
				Path path = contentService.storeFileAsDifference(link.getTitle(), diff);
				contentService.storeFileAsOldVersion(link.getTitle());
				contentService.storeFileAsLatest(link.getTitle(), newContent);
				change = "\nPhat hien su thay doi cua website: "+ link.getTitle() 
					+ "\nDuong dan: " + link.getLink() 
					+ "\nVao luc: " + new Date().toString()
					+ "\nChi tiet thay doi xem tai: differences/" + path.getFileName();
				isChanged = true;
				logger.error(change);
			} else {
				change = "\nKhong phat hien thay doi cua website: "+ link.getTitle() 
						+ "\nDuong dan: " + link.getLink() 
						+ "\nVao luc: " + new Date().toString();
				logger.info(change);
			}
		}
		return change;
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
