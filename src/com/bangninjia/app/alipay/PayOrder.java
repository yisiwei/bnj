package com.bangninjia.app.alipay;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.android.app.sdk.AliPay;
import com.bangninjia.app.activity.OrderConfirmActivity;
import com.bangninjia.app.model.Alipay;
import com.bangninjia.app.utils.Log;

public class PayOrder {
	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	// private static final int RQF_LOGIN = 2;

	private Activity activity;
	private Alipay alipay;
	private String alipayResult = null;

	public PayOrder(Activity activity, Alipay alipay) {
		this.activity = activity;
		this.alipay = alipay;
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			Log.i("======"+alipay.getOut_trade_no());
			OrderConfirmActivity.getpayResult(result, activity,alipay.getBody());

		};
	};

	@SuppressWarnings("deprecation")
	public String pay() {

		try {
			String info = getNewOrderInfo();
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(activity, mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);
					alipayResult = result;

					Log.e(TAG, "付款结果 " + result);

					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return alipayResult;
	}

	private String getNewOrderInfo() {

		Map<String, String> map = new HashMap<String, String>();

		map.put("partner", alipay.getPartner());
		map.put("out_trade_no", alipay.getOut_trade_no());// 订单号
		map.put("subject", alipay.getSubject());// 主题
		map.put("body", alipay.getBody());// 订单描述
		map.put("total_fee", alipay.getTotal_fee());// 总金额小数点两位
		map.put("notify_url", alipay.getNotify_url());// 回调地址
		map.put("service", alipay.getService());
		map.put("_input_charset", alipay.get_input_charset());
		map.put("payment_type", alipay.getPayment_type());
		map.put("seller_id", alipay.getSeller_id());
		map.put("it_b_pay", alipay.getIt_b_pay());

		Set<String> keySet = map.keySet();

		StringBuffer sb = new StringBuffer();
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String next = iterator.next();
			sb.append(next);
			sb.append("=\"");
			sb.append(map.get(next));
			sb.append("\"&");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.toString();
	}

	// private String getOutTradeNo() {
	// SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
	// Locale.getDefault());
	// Date date = new Date();
	// String key = format.format(date);
	//
	// java.util.Random r = new java.util.Random();
	// key += r.nextInt();
	// key = key.substring(0, 15);
	// Log.d(TAG, "outTradeNo: " + key);
	// return key;
	// }

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
