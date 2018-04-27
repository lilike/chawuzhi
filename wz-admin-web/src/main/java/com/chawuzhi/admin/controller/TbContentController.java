package com.chawuzhi.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.admin.pojo.TbContent;

@RestController
public class TbContentController {

	@Reference(version = "1.0.0")
	private TbContentService tbContentService;
	
	@RequestMapping("/hello")
	@ResponseBody
	public  List<TbContent> list() {
		List<TbContent> findAll = tbContentService.findAll();
		return findAll;
	}
	
	@RequestMapping("/str")
	public String list1() {
		return "ogod";
	}
	
}
