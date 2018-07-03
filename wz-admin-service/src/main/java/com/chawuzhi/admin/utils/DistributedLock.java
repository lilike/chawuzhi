package com.chawuzhi.admin.utils;

import java.util.concurrent.CountDownLatch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.extcheck.Main;

/**
 * 用来实现分布式锁的工具类
 * @author lyric
 *
 */
public class DistributedLock {
	
	private CuratorFramework client = null;
	
	// zookeeper 的服务端
	public static final String ZOOKEEPER_SERVER = "120.79.213.49:2181";
	
	// 总的节点
	public static final String NAME_SPACE = "ZKLock_NameSpace";
	
	// 用于挂起当前的请求,并且等待上一个分布式锁的释放
	private static CountDownLatch zklocklatch = new CountDownLatch(1);
	
	// 分布式锁的节点
	private static final String DISTRIBUTED_LOCK = "distributed_lock_order";
	
	// 分布式锁的总节点名
	private static final String ZK_LOCK_PROJECT = "lyric_lock";
	
	/**
	 * 创建zk锁的总节点,相当于eclipse的工作空间下的项目
	 * ZKLock_NameSpace  : 命名空间
	 * 		|
	 * 		 ____ lyric_lock : 所有锁的父节点
	 * 				|
	 * 				 _____distributed_lock_order : 其中一个锁节点
	 */
	
	private static final Logger log = LoggerFactory.getLogger(DistributedLock.class);

	
	public void init() throws Exception {
		log.info("==================start init zkClient============================");
		if (getClient() != null) {
			return;
		}
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		setClient(CuratorFrameworkFactory.builder().connectString(ZOOKEEPER_SERVER).sessionTimeoutMs(10000)
				 .retryPolicy(retryPolicy).namespace(NAME_SPACE).build());
		// 启动客户端
		getClient().start();
		// 使用命名空间
		setClient(getClient().usingNamespace(NAME_SPACE));
		log.info("==================end init zkClient============================");
		
		log.info("=====================start create parentNode======================");
		// 首先判断这个锁节点是否为空,如果为空,说明没有锁,就先创建
		if(getClient().checkExists().forPath("/"+ZK_LOCK_PROJECT) == null) {
			getClient().create()
				.creatingParentsIfNeeded() // 递归创建
				.withMode(CreateMode.PERSISTENT) // 永久节点
				.withACL(Ids.OPEN_ACL_UNSAFE) // 默认的ACL权限
				.forPath("/"+ZK_LOCK_PROJECT);
		}
		
		// 针对zk的分布式节点,创建响应的wather事件进行监听
		// 为什么要添加父节点?
		//当释放锁后,watcher事件会监听到,然后告诉后面等待的线程重新获得一把锁
		addWatcherToLock("/" + ZK_LOCK_PROJECT);
		//addWatcherToLockTest("/db");
		
		log.info("======================end create parentNode===================");
	}

	public CuratorFramework getClient() {
		return client;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}
	
	

	/**
	 * 获取分布式锁
	 * 使用死循环,当且仅当上一个锁释放并且当前请求获得锁成功后才会跳出
	 * @throws Exception
	 */
	public void getLock() throws Exception {
		while(true) {
			try {
				// 创建业务锁
				getClient().create()
					.creatingParentsIfNeeded()
					// session失效后,锁必须要断开,所以是临时节点
					.withMode(CreateMode.EPHEMERAL) //The znode will be deleted upon the client's disconnect.
					.withACL(Ids.OPEN_ACL_UNSAFE)
					.forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK);
				log.info("获取分布式锁成功....");
				return;
			} catch (Exception e) {
				log.info("获取分布式锁失败...");
				try {
					// 如果没有获取到锁,就需要重新设置同步资源值
					if (zklocklatch.getCount() <= 0) {
						zklocklatch = new CountDownLatch(1);
					}
					// 阻塞线程
					zklocklatch.await();
				}catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		}
	}
	
	/**
	 * 创建Watcher监听
	 * @param string
	 * @throws Exception 
	 */
	public void addWatcherToLock(String path) throws Exception {
		final PathChildrenCache cache = new PathChildrenCache(getClient(), path, true);
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
					String path = event.getData().getPath();
					log.info("上一个会话已经释放锁或该会话已经断开,节点路径为:" + path);
					if (path.contains(DISTRIBUTED_LOCK)) {
						log.info("释放计数器,让当前请求来获得分布式锁...");
						// 如果这个锁是订单锁,就释放
						zklocklatch.countDown();
					}
				}
			}
		});
	}

	/**
	 * 测试用
	 * @param path
	 * @throws Exception
	 */
	public void addWatcherToLockTest(String path) throws Exception {
		final PathChildrenCache cache = new PathChildrenCache(getClient(), path, true);
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				System.err.println(event.getType().toString());
				System.err.println(event.getData());
			}
		});
	}

	// 释放锁
	public boolean releaseLock() {
		
		try{
			if(getClient().checkExists().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK) != null) {
				getClient().delete().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("分布式锁释放完毕!");
		return true;
	}
	
	
	public static void main(String[] args) throws Exception {
		DistributedLock lock = new DistributedLock();
		lock.init();
		
		System.out.println("睡了....");
		Thread.sleep(1000000);
		
	}
	
	
	
}
