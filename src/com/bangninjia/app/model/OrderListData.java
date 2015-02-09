package com.bangninjia.app.model;

import java.io.Serializable;

public class OrderListData implements Serializable {

	private static final long serialVersionUID = -769017124333063677L;

	private String userId;
	private String type;// user(正常), worker(作业人员),vendor(供应商)
	private String status;// all
	private Integer pageNum;// 当前页码数
	private Integer limit;// 显示条数

	public OrderListData() {

	}

	public OrderListData(String userId, String type, String status,
			Integer pageNum, Integer limit) {
		super();
		this.userId = userId;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return "OrderListData [userId=" + userId + ", type=" + type
				+ ", status=" + status + ", pageNum=" + pageNum + ", limit="
				+ limit + "]";
	}

}
