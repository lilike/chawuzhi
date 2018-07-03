package com.chawuzhi.admin.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkCuratorConfig {

	@Bean(initMethod="init")
	public DistributedLock init() {
		DistributedLock client = new DistributedLock();
		return client;
	}
	
}
