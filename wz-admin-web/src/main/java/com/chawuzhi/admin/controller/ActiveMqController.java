package com.chawuzhi.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sendMessage")
public class ActiveMqController {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	@RequestMapping("/message")
	@ResponseBody
	public String sendMessage(@RequestParam(required=false)String message) {
		// destination 是发送的目的地
		jmsTemplate.convertAndSend("hello", message);
		return message;
	}
	
	
	@JmsListener(destination = "out.queue")
	public void receiveQueue(String text) {  
        System.out.println("Consumer收到的报文为:"+text);  
    }  
	
	
}
