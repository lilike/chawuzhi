package com.lilike.demo.controller;

import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lilike.demo.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerResource {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private CustomerService service;

	/** 通过JMSListener来发送消息 */
	@PostMapping("/message1")
	public void create (String msg) {

		jmsTemplate.convertAndSend("customer:msg1:new",msg);
	}
	
	/** 直接调用方法发送消息,由于方法上添加了@TansactionManager,同样是有事务的 */
	@PostMapping("/message1/direct")
	public void handle(String msg) {
		service.handle(msg);
	}
	
	/** 获取到消息 */
	@PostMapping("/message")
	public String read(String msg) {
		jmsTemplate.setReceiveTimeout(2000);
		Object obj = jmsTemplate.receiveAndConvert("customer:msg:reply");
		return obj + "";
	}
	
}
