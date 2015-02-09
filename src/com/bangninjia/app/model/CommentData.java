package com.bangninjia.app.model;

import java.io.Serializable;

public class CommentData implements Serializable {

	private static final long serialVersionUID = 2970352917123508472L;

	private String userId;
	private String orderId;
	private String grade;
	private String content;

	public CommentData() {

	}

	public CommentData(String userId, String orderId, String grade,
			String content) {
		super();
		this.userId = userId;
		this.orderId = orderId;
		this.grade = grade;
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CommentData [userId=" + userId + ", orderId=" + orderId
				+ ", grade=" + grade + ", content=" + content + "]";
	}

}
