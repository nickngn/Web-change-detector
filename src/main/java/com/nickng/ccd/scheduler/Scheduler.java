package com.nickng.ccd.scheduler;

import com.nickng.ccd.exception.FileNotExistException;
import com.nickng.ccd.exception.URLException;
import com.nickng.ccd.model.ConfigModel;
import com.nickng.ccd.model.LinkModel;
import com.nickng.ccd.service.ClientService;
import com.nickng.ccd.service.ComparisionService;
import com.nickng.ccd.service.ConfigService;
import com.nickng.ccd.service.EmailService;
import com.nickng.ccd.utils.IOUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
@Slf4j
public class Scheduler{
	
	/** The request sender. */
	private final ClientService requestSender;
	
	/** The io. */
	private final IOUtils io;
	
	/** The email sender. */
	private final EmailService emailSender;
	
	/** The comparision service. */
	private final ComparisionService comparisionService;
	
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
				String storedContent = io.getLastestFileContent(link.getTitle());
				
				String diffContent = comparisionService.getDifference(storedContent, newContent);
				if (diffContent.isEmpty()) {
					log.info("No change: {}", link);
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
					log.info(change);
				}
			} catch (URLException e) {
				isChanged = true;
				log.error("Url khong dung dinh dang: {}", link, e);
				change = "\nUrl \n" + link.getLink() + " \nkhong dung dinh dang.";
			} catch (FileNotExistException e) {
			  change = startMonitoring(link, newContent);
			} catch (IOException e) {
				isChanged = true;
				log.error("\nKhong doc/ghi duoc file cua url: {}", link.getLink(), e);
				change = e.getMessage();
			}
			changes.append(change);
		}
		
		if(isChanged) {
			try {
				emailSender.sendAttachedMessage(changes.toString(), files);
			} catch (MessagingException e) {
				emailSender.sendNotifyMail(changes.toString());
				log.error("Khong gui duoc mail co dinh kem file", e);
			}
		}
	}
	
	private String startMonitoring(LinkModel link, String content) {
	  io.storeFileAsLatest(link.getTitle(), content);
    String change = "Bat dau quan sat: " + link.getTitle() 
      + "    Duong dan: " + link.getLink()
      + "    Vao luc: " + new Date().toString();
    log.info(change);
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
