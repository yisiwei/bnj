package com.bangninjia.app.model;

import java.io.Serializable;

/**
 * 发票
 * 
 */
public class Invoice implements Serializable {

	private static final long serialVersionUID = 4951949722564976867L;

	private String type;// 发票类型
	private String title;// 发票抬头（个人/单位）
	private String unitName;// 单位名称
	private String content;// 发票内容

	public Invoice() {

	}

	public Invoice(String type, String title, String unitName, String content) {
		super();
		this.type = type;
		this.title = title;
		this.unitName = unitName;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Invoice [type=" + type + ", title=" + title + ", unitName="
				+ unitName + ", content=" + content + "]";
	}

}
