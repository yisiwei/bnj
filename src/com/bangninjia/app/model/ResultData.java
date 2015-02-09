package com.bangninjia.app.model;

import java.io.Serializable;

public class ResultData implements Serializable {

	private static final long serialVersionUID = -3981100505907730910L;

	private String code;
	private String msg;

	public ResultData() {

	}

	public ResultData(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
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

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResultData [code=" + code + ", msg=" + msg + "]";
	}

}
