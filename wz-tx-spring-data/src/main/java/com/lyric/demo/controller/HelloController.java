package com.lyric.demo.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyric.demo.entity.TbAcount;
import com.lyric.demo.service.TbAcountService;

@Controller
public class HelloController {

	@Autowired
	private TbAcountService service;
	
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
}
