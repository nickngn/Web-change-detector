package com.mpay.changedwebsitecontentdetector.scheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
	
	private final String SUBJECT_TEMPLATE = "Phat hien thay doi noi dung website ";

	@Scheduled(fixedDelayString="${time}")
	public void doCheckContentAndNotify() {
		ConfigObject config = ConfigService.getConfig();
		for (LinkObject link : config.getLinks()) {
			try {
				String newContent = requestSender.requestToLink(link);
				String storedContent = contentService.getLastSavedContentOfTitle(link.getTitle());
				if (storedContent.equals("FILE_NEVER_STORED_BEFORE") && (newContent != null)) {
					contentService.storeFileAsLatest(link.getTitle(), newContent);
				} else if (newContent != null){
					String diff = contentService.getDifferent(storedContent, newContent);
					if (diff != "NO_DIFFERENCE") {
						Path path = contentService.storeFileAsDifference(link.getTitle(), diff);
						contentService.storeFileAsOldVersion(link.getTitle());
						contentService.storeFileAsLatest(link.getTitle(), newContent);
						sendNotifyMail(link, path, diff);
					}
				}
			} catch (URLException e) {
				logger.error("Sai link {}", link, e);
			} catch (IOException e) {
				logger.error("Khong doc/ghi duoc file", e);
			} catch (MessagingException e) {
				logger.error("Khong gui duoc mail", e);
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
	
	private void sendNotifyMail(LinkObject link, Path differenceFile, String difference) throws MessagingException {
		String content = makeContent(link, difference);
		emailSender.sendAttachedMessage(SUBJECT_TEMPLATE + link.getTitle(), content, differenceFile.toFile());
		logger.info("Sent notification mail");
	}
	
	private String makeContent(LinkObject linkObject, String difference) {
		return "500 ae chúng tôi đã phát hiện ra sự thay đổi của website: " + linkObject.getTitle() + "\n"
		+ "Có đường dẫn là: " + linkObject.getLink() + "\n"
		+ "Vào lúc: " + new Date().toString();
	}

}
