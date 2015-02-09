package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

	private static final long serialVersionUID = 5899509670204887838L;
	
	private String brandId;
	private long id;// 商品id
	private String name;// 商品名称
	private String code;// 商品编码
	private double bnjPrice;// 商品价格
	private double wallpaperContiare;// 壁纸更新壁纸平方米数
	private Integer paintRisenum;// 墙面刷新独有油漆升数
	private double squarePrice;// 墙面刷新独有油漆每平米单价
	private String materialId;// 地板更新材质编号
	private String model;// 商品型号
	private List<String> imageList;// 图片集合

	public Product() {

	}

	public Product(String brandId, long id, String name, String code,
			double bnjPrice, double wallpaperContiare, Integer paintRisenum,
			double squarePrice, String materialId, String model,
			List<String> imageList) {
		super();
		this.brandId = brandId;
		this.id = id;
		this.name = name;
		this.code = code;
		this.bnjPrice = bnjPrice;
		this.wallpaperContiare = wallpaperContiare;
		this.paintRisenum = paintRisenum;
		this.squarePrice = squarePrice;
		this.materialId = materialId;
		this.model = model;
		this.imageList = imageList;
	}

	@Override
	public String toString() {
		return "Product [brandId=" + brandId + ", id=" + id + ", name=" + name
				+ ", code=" + code + ", bnjPrice=" + bnjPrice
				+ ", wallpaperContiare=" + wallpaperContiare
				+ ", paintRisenum=" + paintRisenum + ", squarePrice="
				+ squarePrice + ", materialId=" + materialId + ", model="
				+ model + ", imageList=" + imageList + "]";
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getBnjPrice() {
		return bnjPrice;
	}

	public void setBnjPrice(double bnjPrice) {
		this.bnjPrice = bnjPrice;
	}

	public double getWallpaperContiare() {
		return wallpaperContiare;
	}

	public void setWallpaperContiare(double wallpaperContiare) {
		this.wallpaperContiare = wallpaperContiare;
	}

	public Integer getPaintRisenum() {
		return paintRisenum;
	}

	public void setPaintRisenum(Integer paintRisenum) {
		this.paintRisenum = paintRisenum;
	}

	public double getSquarePrice() {
		return squarePrice;
	}

	public void setSquarePrice(double squarePrice) {
		this.squarePrice = squarePrice;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialld(String materialId) {
		this.materialId = materialId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

}
