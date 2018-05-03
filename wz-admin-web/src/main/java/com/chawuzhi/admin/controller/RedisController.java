package com.chawuzhi.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.admin.pojo.TbContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用来测试SpringBoot整合Redis
 * @author lyric
 *
 */
@Controller
@RequestMapping("/redis")
public class RedisController {

	@Autowired
	private RedisTemplate<String, String> redisTemplatet;
	
	// 引用Dubbo
	@Reference(version = "1.0.0")
	private TbContentService tbContentService;
	
	@RequestMapping("/helloworld")
	@ResponseBody
	public  String list() throws JsonProcessingException {
		
		String hello = redisTemplatet.opsForValue().get("hello");
		if (StringUtils.isEmpty(hello)) {
			List<TbContent> findAll = tbContentService.findAll();
			ObjectMapper mapper = new ObjectMapper();
			hello = mapper.writeValueAsString(findAll);
			redisTemplatet.opsForValue().set("hello", hello, 1000);
		}
		return hello;
	}
	
	
}
