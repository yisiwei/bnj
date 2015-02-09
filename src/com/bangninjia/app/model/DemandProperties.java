package com.bangninjia.app.model;

import java.io.Serializable;

public class DemandProperties implements Serializable {

	private static final long serialVersionUID = -8097966885285505199L;

	private String 电梯信息;
	private String 楼层信息;
	private String 现状信息;// 开裂_蜕皮_陈旧,可多选分隔符为_
	private String 服务类型;// 新装地板_更新地板,可多选分隔符为_

	public DemandProperties() {

	}

	public DemandProperties(String 电梯信息, String 楼层信息, String 现状信息, String 服务类型) {
		super();
		this.电梯信息 = 电梯信息;
		this.楼层信息 = 楼层信息;
		this.现状信息 = 现状信息;
		this.服务类型 = 服务类型;
	}

	public String get电梯信息() {
		return 电梯信息;
	}

	public void set电梯信息(String 电梯信息) {
		this.电梯信息 = 电梯信息;
	}

	public String get楼层信息() {
		return 楼层信息;
	}

	public void set楼层信息(String 楼层信息) {
		this.楼层信息 = 楼层信息;
	}

	public String get现状信息() {
		return 现状信息;
	}

	public void set现状信息(String 现状信息) {
		this.现状信息 = 现状信息;
	}

	public String get服务类型() {
		return 服务类型;
	}

	public void set服务类型(String 服务类型) {
		this.服务类型 = 服务类型;
	}

	@Override
	public String toString() {
		return "DemandProperties [电梯信息=" + 电梯信息 + ", 楼层信息=" + 楼层信息 + ", 现状信息="
				+ 现状信息 + ", 服务类型=" + 服务类型 + "]";
	}

}
