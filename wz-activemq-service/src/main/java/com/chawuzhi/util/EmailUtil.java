package com.chawuzhi.util;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender mailSenderAutowired;

    private static JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String usernameValue;

    private static String username;

    @PostConstruct//bean初始化之前调用
    public void init() {
        this.mailSender = mailSenderAutowired;
        this.username = usernameValue;
    }


    public static void sendEmail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        System.out.println(mailSender+"----------------------");
        mailSender.send(message);
    }

    public static void sendEmailWithFile(String to, String subject, String text, InputStreamSource file) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        // 这里的文件名已经写死了,可以通过截取获取
        helper.addAttachment("附件-1.png", file);
        mailSender.send(mimeMessage);
    }
}