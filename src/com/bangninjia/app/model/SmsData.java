package com.bangninjia.app.model;

import java.io.Serializable;

public class SmsData implements Serializable {

	private static final long serialVersionUID = 7757970844416772342L;

	private String phone;
	private String comefrom;
	private String source;

	public SmsData() {

	}

	public SmsData(String phone, String comefrom, String source) {
		super();
		this.phone = phone;
		this.comefrom = comefrom;
		this.source = source;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "SmsData [phone=" + phone + ", comefrom=" + comefrom
				+ ", source=" + source + "]";
	}

}
