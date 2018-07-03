package com.chawuzhi.test;

import java.util.concurrent.CountDownLatch;

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
	
	
	public static void main(String[] args) throws Exception {
		
		 final CountDownLatch latch = new CountDownLatch(1);
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean flag = false;
				while (true) {
					System.out.println("开启循环");
					try {
						if (!flag) {
							latch.await();
							System.out.println("flag改变了吗");
							flag = true;
						}else {
							System.out.println("出去了");
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		});
		t1.start();
		
		System.out.println("我是主线程");
		
		Thread.sleep(5000);
		System.out.println("主线程OK");
		latch.countDown();
		
	}
	
	
}
