package com.bangninjia.app.model;

import java.io.Serializable;

public class Alipay implements Serializable {

	private static final long serialVersionUID = 8117179153501226436L;

	private String partner;
	private String out_trade_no;
	private String subject;
	private String body;
	private String total_fee;
	private String notify_url;
	private String service;
	private String _input_charset;
	private String payment_type;
	private String seller_id;
	private String it_b_pay;

	public Alipay() {

	}

	public Alipay(String partner, String out_trade_no, String subject,
			String body, String total_fee, String notify_url, String service,
			String _input_charset, String payment_type, String seller_id,
			String it_b_pay) {
		super();
		this.partner = partner;
		this.out_trade_no = out_trade_no;
		this.subject = subject;
		this.body = body;
		this.total_fee = total_fee;
		this.notify_url = notify_url;
		this.service = service;
		this._input_charset = _input_charset;
		this.payment_type = payment_type;
		this.seller_id = seller_id;
		this.it_b_pay = it_b_pay;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String get_input_charset() {
		return _input_charset;
	}

	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getIt_b_pay() {
		return it_b_pay;
	}

	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}

	@Override
	public String toString() {
		return "Alipay [partner=" + partner + ", out_trade_no=" + out_trade_no
				+ ", subject=" + subject + ", body=" + body + ", total_fee="
				+ total_fee + ", notify_url=" + notify_url + ", service="
				+ service + ", _input_charset=" + _input_charset
				+ ", payment_type=" + payment_type + ", seller_id=" + seller_id
				+ ", it_b_pay=" + it_b_pay + "]";
	}

}
