package com.mpay.ccd.scheduler;

import com.mpay.ccd.exception.FileNotExistException;
import com.mpay.ccd.exception.URLException;
import com.mpay.ccd.model.ConfigModel;
import com.mpay.ccd.model.LinkModel;
import com.mpay.ccd.service.ClientService;
import com.mpay.ccd.service.ComparisionService;
import com.mpay.ccd.service.ConfigService;
import com.mpay.ccd.service.EmailService;
import com.mpay.ccd.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class Scheduler.
 */
@Service
public class Scheduler{
	
	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/** The request sender. */
	@Autowired
	private ClientService requestSender;
	
	/** The io. */
	@Autowired
	private IOUtils io;
	
	/** The email sender. */
	@Autowired
	private EmailService emailSender;
	
	/** The comparision service. */
	@Autowired
	private ComparisionService comparisionService;
	
	/**
	 * Monitor then do actions.
	 */
	@Scheduled(fixedRateString="${time}")
	public void monitorThenDoActions() {
		StringBuilder changes = new StringBuilder();
		boolean isChanged = false;
		List<File> files = new ArrayList<>();
		String newContent = "";
		
		ConfigModel config = ConfigService.getConfig();
		for (LinkModel link : config.getLinks()) {
			String change = "";
			try {
				newContent = requestSender.callRequest(link);
				String storedContent = io.getLatestFileContent(link.getTitle());
				
				String diffContent = comparisionService.getDifference(storedContent, newContent);
				if (diffContent.isEmpty()) {
					logger.info("No change: {}", link);
				} else {
					isChanged = true;
					Path path = io.storeFileAsDifference(link.getTitle(), diffContent);
					io.storeFileAsOldVersion(link.getTitle());
					io.storeFileAsLatest(link.getTitle(), newContent);
					change = "\nPhat hien su thay doi cua website: "+ link.getTitle() 
						+ "\nDuong dan: " + link.getLink() 
						+ "\nVao luc: " + new Date().toString()
						+ "\nChi tiet thay doi xem tai: differences/" + path.getFileName() + "\n";
					files.add(path.toFile());
					logger.info(change);
				}
			} catch (URLException e) {
				isChanged = true;
				logger.error("Url khong dung dinh dang: {}", link, e);
				change = "\nUrl \n" + link.getLink() + " \nkhong dung dinh dang.";
			} catch (FileNotExistException e) {
			  change = startMonitoring(link, newContent);
			} catch (IOException e) {
				isChanged = true;
				logger.error("\nKhong doc/ghi duoc file cua url: {}", link.getLink(), e);
				change = e.getMessage();
			}
			changes.append(change);
		}
		
		if(isChanged) {
			try {
				emailSender.sendAttachedMessage(changes.toString(), files);
			} catch (MessagingException e) {
				emailSender.sendNotifyMail(changes.toString());
				logger.error("Khong gui duoc mail co dinh kem file", e);
			}
		}
	}
	
	private String startMonitoring(LinkModel link, String content) {
	  io.storeFileAsLatest(link.getTitle(), content);
    String change = "Bat dau quan sat: " + link.getTitle() 
      + "    Duong dan: " + link.getLink()
      + "    Vao luc: " + new Date().toString();
    logger.info(change);
    return change;
	}
	
	/**
	 * Send email daily at 8:30 every day.
	 */
	@Scheduled(cron="0 30 8 * * *")
	public void sendEmailDaily() {
		emailSender.sendSimpleMessage("(CCD) Báo cáo hoạt động của Phần mềm kiểm tra nội dung website",
				"CCD đang hoạt động bình thường.");
	}

}
