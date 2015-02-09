package com.bangninjia.app.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 8887368151544656412L;

	private String userId;
	private String userName;

	public User() {

	}

	public User(String userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
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

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + "]";
	}

}
