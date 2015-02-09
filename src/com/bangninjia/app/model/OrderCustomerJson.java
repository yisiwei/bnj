package com.bangninjia.app.model;

public class OrderCustomerJson {

	private String address;// 客户地址-具体
	private String area;// 客户地址-城区
	private String city;// 客户地址-城市
	private String mobile;
	private String name;// 客户姓名
	private String phone;// 客户手机号
	private String province;// 客户地址-省份
	private String road;// 客户地址-环线

	public OrderCustomerJson() {

	}

	public OrderCustomerJson(String address, String area, String city,
			String mobile, String name, String phone, String province,
			String road) {
		super();
		this.address = address;
		this.area = area;
		this.city = city;
		this.mobile = mobile;
		this.name = name;
		this.phone = phone;
		this.province = province;
		this.road = road;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	@Override
	public String toString() {
		return "OrderCustomerJson [address=" + address + ", area=" + area
				+ ", city=" + city + ", mobile=" + mobile + ", name=" + name
				+ ", phone=" + phone + ", province=" + province + ", road="
				+ road + "]";
	}

}
