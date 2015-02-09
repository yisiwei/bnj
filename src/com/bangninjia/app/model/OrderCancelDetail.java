package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * 取消订单详情
 *
 */
public class OrderCancelDetail implements Serializable {

	private static final long serialVersionUID = 8833754963115235250L;

	private String appointmentDate;
	private Integer brandId;
	private Long createDate;

	private String creator;
	private float demandSpace;
	private float estimatePrice;

	private float hadPay;
	private Integer id;
	private Integer orderId;

	private List<OrderLog> orderLogs;
	private String orderStatus;
	private float payType;

	private float realPay;
	private String receiverAddress;
	private String receiverArea;

	private String receiverCity;
	private String receiverName;
	private String receiverPhone;

	private String receiverProvince;
	private String receiverRoad;
	private String serviceName;

	private Integer sourceId;
	private String userId;
	private Integer userLevel;

	public OrderCancelDetail() {

	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public float getDemandSpace() {
		return demandSpace;
	}

	public void setDemandSpace(float demandSpace) {
		this.demandSpace = demandSpace;
	}

	public float getEstimatePrice() {
		return estimatePrice;
	}

	public void setEstimatePrice(float estimatePrice) {
		this.estimatePrice = estimatePrice;
	}

	public float getHadPay() {
		return hadPay;
	}

	public void setHadPay(float hadPay) {
		this.hadPay = hadPay;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public List<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(List<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public float getPayType() {
		return payType;
	}

	public void setPayType(float payType) {
		this.payType = payType;
	}

	public float getRealPay() {
		return realPay;
	}

	public void setRealPay(float realPay) {
		this.realPay = realPay;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverArea() {
		return receiverArea;
	}

	public void setReceiverArea(String receiverArea) {
		this.receiverArea = receiverArea;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverRoad() {
		return receiverRoad;
	}

	public void setReceiverRoad(String receiverRoad) {
		this.receiverRoad = receiverRoad;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

}
