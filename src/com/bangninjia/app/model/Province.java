package com.bangninjia.app.model;

import java.io.Serializable;

public class Province implements Serializable {

	private static final long serialVersionUID = -4486358354808416840L;

	private Integer id;
	private String province;

	public Province() {

	}

	public Province(Integer id, String province) {
		super();
		this.id = id;
		this.province = province;
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

	@Override
	public String toString() {
		return province;
	}

}
