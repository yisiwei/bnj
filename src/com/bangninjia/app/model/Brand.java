package com.bangninjia.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌
 * 
 */
public class Brand implements Serializable {

	private static final long serialVersionUID = -6725827066327816807L;

	private String id;// id
	private String chName;// 中文名称
	private String enName;// 英文名称
	private String picLogo;// Logo
	private List<Product> products;// 产品列表

	public Brand() {

	}

	public Brand(String chName, String enName, String id, String picLogo,
			List<Product> products) {
		super();
		this.chName = chName;
		this.enName = enName;
		this.id = id;
		this.picLogo = picLogo;
		this.products = products;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	public String getPicLogo() {
		return picLogo;
	}

	public void setPicLogo(String picLogo) {
		this.picLogo = picLogo;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "BrandBean [id=" + id + ", enName=" + enName + ", chName="
				+ chName + ", picLogo=" + picLogo + ", products=" + products
				+ "]";
	}

}
