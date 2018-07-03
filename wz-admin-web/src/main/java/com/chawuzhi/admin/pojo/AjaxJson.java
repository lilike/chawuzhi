/**
 * Copyright &copy; 2015-2020 <a href="http://www.gc.com.cn/">Redfinger</a> All rights reserved.
 */
package com.chawuzhi.admin.pojo;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * $.ajax后需要接受的JSON
 * 
 * @author
 * 
 */
public class AjaxJson {

	private boolean success = true;// 是否成功
	private String errorCode = "-1";//错误代码
	private String msg = "操作成功";// 提示信息
	private LinkedHashMap<String, Object> body = new LinkedHashMap();//封装json的map
	
	public LinkedHashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(LinkedHashMap<String, Object> body) {
		this.body = body;
	}

	public void put(String key, Object value){//向json中添加属性，在js中访问，请调用data.map.key
		body.put(key, value);
	}
	
	public void remove(String key){
		body.remove(key);
	}
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {//向json中添加属性，在js中访问，请调用data.msg
		this.msg = msg;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
