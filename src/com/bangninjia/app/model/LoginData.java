package com.bangninjia.app.model;

/**
 * 登录的参数
 * 
 */
public class LoginData {

	private String userName;// 手机号
	private String password;// 密码
	private String comefrom;// 1：用户 2：商家 3：作业人员

	public LoginData() {

	}

	public LoginData(String userName, String password, String comefrom) {
		super();
		this.userName = userName;
		this.password = password;
		this.comefrom = comefrom;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	@Override
	public String toString() {
		return "LoginData [userName=" + userName + ", password=" + password
				+ ", comefrom=" + comefrom + "]";
	}

}
