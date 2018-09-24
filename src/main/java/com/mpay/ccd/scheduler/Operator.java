package com.mpay.ccd.scheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.mpay.ccd.exception.URLException;
import com.mpay.ccd.object.ConfigObject;
import com.mpay.ccd.object.LinkObject;
import com.mpay.ccd.service.ConfigService;
import com.mpay.ccd.service.ContentService;
import com.mpay.ccd.service.EmailService;
import com.mpay.ccd.service.RequestService;

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
				String newContent = requestSender.requestToLink(link);
				change += compare(link, newContent);
			} catch (URLException e) {
				isChanged = true;
				logger.error("Url khong dung dinh dang: {}", link, e);
				change += "Url \n" + link.getLink() + " \nkhong dung dinh dang.";
			} catch (RestClientException e) {
				isChanged = true;
				logger.error("Khong ket noi duoc den url: {}", link.getLink());
				change += "Url \n" + link.getLink() + " \nkhong hoat dong.";
			} catch (IOException e) {
				isChanged = true;
				logger.error("Khong doc/ghi duoc file cua url: {}", link.getLink(), e);
				change += "Khong ghi duoc noi dung website cua Url: \n" + link.getLink();
			} 
		}
		
		if(isChanged) {
			emailSender.sendNotifyMail(change);
		}
	}
	
	private String compare(LinkObject link, String newContent) 
			throws RestClientException, IOException {
		String change = "";
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
			if (diff.equals("NO_DIFFERENCE")) {
				logger.info("\nKhong phat hien thay doi cua website: "+ link.getTitle() 
					+ "\nDuong dan: " + link.getLink() 
					+ "\nVao luc: " + new Date().toString());
			} else {
				Path path = contentService.storeFileAsDifference(link.getTitle(), diff);
				contentService.storeFileAsOldVersion(link.getTitle());
				contentService.storeFileAsLatest(link.getTitle(), newContent);
				change = "\nPhat hien su thay doi cua website: "+ link.getTitle() 
					+ "\nDuong dan: " + link.getLink() 
					+ "\nVao luc: " + new Date().toString()
					+ "\nChi tiet thay doi xem tai: differences/" + path.getFileName();
				isChanged = true;
				logger.error(change);
				
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
