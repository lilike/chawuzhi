package com.lyric.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lyric.demo.Application;
import com.lyric.demo.dao.TbAcountRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class TestDemo {
	
	@Autowired
	private TbAcountRepository tbAcountRepository;

	@Before
	public void before () {
		System.out.println("开始...........");
	}
	
	@Test
	public void test001() throws Exception {
		
		System.out.println(tbAcountRepository.findAll());
		
	}
	

}
