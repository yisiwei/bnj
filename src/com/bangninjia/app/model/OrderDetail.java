package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

public class OrderDetail implements Serializable {

	private static final long serialVersionUID = -8412441420332289381L;

	private String appointmentDate;
	private Integer brandId;
	private Long createDate;

	private String serviceDate;
	private String creator;
	private float estimatePrice;

	private String demandSpace;
	private float hadPay;
	private Integer id;

	private String orderId;
	private List<DemandItem> orderItems;
	private DemandProperties orderProperties;

	private String orderStatus;
	private String productImages;
	private float realPay;

	private List<OrderLog> orderLogs;
	private String invoiceMemo;
	private String invoiceType;

	private String ip;
	private String payType;

	private String receiverAddress;
	private String receiverArea;
	private String receiverCity;

	private String receiverName;
	private String receiverPhone;
	private String receiverMobile;

	private String receiverProvince;
	private String receiverRoad;
	private String serviceName;

	private String sourceId;
	private String userId;
	private String userName;

	private String userLevel;
	private Supplier supplier;
	private Worker worker;

	public OrderDetail() {

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

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public float getEstimatePrice() {
		return estimatePrice;
	}

	public void setEstimatePrice(float estimatePrice) {
		this.estimatePrice = estimatePrice;
	}

	public String getDemandSpace() {
		return demandSpace;
	}

	public void setDemandSpace(String demandSpace) {
		this.demandSpace = demandSpace;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<DemandItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<DemandItem> orderItems) {
		this.orderItems = orderItems;
	}

	public DemandProperties getOrderProperties() {
		return orderProperties;
	}

	public void setOrderProperties(DemandProperties orderProperties) {
		this.orderProperties = orderProperties;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getProductImages() {
		return productImages;
	}

	public void setProductImages(String productImages) {
		this.productImages = productImages;
	}

	public float getRealPay() {
		return realPay;
	}

	public void setRealPay(float realPay) {
		this.realPay = realPay;
	}

	public List<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(List<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	public String getInvoiceMemo() {
		return invoiceMemo;
	}

	public void setInvoiceMemo(String invoiceMemo) {
		this.invoiceMemo = invoiceMemo;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
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

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	@Override
	public String toString() {
		return "OrderDetail [appointmentDate=" + appointmentDate + ", brandId="
				+ brandId + ", createDate=" + createDate + ", serviceDate="
				+ serviceDate + ", creator=" + creator + ", estimatePrice="
				+ estimatePrice + ", demandSpace=" + demandSpace + ", hadPay="
				+ hadPay + ", id=" + id + ", orderId=" + orderId
				+ ", orderItems=" + orderItems + ", orderProperties="
				+ orderProperties + ", orderStatus=" + orderStatus
				+ ", productImages=" + productImages + ", realPay=" + realPay
				+ ", orderLogs=" + orderLogs + ", invoiceMemo=" + invoiceMemo
				+ ", invoiceType=" + invoiceType + ", ip=" + ip + ", payType="
				+ payType + ", receiverAddress=" + receiverAddress
				+ ", receiverArea=" + receiverArea + ", receiverCity="
				+ receiverCity + ", receiverName=" + receiverName
				+ ", receiverPhone=" + receiverPhone + ", receiverMobile="
				+ receiverMobile + ", receiverProvince=" + receiverProvince
				+ ", receiverRoad=" + receiverRoad + ", serviceName="
				+ serviceName + ", sourceId=" + sourceId + ", userId=" + userId
				+ ", userName=" + userName + ", userLevel=" + userLevel
				+ ", supplier=" + supplier + ", worker=" + worker + "]";
	}

}
