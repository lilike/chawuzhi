package com.lyric.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lyric.demo.entity.TbAcount;

public interface TbAcountRepository extends JpaRepository<TbAcount, String> {
	
	
}