package com.bangninjia.app.model;

import java.io.Serializable;

/**
 * 获取颜色的参数
 *
 */
public class ColorData implements Serializable{

	private static final long serialVersionUID = 5557097461271935922L;
	
	private String colorSeriesId;
	private String brandId;
	
	public ColorData() {
		
	}

	public String getColorSeriesId() {
		return colorSeriesId;
	}

	public void setColorSeriesId(String colorSeriesId) {
		this.colorSeriesId = colorSeriesId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	@Override
	public String toString() {
		return "ColorData [colorSeriesId=" + colorSeriesId + ", brandId="
				+ brandId + "]";
	}

}
