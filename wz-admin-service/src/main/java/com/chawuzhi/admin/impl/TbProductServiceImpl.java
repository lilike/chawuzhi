package com.chawuzhi.admin.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.admin.inter.TbProductService;
import com.chawuzhi.admin.mapper.TbContentMapper;
import com.chawuzhi.admin.mapper.TbProductMapper;
import com.chawuzhi.admin.pojo.TbContent;
import com.chawuzhi.admin.pojo.TbContentExample;
import com.chawuzhi.admin.pojo.TbProduct;
import com.chawuzhi.admin.utils.DistributedLock;
import com.github.pagehelper.PageHelper;

@Service(version = "1.0.0")
public class TbProductServiceImpl implements TbProductService {

	@Autowired
	private TbProductMapper mapper;
	
	@Autowired
	private DistributedLock distributedLock;
	
	private static final Logger log = LoggerFactory.getLogger(TbProductServiceImpl.class);
	
	@Override
	public boolean displayBuy(String productName, Integer count) throws Exception {
		
		distributedLock.getLock(); /** 获得锁 */
		/** 1. 校验产品名和产品数量数据是否齐全  */
		if (StringUtils.isEmpty(productName) || count == null)  {
			log.info("信息不全!");
			distributedLock.releaseLock(); /** 信息不全,释放锁 */
			return false;
		}
		
		
		/** 2. 首先去数据库中查询库存够不够   */
		int stockCounts = mapper.getProductCount(productName);
		log.info("开始进入订单创建程序..........");
		if (count > stockCounts) {
			log.info("库存剩余{}件,{}库存不足,订单创建失败.....",stockCounts,productName);
			distributedLock.releaseLock(); /** 库存不足,释放锁 */
			return false;
		}
		
		/** 3. 创建订单的业务,模拟需要5秒的时间 */
		boolean isOrderCreated = false;
		try {
			Thread.sleep(5000);
			isOrderCreated = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/** 4. 创建完订单后,去数据库中扣除库存 */
		if (isOrderCreated) {
			log.info("订单创建成功....");
			mapper.displayReduceCount(count, productName);
			distributedLock.releaseLock(); /** 订单购买成功,释放锁 */
			return true;
		}else {
			log.info("订单创建失败...."); /** 订单创建失败,释放锁 */
			distributedLock.releaseLock();
			return false;
		}
		
	}
	
}
