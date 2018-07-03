package com.chawuzhi.admin.pojo;

import com.chawuzhi.admin.base.BaseDomain;

/**
 * 商品类
 * @author lyric
 *
 */
public class TbProduct extends BaseDomain {

	private String productName;
	
	private Integer productCount;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}
	
	
	
}
