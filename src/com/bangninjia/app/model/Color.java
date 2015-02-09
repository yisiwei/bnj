package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * 色系
 * 
 */
public class Color implements Serializable {

	private static final long serialVersionUID = -8562789526074084775L;

	private Integer id;
	private String typeName;
	private List<ColorValue> colors;// 颜色值

	public Color() {

	}

	public Color(Integer id, String typeName, List<ColorValue> colors) {
		super();
		this.id = id;
		this.typeName = typeName;
		this.colors = colors;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<ColorValue> getColors() {
		return colors;
	}

	public void setColors(List<ColorValue> colors) {
		this.colors = colors;
	}

	@Override
	public String toString() {
		return "Color [id=" + id + ", typeName=" + typeName + ", colors="
				+ colors + "]";
	}

}
