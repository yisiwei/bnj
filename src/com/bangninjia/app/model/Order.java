package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

	private static final long serialVersionUID = -1637284790327887521L;

	private String createDate;// 创建时间
	private String creator;// 创建者
	private List<DemandItem> orderItems;// 商品列表

	private String orderProperties;// 属性
	private double hadPay;// 0, //已经支付金额
	private Integer orderId;// 订单编号

	private Integer orderStatus;// 订单状态
	private double realPay;// 0，订单金额
	private String serviceName;// 地板更新

	private String userId;
	private Integer version;
	private String productImages;

	private String demandSpace;

	public Order() {

	}

	public Order(String createDate, String creator,
			List<DemandItem> orderItems, String orderProperties, double hadPay,
			Integer orderId, Integer orderStatus, double realPay,
			String serviceName, String userId, Integer version,
			String productImages, String demandSpace) {
		super();
		this.createDate = createDate;
		this.creator = creator;
		this.orderItems = orderItems;
		this.orderProperties = orderProperties;
		this.hadPay = hadPay;
		this.orderId = orderId;
		this.orderStatus = orderStatus;
		this.realPay = realPay;
		this.serviceName = serviceName;
		this.userId = userId;
		this.version = version;
		this.productImages = productImages;
		this.demandSpace = demandSpace;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public List<DemandItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<DemandItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getOrderProperties() {
		return orderProperties;
	}

	public void setOrderProperties(String orderProperties) {
		this.orderProperties = orderProperties;
	}

	public double getHadPay() {
		return hadPay;
	}

	public void setHadPay(double hadPay) {
		this.hadPay = hadPay;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getRealPay() {
		return realPay;
	}

	public void setRealPay(double realPay) {
		this.realPay = realPay;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getProductImages() {
		return productImages;
	}

	public void setProductImages(String productImages) {
		this.productImages = productImages;
	}

	public String getDemandSpace() {
		return demandSpace;
	}

	public void setDemandSpace(String demandSpace) {
		this.demandSpace = demandSpace;
	}

	@Override
	public String toString() {
		return "Order [createDate=" + createDate + ", creator=" + creator
				+ ", orderItems=" + orderItems + ", orderProperties="
				+ orderProperties + ", hadPay=" + hadPay + ", orderId="
				+ orderId + ", orderStatus=" + orderStatus + ", realPay="
				+ realPay + ", serviceName=" + serviceName + ", userId="
				+ userId + ", version=" + version + ", productImages="
				+ productImages + ", demandSpace=" + demandSpace + "]";
	}

}
