package com.chawuzhi.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.admin.pojo.TbContent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class TbContentController {

	
	@Value(value="${spring.mvc.view.prefix}")
	private String hello;
	
	// 引用Dubbo
	@Reference(version = "1.0.0")
	private TbContentService tbContentService;
	
	@RequestMapping("/hello")
	@ResponseBody
	public  List<TbContent> list() {
		List<TbContent> findAll = tbContentService.findAll();
		return findAll;
	}
	
	@RequestMapping("/good")
	public ModelAndView list1(HttpServletRequest request,HttpServletResponse response) {
		System.out.println(hello);
		ModelAndView mv = new ModelAndView();
		mv.addObject("good", "say Hi");
		mv.setViewName("/hello");
		return mv;
	}
	
}
