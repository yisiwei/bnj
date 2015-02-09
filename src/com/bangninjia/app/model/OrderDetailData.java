package com.bangninjia.app.model;

import java.io.Serializable;

public class OrderDetailData implements Serializable {

	private static final long serialVersionUID = 6368999822586425345L;

	private String orderId;
	private String type;

	public OrderDetailData() {

	}

	public OrderDetailData(String orderId, String type) {
		super();
		this.orderId = orderId;
		this.type = type;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "OrderDetailData [orderId=" + orderId + ", type=" + type + "]";
	}

}
