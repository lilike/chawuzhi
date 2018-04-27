package com.chawuzhi.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chawuzhi.admin.Application;
import com.chawuzhi.admin.inter.TbContentService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class TestDataSource {

	//@Reference(version = "1.0.0")
	@Autowired
	private TbContentService service;
	
	@Test
	public void test001() throws Exception {
		System.out.println(service.findAll());
	
	}
	
	
}
