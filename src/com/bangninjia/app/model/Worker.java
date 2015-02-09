package com.bangninjia.app.model;

import java.io.Serializable;

public class Worker implements Serializable {

	private static final long serialVersionUID = 6428194543842485461L;

	private String address;
	private String age;
	private String email;

	private String id;
	private String mobile;
	private String name;

	private String nation;
	private String nativePlace;
	private String position;

	private String properties;
	private String sex;
	private String userId;

	public Worker(String address, String age, String email, String id,
			String mobile, String name, String nation, String nativePlace,
			String position, String properties, String sex, String userId) {
		super();
		this.address = address;
		this.age = age;
		this.email = email;
		this.id = id;
		this.mobile = mobile;
		this.name = name;
		this.nation = nation;
		this.nativePlace = nativePlace;
		this.position = position;
		this.properties = properties;
		this.sex = sex;
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Worker [address=" + address + ", age=" + age + ", email="
				+ email + ", id=" + id + ", mobile=" + mobile + ", name="
				+ name + ", nation=" + nation + ", nativePlace=" + nativePlace
				+ ", position=" + position + ", properties=" + properties
				+ ", sex=" + sex + ", userId=" + userId + "]";
	}

}
