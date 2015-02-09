package com.bangninjia.app.model;

import java.io.Serializable;

/**
 * 踢脚线属性
 * 
 */
public class SeriesProperty implements Serializable {

	private static final long serialVersionUID = 1463196816817712221L;

	private Integer id;// 踢脚线属性id
	private String name;// 踢脚线属性名称
	private String type;// 踢脚线属性型号

	private Integer unitLength;// 踢脚线属性长度
	private double unitPrice;// 踢脚线属性单价
	private String images;// 踢脚线图片链接

	private String spec;// 踢脚线属性规格

	public SeriesProperty() {

	}

	public SeriesProperty(Integer id, String name, String type,
			Integer unitLength, double unitPrice, String images, String spec) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.unitLength = unitLength;
		this.unitPrice = unitPrice;
		this.images = images;
		this.spec = spec;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getUnitLength() {
		return unitLength;
	}

	public void setUnitLength(Integer unitLength) {
		this.unitLength = unitLength;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	@Override
	public String toString() {
		return "SeriesProperty [id=" + id + ", name=" + name + ", type=" + type
				+ ", unitLength=" + unitLength + ", unitPrice=" + unitPrice
				+ ", images=" + images + ", spec=" + spec + "]";
	}

}
