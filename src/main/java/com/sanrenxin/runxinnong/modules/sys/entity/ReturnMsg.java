package com.sanrenxin.runxinnong.modules.sys.entity;

import org.apache.poi.ss.formula.functions.T;



public class ReturnMsg<T>{
	private static final long serialVersionUID = 1L;
	private int code;		// 结果代码
	private T data;		// 数据集
	private String msg;		// 结果信息
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
}
