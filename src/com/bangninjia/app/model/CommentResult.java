package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

public class CommentResult implements Serializable {

	private static final long serialVersionUID = 2663520851195633106L;

	private List<Comment> commentList;
	private String createDate;
	private String creator;

	private double hadPay;
	private Integer orderId;
	private List<DemandItem> orderItems;

	private String orderProperties;
	private double orderStatus;
	private double realPay;

	private String serviceName;
	private String userId;
	private Integer version;

	public CommentResult() {

	}

	public CommentResult(List<Comment> commentList, String createDate,
			String creator, double hadPay, Integer orderId,
			List<DemandItem> orderItems, String orderProperties,
			double orderStatus, double realPay, String serviceName,
			String userId, Integer version) {
		super();
		this.commentList = commentList;
		this.createDate = createDate;
		this.creator = creator;
		this.hadPay = hadPay;
		this.orderId = orderId;
		this.orderItems = orderItems;
		this.orderProperties = orderProperties;
		this.orderStatus = orderStatus;
		this.realPay = realPay;
		this.serviceName = serviceName;
		this.userId = userId;
		this.version = version;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
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

	public double getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(double orderStatus) {
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

	@Override
	public String toString() {
		return "CommentResult [commentList=" + commentList + ", createDate="
				+ createDate + ", creator=" + creator + ", hadPay=" + hadPay
				+ ", orderId=" + orderId + ", orderItems=" + orderItems
				+ ", orderProperties=" + orderProperties + ", orderStatus="
				+ orderStatus + ", realPay=" + realPay + ", serviceName="
				+ serviceName + ", userId=" + userId + ", version=" + version
				+ "]";
	}

}
