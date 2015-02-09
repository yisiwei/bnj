package com.bangninjia.app.model;

import java.io.Serializable;

public class School implements Serializable {

	private static final long serialVersionUID = 4994267481633044147L;

	private Integer id;
	private String title;
	private String contentAbstract;

	private String mainImage;
	private String typeName;
	private String appUrl;

	private String createDate;
	private String appContent;

	public School() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentAbstract() {
		return contentAbstract;
	}

	public void setContentAbstract(String contentAbstract) {
		this.contentAbstract = contentAbstract;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getAppContent() {
		return appContent;
	}

	public void setAppContent(String appContent) {
		this.appContent = appContent;
	}

	@Override
	public String toString() {
		return "School [id=" + id + ", title=" + title + ", contentAbstract="
				+ contentAbstract + ", mainImage=" + mainImage + ", typeName="
				+ typeName + ", appUrl=" + appUrl + ", createDate="
				+ createDate + ", appContent=" + appContent + "]";
	}

}
