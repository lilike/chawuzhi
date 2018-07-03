package com.chawuzhi.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chawuzhi.admin.inter.TbProductService;
import com.chawuzhi.admin.pojo.AjaxJson;

/**
 * 用来下订单的控制器
 * @author lyric
 *
 */
@RequestMapping("/order")
@Controller
public class BuyProductController {

	@Reference(version = "1.0.0",timeout=15000)
	private TbProductService service;
	
	@RequestMapping("/buy")
	@ResponseBody
	public AjaxJson buy(String productName,Integer count) {
		
		AjaxJson json = new AjaxJson();
		boolean displayBuy = service.displayBuy(productName, count);
		
		if (displayBuy) {
			json.setMsg("恭喜!购买"+productName+"成功!");
		}else {
			json.setMsg("购买"+productName+"失败!");
		}
		return json;
	}

	
	@RequestMapping("/buy2")
	@ResponseBody
	public AjaxJson buy2(String productName,Integer count) {
		AjaxJson json = new AjaxJson();
		boolean displayBuy = service.displayBuy(productName, count);
		
		if (displayBuy) {
			json.setMsg("恭喜!购买"+productName+"成功!");
		}else {
			json.setMsg("购买"+productName+"失败!");
		}
		return json;
	}
	
}
