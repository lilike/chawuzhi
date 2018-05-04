package com.chawuzhi.activemq;

import java.io.File;

import javax.mail.MessagingException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.chawuzhi.util.EmailUtil;

@Service
public class TopicListener1 {
	
	

	// 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
	@JmsListener(destination = "test-topic")
	public void receiveQueue(String text) throws MessagingException {
		System.out.println("Consumer收到:" + text);
		//EmailUtil.sendEmail("775850004@qq.com", text, "内容:"+text);
		EmailUtil.sendEmailWithFile("775850004@qq.com", "file", "fileRead", new FileSystemResource(new File("d://result.log")));
	}

}
