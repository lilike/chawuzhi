package com.lilike.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

	
	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/** 监听 */
	@Transactional // 申明式事务管理
	@JmsListener(destination="customer:msg1:new",containerFactory="msgFactory") // 配置自己创建的监听容器工厂类
	public void handle(String msg) {
		log.info("收到消息" + msg);
		String reply = "收到:" + msg;
		jmsTemplate.convertAndSend("customer:msg:reply", msg);

		/** 模拟出现错误  */
		if (msg.contains("error")) {
			throw new RuntimeException("出错了");
		}
		
	}
	
	
}
