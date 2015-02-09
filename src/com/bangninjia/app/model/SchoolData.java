package com.bangninjia.app.model;

import java.io.Serializable;

public class SchoolData implements Serializable {

	private static final long serialVersionUID = 2589002980693957134L;

	private String typeId;
	private String isPage;
	private Integer startRow;
	private Integer rowCount;

	public SchoolData() {

	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getIsPage() {
		return isPage;
	}

	public void setIsPage(String isPage) {
		this.isPage = isPage;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	@Override
	public String toString() {
		return "SchoolData [typeId=" + typeId + ", isPage=" + isPage
				+ ", startRow=" + startRow + ", rowCount=" + rowCount + "]";
	}

}
