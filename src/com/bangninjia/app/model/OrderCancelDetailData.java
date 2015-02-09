package com.bangninjia.app.model;

import java.io.Serializable;

public class OrderCancelDetailData implements Serializable {

	private static final long serialVersionUID = -2310084267679499488L;

	private String orderId;
	private String userId;

	public OrderCancelDetailData() {

	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "OrderCancelDetailData [orderId=" + orderId + ", userId="
				+ userId + "]";
	}

}
