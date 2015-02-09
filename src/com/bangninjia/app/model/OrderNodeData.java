package com.bangninjia.app.model;

import java.io.Serializable;

public class OrderNodeData implements Serializable {

	private static final long serialVersionUID = -613178467296754951L;

	private String userId;
	private String orderId;

	public OrderNodeData() {

	}

	public OrderNodeData(String userId, String orderId) {
		super();
		this.userId = userId;
		this.orderId = orderId;
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

	@Override
	public String toString() {
		return "OrderNodeData [userId=" + userId + ", orderId=" + orderId + "]";
	}

}
