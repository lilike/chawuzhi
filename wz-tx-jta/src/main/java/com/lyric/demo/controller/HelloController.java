package com.lyric.demo.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyric.demo.entity.TbAcount;
import com.lyric.demo.service.TbAcountService;

@Controller
public class HelloController {

	@Autowired
	private TbAcountService service;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@ResponseBody
	@RequestMapping("/hello")
	public String sayHi() {
		return "good";
	}
	
	
	/**
	 * 查询所有的账户
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAll")
	public List<TbAcount> findAll() {
		return service.findAll();
	}
	

	/**
	 * 查询所有的账户
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveByAnno")
	public TbAcount saveByAnnoation(TbAcount tbAcount) {
		return service.createByAnnoation(tbAcount);
	}

	/**
	 * 查询所有的账户
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveByCode")
	@Transactional
	public TbAcount saveByCode(TbAcount tbAcount) {
		return service.createByCode(tbAcount);
	}
	
	
	/** 获取到消息 */
	@RequestMapping("/message")
	@Transactional
	@ResponseBody
	public String read() {
		jmsTemplate.setReceiveTimeout(2000);
		Object obj = jmsTemplate.receiveAndConvert("sys:lilike:demo2");
		return obj + "";
	}
	
	/** 获取到消息 */
	@GetMapping("/sendMessage")
	@Transactional
	public void write(String message) {
		jmsTemplate.convertAndSend("sys:lilike:demo1", message);
	}
	
	/** 直接调用 */
	@GetMapping("/sendMessageDirect")
	public void directSend(String message) {
		service.listenMessageAndSaveTbAcount(message);
	}
	
}
