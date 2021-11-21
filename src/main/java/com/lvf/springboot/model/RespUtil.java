package com.lvf.springboot.model;

import com.github.pagehelper.PageInfo;

public class RespUtil {

	private String code;
	private String msg;
	private Object data;

	public static RespUtil success(PageInfo pageInfo) {
		RespUtil respUtil = new RespUtil();
		respUtil.setCode("0");
		respUtil.setData(pageInfo);
		return respUtil;
	}
	
	public static RespUtil failure(PageInfo pageInfo) {
		RespUtil respUtil = new RespUtil();
		respUtil.setCode("1");
		respUtil.setData(pageInfo);
		return respUtil;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public RespUtil setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
