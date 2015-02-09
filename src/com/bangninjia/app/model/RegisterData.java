package com.bangninjia.app.model;

import java.io.Serializable;

public class RegisterData implements Serializable {

	private static final long serialVersionUID = -1938061959102016830L;

	private String userName;//用户名
	private String kpac;//短信验证码
	private String password;//密码
	private String source;//来源

	public RegisterData() {

	}

	public RegisterData(String userName, String kpac, String password,
			String source) {
		super();
		this.userName = userName;
		this.kpac = kpac;
		this.password = password;
		this.source = source;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getKpac() {
		return kpac;
	}

	public void setKpac(String kpac) {
		this.kpac = kpac;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "RegisterData [userName=" + userName + ", kpac=" + kpac
				+ ", password=" + password + ", source=" + source + "]";
	}

}
