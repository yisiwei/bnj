package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.Map;

public class Area implements Serializable {

	private static final long serialVersionUID = -1644930707167391145L;

	private String cityId;
	private Map<String, String> areas;

	public Area() {

	}

	public Area(String cityId, Map<String, String> areas) {
		super();
		this.cityId = cityId;
		this.areas = areas;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public Map<String, String> getAreas() {
		return areas;
	}

	public void setAreas(Map<String, String> areas) {
		this.areas = areas;
	}

	@Override
	public String toString() {
		return "Area [cityId=" + cityId + ", areas=" + areas + "]";
	}

}
