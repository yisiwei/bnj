package com.bangninjia.app.model;

import java.io.Serializable;

public class CommentResultData implements Serializable {

	private static final long serialVersionUID = -5136408593438661905L;

	private String userId;
	private String status;
	private Integer pageNum;
	private Integer limit;

	public CommentResultData() {

	}

	public CommentResultData(String userId, String status, Integer pageNum,
			Integer limit) {
		super();
		this.userId = userId;
		this.status = status;
		this.pageNum = pageNum;
		this.limit = limit;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "CommentResultData [userId=" + userId + ", status=" + status
				+ ", pageNum=" + pageNum + ", limit=" + limit + "]";
	}

}
