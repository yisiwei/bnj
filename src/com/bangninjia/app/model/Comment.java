package com.bangninjia.app.model;

import java.io.Serializable;

public class Comment implements Serializable {

	private static final long serialVersionUID = -1377871452067347170L;

	private Integer id;
	private String commentContent;
	private Integer commentGrade;
	private Integer orderId;
	private Integer type;
	private String userId;

	public Comment() {

	}

	public Comment(Integer id, String commentContent, Integer commentGrade,
			Integer orderId, Integer type, String userId) {
		super();
		this.id = id;
		this.commentContent = commentContent;
		this.commentGrade = commentGrade;
		this.orderId = orderId;
		this.type = type;
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Integer getCommentGrade() {
		return commentGrade;
	}

	public void setCommentGrade(Integer commentGrade) {
		this.commentGrade = commentGrade;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", commentContent=" + commentContent
				+ ", commentGrade=" + commentGrade + ", orderId=" + orderId
				+ ", type=" + type + ", userId=" + userId + "]";
	}

}
