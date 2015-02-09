package com.bangninjia.app.utils;

import java.io.File;

import android.os.Environment;

public class Constants {
	
	//umeng
	public static final String DESCRIPTOR = "com.umeng.share";

//	public static final String BASE_URL = "http://m.bangninjia.com/";
//	public static final String BASE_PHONE_URL = "http://mobiletest.bangninjia.com:1180/"; // 测试地址
	public static final String BASE_PHONE_URL = "http://mobile.bangninjia.com/"; // 正式地址
	public static final String BASE_IMAGE_URL = "http://pic.bangninjia.com/";
//	public static final String BANNER_URL = BASE_URL + "index/app-banner";// banner图
	public static final int GRID_TYPE_LINE = 525;
	public static final int GRID_TYPE_NORMAL = 526;

	public static final int BRAND_TYPE_FLOOR = 11;// 地板
	public static final int BRAND_TYPE_WALLPAPER = 12;// 壁纸
	public static final int BRAND_TYPE_WALL = 13;// 墙面

	//品牌地址
	public static final String BRAND_PDT_SERVER = BASE_PHONE_URL + "brandPdtServer";
	//踢脚线
	public static final String SKIRTING_BOARD_SERVER = BASE_PHONE_URL + "skirtingBoardServer";
	public static final int REQUEST_CODE_BRAND = 527;

	public static final String GET_COLOR_URL = BASE_PHONE_URL + "colorServer"; // 八大色系值

	public static final String PLEASE_WAIT_URL = BASE_PHONE_URL
			+ "pleaseWaitServer"; // 敬请期待
	public static final String REGIST_SMS_VALIDE = BASE_PHONE_URL
			+ "sendSMSServer"; // 获取短信验证码
	public static final String REGIST_SEND_INFO_URL = BASE_PHONE_URL
			+ "registServer"; // 发送注册信息
	public static final String LOGIN_SEND_INFO_URL = BASE_PHONE_URL
			+ "loginServer"; // 发送登录信息
	public static final String RESET_PASS_URL = BASE_PHONE_URL
			+ "resetPwdServer"; // 找回密码
	public static final String MODIFY_PASS_URL = BASE_PHONE_URL
			+ "modifyPasswordServer"; // 修改密码
	public static final String ORDER_CREATE_URL = BASE_PHONE_URL
			+ "createOrderServer"; // 创建订单
	public static final String ORDER_UPDATE_URL = BASE_PHONE_URL
			+ "updateOrderServer";// 修改订单

	public static final String ORDER_COMMENT_URL = BASE_PHONE_URL
			+ "listOrderCommentsServer"; // 获取订单评论信息
	public static final String GET_ORDER_LIST_URL = BASE_PHONE_URL
			+ "listOrdersServer"; // 获取订单列表信息
	public static final String GET_ORDER_DETAIL_URL = BASE_PHONE_URL
			+ "getOrderServer"; // 获取订单信息
	public static final String CANCEL_ORDER_URL = BASE_PHONE_URL
			+ "cancelOrderServer"; // 用户取消订单
	public static final String GET_ORDER_CANNL_INFO_URL = BASE_PHONE_URL
			+ "getOrderCancelInfoServer";// 获取取消订单信息
	public static final String EVAL_ORDER_URL = BASE_PHONE_URL
			+ "saveOrderCommentServer"; // 用户点评订单
	public static final String GET_ORDER_NODE_URL = BASE_PHONE_URL
			+ "getOrderNodeServer"; // 获取订单时间节点信息
	public static final String SAVE_USER_ADDRESS_URL = BASE_PHONE_URL
			+ "saveUserAddressServer"; // 用户提交服务地址
	public static final String GET_USER_ADDRESS_URL = BASE_PHONE_URL
			+ "listUserAddressServer"; // 获取用户地址列表
	public static final String SUGGESTION_FEED_BACK_URL = BASE_PHONE_URL
			+ "feedbackServer";// 意见反馈
	public static final String SUGGESTION_EXPECT_MORE_URL = BASE_PHONE_URL
			+ "pleaseWaitServer";// 敬请期待
	
	public static final String CHANGE_ORDER_URL = BASE_PHONE_URL
			+ "changeOrderServer";//修改订单状态
	
	public static final String EXIT_USER_URL = BASE_PHONE_URL
			+ "exitUserServer";//判断用户是否存在
	
	public static final String COUPON_URL = BASE_PHONE_URL
			+ "couponServer";//优惠码
	
	public static final String SMS_URL = BASE_PHONE_URL
			+ "smsServer";//快速预约获取短信验证码
	
	public static final String RESERVATION_URL = BASE_PHONE_URL
			+ "saveReservationServer";//快速预约
	
	public static final String SCHOOL_URL = BASE_PHONE_URL 
			+ "knowledgeSchoolServlet";//知识学堂
	
//	public static final String SCHOOL_URL = "http://192.168.1.32/bnjAppClient/knowledgeSchoolServlet";
	
	public static final String ALI_PAY_URL = "http://pay.bangninjia.com/pay/alipaySDKPayBack.jsp";
	
	//支付测试地址
//	public static final String PAY_SERVER_URL = "http://paytest.bangninjia.com:1580/bnjPay/pay/alipaySDKPay.jsp";
	//支付正式地址
	public static final String PAY_SERVER_URL = "http://pay.bangninjia.com/pay/alipaySDKPay.jsp";
	
	/**
	 * 下载apk存放路径
	 */
	public static final String UPGRADE_DOWNLOAD_PATH = Environment
			.getExternalStorageDirectory() + File.separator;
	/**
	 * apk文件名
	 */
	public static final String APK_NAME = "bangninjia.apk";

//	public static final String DOWNLOAD_URL = "http://gdown.baidu.com/data/wisegame/f98d235e39e29031/baiduxinwen.apk";
	/**
	 * 版本升级检查地址
	 */
	public static final String VERSION_EXAM_URL = BASE_PHONE_URL
			+ "versionServer";
	
//	/**
//	 * 判断是否5环之外
//	 * 
//	 * @param road
//	 * @return
//	 */
//	public static boolean is5RoadOrOutside(String road) {
//		if (road.equals("内环到三环里") || road.equals("内环到二环里")
//				|| road.equals("内环到二环") || road.equals("二环到三环")
//				|| road.equals("内环到三环里") || road.equals("三环以内")
//				|| road.equals("三环到四环之间") || road.equals("四环到五环之间")
//				|| road.equals("四环到五环内")
//				|| road.equals("石景山城区")
//				|| road.equals("八大处科技园区")
//				|| road.equals("城区以内")) {
//			return false;
//		}
//		return true;
//	}
	
	public static String orderStatus(int orderCode){
		String status = "";
		switch (orderCode) {
		case 100:
			status = "待付款";
			break;
		case 101:
			status = "待派单";
			break;
		case 102:
			status = "已派单";
			break;
		case 103:
			status = "用户已确认";
			break;
		case 104:
			status = "作业人员已确认";
			break;
		case 105:
			status = "施工完成";
			break;
		case 106:
			status = "已评价";
			break;
		case 200:
			status = "申请取消";
			break;
		case 201:
			status = "已取消";
			break;
		case 202:
			status = "取消失败";
			break;
		case 203:
			status = "派单失败";
			break;

		default:
			break;
		}
		return status;
	}
}
