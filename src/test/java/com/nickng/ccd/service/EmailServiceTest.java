package com.nickng.ccd.service;

import com.nickng.ccd.model.LinkModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;

class EmailServiceTest {

    @Spy
    private JavaMailSender mockEmailSender;

    private EmailService emailServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        emailServiceUnderTest = new EmailService(mockEmailSender);
    }

    @Test
    void testSendSimpleMessage() {
        // Setup
        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Run the test
        emailServiceUnderTest.sendSimpleMessage("subject", "text");

        // Verify the results
        Mockito.verify(mockEmailSender).send(argumentCaptor.capture());
        assertEquals("subject", argumentCaptor.getValue().getSubject(), "Mail subject not as injected");
        assertEquals("text", argumentCaptor.getValue().getText(), "Mail content not as injected");
    }

    @Test
    void testSendAttachedMessage() throws Exception {
        // Setup
        final List<File> files = Arrays.asList(
                new File("filename_1.txt"),
                new File("filename_2.txt"));
        ArgumentCaptor<MimeMessage> messageArgumentCaptor= ArgumentCaptor.forClass(MimeMessage.class);
//        Mockito.when(mockEmailSender.createMimeMessage()).thenReturn(new MimeMessage(files));

        // Run the test
        emailServiceUnderTest.sendAttachedMessage("text", files);

        // Verify the results
        // Verify mail should attach files
        Mockito.verify(mockEmailSender).send(messageArgumentCaptor.capture());
        MimeMessage message = messageArgumentCaptor.getValue();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        assertEquals("multipart", message.getContentType(), "Email should attach files");

        Multipart multiPart = (Multipart) message.getContent();
        assertEquals(2, multiPart.getCount());
        assertEquals("filename_1.txt", multiPart.getBodyPart(0).getFileName(), "File name should not be changed when send mail");
        assertEquals("filename_2.txt", multiPart.getBodyPart(1).getFileName(), "File name should not be changed when send mail");
    }

    @Test
    void testSendAttachedMessage_ThrowsMessagingException() {
        // Setup
        final List<File> files = Arrays.asList(new File("filename.txt"));

        // Run the test
        assertThrows(MessagingException.class, () -> {
            emailServiceUnderTest.sendAttachedMessage("text", files);
        });
    }

    @Test
    void testSendNotifyMail() {
        // Setup
        final LinkModel link = new LinkModel("title", "link");

        // Run the test
        emailServiceUnderTest.sendNotifyMail(link, "emailContent");

        // Verify the results
    }

    @Test
    void testSendNotifyMail1() {
        // Setup

        // Run the test
        emailServiceUnderTest.sendNotifyMail("emailContent");

        // Verify the results
    }
}
