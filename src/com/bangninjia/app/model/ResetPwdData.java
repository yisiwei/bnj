package com.bangninjia.app.model;

import java.io.Serializable;

public class ResetPwdData implements Serializable {

	private static final long serialVersionUID = 5581376974586622103L;

	private String userName;
	private String kpac;
	private String newPassword;

	public ResetPwdData() {

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

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ResetPwdData [userName=" + userName + ", kpac=" + kpac
				+ ", newPassword=" + newPassword + "]";
	}

}
