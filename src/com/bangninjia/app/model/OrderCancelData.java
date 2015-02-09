package com.bangninjia.app.model;

import java.io.Serializable;

public class OrderCancelData implements Serializable {

	private static final long serialVersionUID = 2446473735868039210L;

	private String userId;
	private String orderId;
	private String reason;

	public OrderCancelData() {

	}

	public OrderCancelData(String userId, String orderId, String reason) {
		super();
		this.userId = userId;
		this.orderId = orderId;
		this.reason = reason;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "OrderCancelData [userId=" + userId + ", orderId=" + orderId
				+ ", reason=" + reason + "]";
	}

}
