package com.chawuzhi.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class TopicListener2 {

	// 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
	@JmsListener(destination = "test-topic")
	@SendTo("out.queue")
	public String receiveQueue(String text) {
		System.out.println("Consumer2收到:" + text);
		return "Consumer2收到!";
	}
}
