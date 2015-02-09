package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * 踢脚线
 *
 */
public class SkirtingBoard implements Serializable {

	private static final long serialVersionUID = -4755801355704591350L;

	private Integer id;
	private String name;
	private List<SeriesProperty> seriesProperties;

	public SkirtingBoard() {

	}

	public SkirtingBoard(Integer id, String name,
			List<SeriesProperty> seriesProperties) {
		super();
		this.id = id;
		this.name = name;
		this.seriesProperties = seriesProperties;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SeriesProperty> getSeriesProperties() {
		return seriesProperties;
	}

	public void setSeriesProperties(List<SeriesProperty> seriesProperties) {
		this.seriesProperties = seriesProperties;
	}

	@Override
	public String toString() {
		return "SkirtingBoard [id=" + id + ", name=" + name
				+ ", seriesProperties=" + seriesProperties + "]";
	}

}
