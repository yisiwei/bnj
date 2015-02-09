package com.bangninjia.app.model;

import java.io.Serializable;

/**
 * 服务地址
 * 
 */
public class Address implements Serializable {

	private static final long serialVersionUID = 8836199074788120589L;

	private Integer id;
	private String userId;//用户Id
	private String name;//用户姓名

	private String mobile;//用户手机
	private String province;// 省份
	private String city;// 城市

	private String area;// 城区
	private String road;// 环线
	private String address;// 具体地址

	public Address() {

	}

	public Address(Integer id, String userId, String name, String mobile,
			String province, String city, String area, String road,
			String address) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.mobile = mobile;
		this.province = province;
		this.city = city;
		this.area = area;
		this.road = road;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "address [id=" + id + ", userId=" + userId + ", name=" + name
				+ ", mobile=" + mobile + ", province=" + province + ", city="
				+ city + ", area=" + area + ", road=" + road + ", address="
				+ address + "]";
	}

}
