package com.chawuzhi.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chawuzhi.admin.Application;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.admin.pojo.TbContent;
import com.chawuzhi.util.HelloUtils;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class TestDataSource {

	@Autowired
	private TbContentService service;
	
	@Test
	public void test001() throws Exception {

		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(service.findAll());
		List<TbContent> list = pageInfo.getList();
		for (TbContent tbContent : list) {
			System.out.println(tbContent);
		}
		
		System.out.println(HelloUtils.sayHello());
	}
	
	
}
