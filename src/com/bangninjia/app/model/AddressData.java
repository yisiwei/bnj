package com.bangninjia.app.model;

import java.io.Serializable;

public class AddressData implements Serializable {

	private static final long serialVersionUID = 4731015680001440609L;

	private String userId;
	private Integer id;
	private String province;

	private String city;
	private String area;
	private String road;

	private String address;
	private String name;
	private String phone;

	public AddressData() {

	}

	public AddressData(String userId, Integer id, String province, String city,
			String area, String road, String address, String name, String phone) {
		super();
		this.userId = userId;
		this.id = id;
		this.province = province;
		this.city = city;
		this.area = area;
		this.road = road;
		this.address = address;
		this.name = name;
		this.phone = phone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "AddressData [userId=" + userId + ", id=" + id + ", province="
				+ province + ", city=" + city + ", area=" + area + ", road="
				+ road + ", address=" + address + ", name=" + name + ", phone="
				+ phone + "]";
	}

}
