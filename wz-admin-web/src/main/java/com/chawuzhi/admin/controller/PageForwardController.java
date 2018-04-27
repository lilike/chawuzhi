package com.chawuzhi.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author lyric
 *
 */
@Controller
public class PageForwardController {
	
	/** 跳转的方法 */
	@RequestMapping("/page/{viewName}")
	public String forward(@PathVariable("viewName")String viewName){
		return "/" + viewName;
	}
}
