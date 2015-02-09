package com.bangninjia.app.model;

import java.io.Serializable;

public class Coupon implements Serializable {

	private static final long serialVersionUID = 6190360361670778637L;

	private float amount;
	private String code;
	private String createDate;

	private String creator;
	private String endDate;
	// private Integer proPlanId;

	private String startDate;
	private Integer status;
	private String summary;

	private Integer useCount;

	public Coupon() {

	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	@Override
	public String toString() {
		return "Coupon [amount=" + amount + ", code=" + code + ", createDate="
				+ createDate + ", creator=" + creator + ", endDate=" + endDate
				+ ", startDate=" + startDate + ", status=" + status
				+ ", summary=" + summary + ", useCount=" + useCount + "]";
	}

}
