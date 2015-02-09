package com.bangninjia.app.model;

import java.io.Serializable;

public class OrderLog implements Serializable {

	private static final long serialVersionUID = 639512032788426758L;

	private String beforeRemark;
	private Long createDate;
	private String creator;

	private String id;
	private Integer newOrderStatus;
	private String operRemark;

	private String remark;

	public OrderLog() {

	}

	public OrderLog(String beforeRemark, Long createDate, String creator,
			String id, Integer newOrderStatus, String operRemark, String remark) {
		super();
		this.beforeRemark = beforeRemark;
		this.createDate = createDate;
		this.creator = creator;
		this.id = id;
		this.newOrderStatus = newOrderStatus;
		this.operRemark = operRemark;
		this.remark = remark;
	}

	public String getBeforeRemark() {
		return beforeRemark;
	}

	public void setBeforeRemark(String beforeRemark) {
		this.beforeRemark = beforeRemark;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNewOrderStatus() {
		return newOrderStatus;
	}

	public void setNewOrderStatus(Integer newOrderStatus) {
		this.newOrderStatus = newOrderStatus;
	}

	public String getOperRemark() {
		return operRemark;
	}

	public void setOperRemark(String operRemark) {
		this.operRemark = operRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "OrderLog [beforeRemark=" + beforeRemark + ", createDate="
				+ createDate + ", creator=" + creator + ", id=" + id
				+ ", newOrderStatus=" + newOrderStatus + ", operRemark="
				+ operRemark + ", remark=" + remark + "]";
	}

}
