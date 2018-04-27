package com.chawuzhi.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.chawuzhi.admin.inter.TbContentService;
import com.chawuzhi.admin.mapper.TbContentMapper;
import com.chawuzhi.admin.pojo.TbContent;
import com.chawuzhi.admin.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;

@Service(version = "1.0.0")
public class TbContentServiceImpl implements TbContentService {

	@Autowired
	private TbContentMapper mapper;
	
	@Override
	public List<TbContent> findAll() {
		PageHelper.startPage(1, 2);
		return mapper.selectByExample(new TbContentExample());
	}

	
	
}
