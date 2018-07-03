package com.chawuzhi.admin.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 产品的功能
 * @author lyric
 *
 */
public interface TbProductMapper {

	/**
	 * 下订单成功后减库存的功能
	 */
	public void displayReduceCount(@Param("productCount")int count,@Param("productName")String productName);
	
	/**
	 * 查询库存是否足够的操作
	 */
	public Integer getProductCount(@Param("productName")String productName);
	
}
