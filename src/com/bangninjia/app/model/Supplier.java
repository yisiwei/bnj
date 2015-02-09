package com.bangninjia.app.model;

import java.io.Serializable;

public class Supplier implements Serializable {

	private static final long serialVersionUID = 4938602207692423450L;

	private String address;
	private String code;
	private String detail;

	private String district;
	private String email;
	private String id;

	private String linkMan;
	private String linkManMobile;
	private String name;

	private String properties;
	private String simName;
	private String telPhone;

	public Supplier() {

	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkManMobile() {
		return linkManMobile;
	}

	public void setLinkManMobile(String linkManMobile) {
		this.linkManMobile = linkManMobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getSimName() {
		return simName;
	}

	public void setSimName(String simName) {
		this.simName = simName;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	@Override
	public String toString() {
		return "Supplier [address=" + address + ", code=" + code + ", detail="
				+ detail + ", district=" + district + ", email=" + email
				+ ", id=" + id + ", linkMan=" + linkMan + ", linkManMobile="
				+ linkManMobile + ", name=" + name + ", properties="
				+ properties + ", simName=" + simName + ", telPhone="
				+ telPhone + "]";
	}

}
