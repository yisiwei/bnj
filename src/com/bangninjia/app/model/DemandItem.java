package com.bangninjia.app.model;

import java.io.Serializable;

public class DemandItem implements Serializable {

	private static final long serialVersionUID = 4396019878005777540L;

	private String brandId;// 品牌Id
	private String memo;// 记录主材的颜色、规格等
	private String name;// 名称
	private String specialProductType;

	private String price;// 单价
	private String productCode;// 商品编码
	private long productId;// 商品Id

	private Integer quantity;// 数量（人工费为作业面积）
	private String smallPic;
	private Integer type;// 类型 （1.主材费2.人工费3.辅材费4.搬运费5.运输费6.踢脚线费）

	public DemandItem() {

	}

	public String getSpecialProductType() {
		return specialProductType;
	}

	public void setSpecialProductType(String specialProductType) {
		this.specialProductType = specialProductType;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSmallPic() {
		return smallPic;
	}

	public void setSmallPic(String smallPic) {
		this.smallPic = smallPic;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DemandItem [brandId=" + brandId + ", memo=" + memo + ", name="
				+ name + ", specialProductType=" + specialProductType
				+ ", price=" + price + ", productCode=" + productCode
				+ ", productId=" + productId + ", quantity=" + quantity
				+ ", smallPic=" + smallPic + ", type=" + type + "]";
	}

}
