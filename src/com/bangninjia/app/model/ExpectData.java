package com.bangninjia.app.model;

import java.io.Serializable;

public class ExpectData implements Serializable {

	private static final long serialVersionUID = 8003647026134913507L;

	private String modular;
	private String centent;
	private String regsource;

	public ExpectData() {

	}

	public ExpectData(String modular, String centent, String regsource) {
		super();
		this.modular = modular;
		this.centent = centent;
		this.regsource = regsource;
	}

	public String getModular() {
		return modular;
	}

	public void setModular(String modular) {
		this.modular = modular;
	}

	public String getCentent() {
		return centent;
	}

	public void setCentent(String centent) {
		this.centent = centent;
	}

	public String getRegsource() {
		return regsource;
	}

	public void setRegsource(String regsource) {
		this.regsource = regsource;
	}

	@Override
	public String toString() {
		return "ExpectData [modular=" + modular + ", centent=" + centent
				+ ", regsource=" + regsource + "]";
	}

}
