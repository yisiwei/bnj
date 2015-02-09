package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable {

	private static final long serialVersionUID = 7223552168324943332L;

	// private Integer provinceId;
	// private Map<String, String> citys;

	private String cityName;
	private List<String> areaList;

	public City() {

	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<String> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<String> areaList) {
		this.areaList = areaList;
	}

}
