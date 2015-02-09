package com.bangninjia.app.model;

import java.io.Serializable;

public class ModifyPwdData implements Serializable {

	private static final long serialVersionUID = 3694458517718781166L;

	private String userId;
	private String userName;
	private String password;// 旧密码
	private String newPassword;// 新密码
	private String comefrom;

	public ModifyPwdData() {

	}

	public ModifyPwdData(String userId, String userName, String password,
			String newPassword, String comefrom) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.newPassword = newPassword;
		this.comefrom = comefrom;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	@Override
	public String toString() {
		return "ModifyPwdData [userId=" + userId + ", userName=" + userName
				+ ", password=" + password + ", newPassword=" + newPassword
				+ ", comefrom=" + comefrom + "]";
	}

}
