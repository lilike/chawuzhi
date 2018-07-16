package com.lyric.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private TbAcountRepository tbAcountRepository;

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
		/** 设置事务隔离级别为可序列化 */
		definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
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
	
	
	
}

