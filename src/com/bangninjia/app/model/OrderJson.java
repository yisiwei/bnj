package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

public class OrderJson implements Serializable {

	private static final long serialVersionUID = 6068931348098676742L;

	private String appointmentDate; // 预约时间
	private String brandId;// 品牌ID
	private List<DemandItem> demandItems;
	private DemandProperties demandProperties;
	private String demandSpace;// 作业面积
	private Integer demandType; // 服务类型 (1.壁纸更新,2.墙面更新,3.地板更新)
	private String estimatePrice;// 预算价格
	private String invoiceMemo;// [北京邦您家投资管理有限公司] 发票详情信息
	private String invoiceType;// "0",//发票类型 0个人 1公司
	private Integer isInvoice;// 1//是否开发票 0否 1是
	private Integer sourceId;// 0//来源 1android, 2ios
	private String userId;// 10000//用户ID
	private Integer userLevel;// 0//用户等级
	private String userName;// userNameTest
	private String serviceName;// "壁纸更新"//根据demandType获取
	private String productImages;

	public OrderJson() {

	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public List<DemandItem> getDemandItems() {
		return demandItems;
	}

	public void setDemandItems(List<DemandItem> demandItems) {
		this.demandItems = demandItems;
	}

	public DemandProperties getDemandProperties() {
		return demandProperties;
	}

	public void setDemandProperties(DemandProperties demandProperties) {
		this.demandProperties = demandProperties;
	}

	public String getDemandSpace() {
		return demandSpace;
	}

	public void setDemandSpace(String demandSpace) {
		this.demandSpace = demandSpace;
	}

	public Integer getDemandType() {
		return demandType;
	}

	public void setDemandType(Integer demandType) {
		this.demandType = demandType;
	}

	public String getEstimatePrice() {
		return estimatePrice;
	}

	public void setEstimatePrice(String estimatePrice) {
		this.estimatePrice = estimatePrice;
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

	public Integer getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getProductImages() {
		return productImages;
	}

	public void setProductImages(String productImages) {
		this.productImages = productImages;
	}

	@Override
	public String toString() {
		return "OrderJson [appointmentDate=" + appointmentDate + ", brandId="
				+ brandId + ", demandItems=" + demandItems
				+ ", demandProperties=" + demandProperties + ", demandSpace="
				+ demandSpace + ", demandType=" + demandType
				+ ", estimatePrice=" + estimatePrice + ", invoiceMemo="
				+ invoiceMemo + ", invoiceType=" + invoiceType + ", isInvoice="
				+ isInvoice + ", sourceId=" + sourceId + ", userId=" + userId
				+ ", userLevel=" + userLevel + ", userName=" + userName
				+ ", serviceName=" + serviceName + ", productImages="
				+ productImages + "]";
	}

}
