/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 *
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.bangninjia.app.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088112738535023";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "bangninjia@126.com";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAM99AHDnDbybtO/z/XqieV1Y1jHH3Y5m9XCGNXPMBSkIU5nzc/jLmrl034GnlaoB5q3OH2Ze99Tet816OmWfDqmCoZZIZ8g3cw8ntgM92lxwAd97Lh76NtwDQ5KN1slBw2ZUkszyvNq/VGyjqiE/270JwQWslo9qMHaPldpL/Z2tAgMBAAECgYBAYujtIDYaDjOCtrM1CBAjUHr/bacqFncZmPwjV+9OAWB4CJ03Wm9EF6l4AXonDHn5+1mxGc1MSGS/JavTdx1EG0uPyDc16BpSWbvlmKsPQgUKyAs6Fugyl11PowIDvf29geukQcKLVwhgLYHPd5hH1GO1UAF+cj/IsjIWFaGhoQJBAPtIjh8p3DBvAbgvh5fBEwjX0b6q95jkbbfdQZS4YgWFdoaP70Zq3KYw3dHVGgLJxWGxCSW2wEwoHuLzZjNwyeUCQQDTYgGOJYM0PcMkJVnEhy4S2/+mQb7WBU+mBWooUK61lUpajZuBj8xBZggrHkPU4iQB/wWOVCc8XdPaq9ctw6gpAkBH6Sya6HzV836XeiqgmCVdW33vxbeTrpNjkdMJv3Z1xAr2WUyNZ1l7yfJA8W4/LATrfyFyBImlgbnNEwDFadqtAkAr6EakbRxUxKN9JZkA3odueW4f7bYjJJVHygYj+6Zep3T7XEC559Gon/YAZtf2J0cNdxiDWO2Rd3fjCyC2K9cJAkBEQP43KekFeDdADP0QDNhG/bWfqWHfVim46EOxgJr8yw8Kr9SfinQfQZvPKPEFBvorK90/jtsukmwvEaaK+WZj";

	//支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	//回调接口
	public static final String NOTIFY_URL = "http://pay.bangninjia.com/pay/directPayAsynNotify.jsp" ;
	
	public static final String SERVICE_URL = "mobile.securitypay.pay" ;
	
	//有效时间
	public static final String PAY_TIME = "30m" ;
	
	public static final String PAYMENY_TYPE = "1" ;
	

}
