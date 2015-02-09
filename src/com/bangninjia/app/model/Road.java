package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

public class Road implements Serializable {

	private static final long serialVersionUID = -5664658145107627469L;

	// private String areaId;
	// private Map<String, String> roads;

	private String areaName;
	private List<String> roadList;

	public Road() {

	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public List<String> getRoadList() {
		return roadList;
	}

	public void setRoadList(List<String> roadList) {
		this.roadList = roadList;
	}

}
