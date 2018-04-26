package com.chawuzhi.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chawuzhi.admin.Application;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.util.HelloUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class TestDataSource {

	@Autowired
	private TbContentService service;
	
	@Test
	public void test001() throws Exception {
		
		System.out.println(service.findAll());
		
		System.out.println(HelloUtils.sayHello());
	}
	
	
}
