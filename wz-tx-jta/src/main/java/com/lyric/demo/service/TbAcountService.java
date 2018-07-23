package com.lyric.demo.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.lyric.demo.dao.TbAcountRepository;
import com.lyric.demo.entity.TbAcount;

@Service
public class TbAcountService {

	private static final Logger log = LoggerFactory.getLogger(TbAcountService.class);
	

	@Autowired
	private TbAcountRepository tbAcountRepository;

	@Autowired
	private JmsTemplate jmsTemplate;
	
	/** 将事务管理器注入  */
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Transactional
	public List<TbAcount> findAll() {
		return tbAcountRepository.findAll();
	}

	/**
	 * 根据注解事务的方式来创建账户
	 * @param tbAcount
	 * @return
	 */
	@Transactional/** 注解中也可以设置事务的传播机制和隔离机制  */
	public TbAcount createByAnnoation(TbAcount tbAcount) {
		return tbAcountRepository.save(tbAcount);
	}
	
	/**
	 * 根据代码的方式来创建事务
	 * @param tbAcount
	 * @return
	 */
	public TbAcount createByCode(TbAcount tbAcount) {
		TbAcount acount = null;
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		/** 设置事务传播机制为支持事务 */
		int i = 1 / 0;
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
		definition.setTimeout(15);
		/** 根据事务定义创建一个事务  */
		TransactionStatus staus = transactionManager.getTransaction(definition);
		
		try {
			acount = tbAcountRepository.save(tbAcount);
			/** 事务提交 */
			transactionManager.commit(staus);
		} catch (Exception e) {
			/** 事务回滚 */
			transactionManager.rollback(staus);
			throw e;
		}
		return acount;
	}
	
	
	/** 监听一个消息,同时插入数据 */
	@Transactional
	@JmsListener(destination="sys:lilike:demo1")
	public void listenMessageAndSaveTbAcount(String message) {
		
		/** 对接收到的消息进行分解 */
		String[] tb = message.split(",");
		String userName = tb[0];
		Integer money = Integer.valueOf(tb[1]);
		
		TbAcount tbA = new TbAcount();
		tbA.setMoney(money);
		tbA.setUsername(userName);
		
		/** 1. 保存消息到数据库  */
		TbAcount save = tbAcountRepository.save(tbA);
		
		log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$可能出错$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		//int i = 1 / 0;

		/** 2. 发送消息 */
		jmsTemplate.convertAndSend("sys:lilike:demo2", userName);		
		
		log.info(save.toString());
	}
	
	
	
}

