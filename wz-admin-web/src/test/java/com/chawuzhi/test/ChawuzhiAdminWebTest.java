package com.chawuzhi.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chawuzhi.admin.Application;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.admin.pojo.TbContent;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class ChawuzhiAdminWebTest {

	
	@Reference(version="1.0.0")
	private TbContentService contentService;
	
	@Test
	public void test010() throws Exception {
		System.out.println(contentService.findAll());
	}
	
	
	
}
