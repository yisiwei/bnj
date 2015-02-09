package com.bangninjia.app.model;

import java.io.Serializable;

/**
 * 快速预约参数
 *
 */
public class ReservationData implements Serializable {

	private static final long serialVersionUID = -7595543500650042071L;

	private String name;// 服务类型
	private String receiverName;// 用户姓名
	private String receiverMobile;// 用户手机

	private String code;// 验证码

	public ReservationData() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ReservationData [name=" + name + ", receiverName="
				+ receiverName + ", receiverMobile=" + receiverMobile
				+ ", code=" + code + "]";
	}

}
