package com.bangninjia.app.model;

import java.io.Serializable;

/**
 * 提交订单的参数
 * 
 */
public class OrderData implements Serializable {

	private static final long serialVersionUID = -3177121329383540456L;

	private OrderCustomerJson customerJson;
	private OrderJson orderJson;

	private String serviceDate;// 施工时间
	private String orderId;// 订单Id

	public OrderData() {

	}

	public OrderCustomerJson getCustomerJson() {
		return customerJson;
	}

	public void setCustomerJson(OrderCustomerJson customerJson) {
		this.customerJson = customerJson;
	}

	public OrderJson getOrderJson() {
		return orderJson;
	}

	public void setOrderJson(OrderJson orderJson) {
		this.orderJson = orderJson;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "OrderData [customerJson=" + customerJson + ", orderJson="
				+ orderJson + ", serviceDate=" + serviceDate + ", orderId="
				+ orderId + "]";
	}

}
