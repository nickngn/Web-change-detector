package com.nickng.ccd.service;

import com.nickng.ccd.model.LinkModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * The Class EmailService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  /** The email sender. */
  public final JavaMailSender emailSender;
  
  /** The Constant SUBJECT_TEMPLATE. */
  private static final String SUBJECT_TEMPLATE = "Phat hien thay doi noi dung website ";

  /**
   * Send simple message.
   *
   * @param subject
   *          the subject
   * @param text
   *          the text
   */
  public void sendSimpleMessage(String subject, String text) {
    log.info("Gui email: Subject={}, Content={}", subject, text);
    String[] receivers = ConfigService.getConfig().getReceivers().toArray(new String[] {});
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(receivers);
    message.setSubject(subject);
    message.setText(text);
    emailSender.send(message);
  }

  /**
   * Send attached message.
   *
   * @param text
   *          the text
   * @param files
   *          the files
   * @throws MessagingException
   *           the messaging exception
   */
  public void sendAttachedMessage(String text, List<File> files) throws MessagingException {
    log.info("Gui email: Subject={}, Content={}", SUBJECT_TEMPLATE, text);
    MimeMessage message = emailSender.createMimeMessage();
    String[] receivers = ConfigService.getConfig().getReceivers().toArray(new String[] {});
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(receivers);
    helper.setSubject(SUBJECT_TEMPLATE);
    helper.setText(text);
    for (File file : files) {
      FileSystemResource fileSystemResource = new FileSystemResource(file);
      helper.addAttachment(file.getName(), fileSystemResource);
    }
    emailSender.send(message);
  }

  /**
   * Send notify mail.
   *
   * @param link
   *          the link
   * @param emailContent
   *          the email content
   */
  public void sendNotifyMail(LinkModel link, String emailContent) {
    sendSimpleMessage(SUBJECT_TEMPLATE + link.getTitle(), emailContent);
  }

  /**
   * Send notify mail.
   *
   * @param emailContent
   *          the email content
   */
  public void sendNotifyMail(String emailContent) {
    sendSimpleMessage(SUBJECT_TEMPLATE, emailContent);
  }

}
