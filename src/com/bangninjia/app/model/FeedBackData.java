package com.bangninjia.app.model;

import java.io.Serializable;

public class FeedBackData implements Serializable {

	private static final long serialVersionUID = 1383142831431049517L;

	private String content;
	private String source;

	public FeedBackData() {

	}

	public FeedBackData(String content, String source) {
		super();
		this.content = content;
		this.source = source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
