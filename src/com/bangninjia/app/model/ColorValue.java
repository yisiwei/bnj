package com.bangninjia.app.model;

import java.io.Serializable;

/**
 * 颜色值
 * 
 */
public class ColorValue implements Serializable {

	private static final long serialVersionUID = -5283396022135050589L;

	private Integer id;
	private String colorName;// 颜色名称  嫩粉
	private String modelNum;// 颜色型号  50RR 83/040
	private String colorNum;// 颜色编号  #feefea

	public ColorValue() {

	}

	public ColorValue(Integer id, String colorName, String modelNum,
			String colorNum) {
		super();
		this.id = id;
		this.colorName = colorName;
		this.modelNum = modelNum;
		this.colorNum = colorNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getModelNum() {
		return modelNum;
	}

	public void setModelNum(String modelNum) {
		this.modelNum = modelNum;
	}

	public String getColorNum() {
		return colorNum;
	}

	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}

	@Override
	public String toString() {
		return "ColorValue [id=" + id + ", colorName=" + colorName
				+ ", modelNum=" + modelNum + ", colorNum=" + colorNum + "]";
	}

}
