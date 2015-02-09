package com.bangninjia.app.model;

import java.io.Serializable;

public class OrderChangeData implements Serializable {

	private static final long serialVersionUID = 7631003660275597988L;

	private String orderId;
	private String userId;
	private String oper;

	public OrderChangeData() {

	}

	public OrderChangeData(String orderId, String userId, String oper) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.oper = oper;
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

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	@Override
	public String toString() {
		return "OrderChangeData [orderId=" + orderId + ", userId=" + userId
				+ ", oper=" + oper + "]";
	}

}
