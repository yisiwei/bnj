package com.bangninjia.app.activity;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.adapter.SimpleListViewAdapter;
import com.bangninjia.app.alipay.Keys;
import com.bangninjia.app.alipay.PayOrder;
import com.bangninjia.app.alipay.Result;
import com.bangninjia.app.model.Address;
import com.bangninjia.app.model.Alipay;
import com.bangninjia.app.model.Coupon;
import com.bangninjia.app.model.DemandItem;
import com.bangninjia.app.model.Invoice;
import com.bangninjia.app.model.OrderCustomerJson;
import com.bangninjia.app.model.OrderData;
import com.bangninjia.app.model.OrderJson;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

/**
 * 确认订单
 * 
 */
public class OrderConfirmActivity extends BaseNetActivity implements
		OnClickListener, OnItemClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mAddAddressView;// 添加服务地址
	private LinearLayout mAddressLayout;// 服务地址Layout
	private TextView mConsigneeView;// 收件人
	private TextView mPhoneView;// 收件人电话
	private TextView mAddressView;// 收件人地址

	private Address mAddress;

	private LinearLayout mServiceInfoLayout;
	private TextView mServiceTypeView;// 服务类型
	private TextView mHouseInfoView;// 房屋信息
	private TextView mPaintingInfoView;// 涂料信息
	private TextView mColorInfoView;// 颜色信息

	private LinearLayout mServiceTypeLayout1;
	private TextView mServiceTypeText1;
	private TextView mServiceTypeText2;

	private LinearLayout mAppointmentTimeView;// 预约时间
	private TextView mDateTimeView;

	private String mDate;// 用于保存选择的日期
	private String mTime;// 用于保存选择的时间

	private AlertDialog dialog;
	private ListView datepicker;
	private ListView timepicker;
	private SimpleListViewAdapter dateAdapter;
	private SimpleListViewAdapter timeAdapter;
	private ArrayList<String> datepickerData;
	private ArrayList<String> timepickerData;

	private LinearLayout mInvoiceInfoView;// 发票信息
	private LinearLayout mIsInvoiceLayout;// 是否需要发票
	private TextView mIsInvoiceView;// 发票类型
	private TextView mInvoiceTypeView;// 发票抬头（0.个人/1.公司）
	private TextView mInvoiceContent;// 发票内容

	/**
	 * 显示费用View
	 */
	private TextView mPrice1View;// 主材费
	private TextView mPrice2View;// 人工费
	// private TextView mPrice3View;// 辅材费
	private TextView mPrice4View;// 搬运费
	private TextView mPrice5View;// 运输费
	private TextView mPrice6View;// 踢脚线费
	// private TextView mPrice7View;// 壁纸胶费
	private TextView mPrice8View;// 拆除费
	// private TextView mPrice9View;// 测量费
	private TextView mPrice10View;// 折扣金额

	private EditText mCouponEt;// 优惠码
	private Button mCouponBtn;

	private float mPrice5 = 0;// 运输费
	private float mPrice10 = 0;// 折扣金额
	private float mTotalPrice;// 总价
	// private float mUnitPrice;//单价
	// private float mAmountPayable;//应付金额

	private LinearLayout mPrice3Layout;
	private LinearLayout mPrice4Layout;
	private LinearLayout mPrice6Layout;
	// private LinearLayout mPrice7Layout;
	private LinearLayout mPrice8Layout;
	private LinearLayout mPrice10Layout;

	private TextView mUnitPriceView;// 单价
	private TextView mTotalMoneyView;// 总金额
	private TextView mAmountPayableView;// 应付金额
	private TextView mPriceHintText;

	private Button mPayBtn;// 付款按钮
	private static String mOrderId;// 订单号

	private static final int ADDRESS_CODE = 2000;// 地址requestCode
	private static final int CREATE_ORDER_CODE = 2001;// 订单requestCode
	private static final int PAY_ORDER_CODE = 2002;// 付款前调用接口
	private static final int COUPON_ORDER_CODE = 2003;// 优惠码

	private static final int INVOICE_REQUEST_CODE = 1000;// 发票信息

	private static final int ADD_ADDRESS_REQUEST_CODE = 3000;// 添加服务地址requestCode
	private static final int EDIT_ADDRESS_REQUEST_CODE = 3001;// 修改服务地址requestCode

	private OrderJson mOrderJson;
	private DemandItem mDemandItem;// 主材费
	private DemandItem mDemandItem2;// 人工安装费
	// private DemandItem mDemandItem3;// 辅材费
	private DemandItem mDemandItem4;// 搬运费
	private DemandItem mDemandItem5;// 运输费
	private DemandItem mDemandItem6;// 踢脚线费
	// private DemandItem mDemandItem7;// 壁纸胶费
	private DemandItem mDemandItem8;// 拆除费
	// private DemandItem mDemandItem9;// 测量费
	private DemandItem mDemandItem10;// 折扣金额

	private static Format mFormat = new DecimalFormat("0.00");

	private String mType;// 服务类型

	private MyProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);

		initView();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.confirm_order);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		// 收货地址
		mAddAddressView = (TextView) this
				.findViewById(R.id.order_confirm_add_address);
		mAddAddressView.setOnClickListener(this);

		mAddressLayout = (LinearLayout) this
				.findViewById(R.id.order_confirm_address_layout);
		mConsigneeView = (TextView) this
				.findViewById(R.id.order_confirm_consignee);
		mPhoneView = (TextView) this.findViewById(R.id.order_confirm_phone);
		mAddressView = (TextView) this.findViewById(R.id.order_confirm_address);

		// 服务信息
		mServiceInfoLayout = (LinearLayout) this
				.findViewById(R.id.order_confirm_serviceinfo);
		mServiceInfoLayout.setOnClickListener(this);
		mServiceTypeView = (TextView) this
				.findViewById(R.id.order_confirm_servicetype);
		mHouseInfoView = (TextView) this
				.findViewById(R.id.order_confirm_house_info);
		mPaintingInfoView = (TextView) this
				.findViewById(R.id.order_confirm_painting_info);
		mColorInfoView = (TextView) this
				.findViewById(R.id.order_confirm_color_info);

		mServiceTypeLayout1 = (LinearLayout) this
				.findViewById(R.id.service_type_layout1);
		mServiceTypeText1 = (TextView) this
				.findViewById(R.id.service_type_text1);
		mServiceTypeText2 = (TextView) this
				.findViewById(R.id.service_type_text2);

		// 预约时间
		mAppointmentTimeView = (LinearLayout) this
				.findViewById(R.id.order_confirm_appointment_time);
		mDateTimeView = (TextView) this
				.findViewById(R.id.order_confirm_app_datetime);

		// 发票信息
		mInvoiceInfoView = (LinearLayout) this
				.findViewById(R.id.order_confirm_invoice_info);
		mIsInvoiceLayout = (LinearLayout) this
				.findViewById(R.id.order_confirm_invoice_layout);
		mIsInvoiceView = (TextView) this
				.findViewById(R.id.order_confirm_is_invoice);
		mInvoiceTypeView = (TextView) this
				.findViewById(R.id.order_confirm_invoice_type);
		mInvoiceContent = (TextView) this
				.findViewById(R.id.order_confirm_invoice_content);

		mAppointmentTimeView.setOnClickListener(this);
		mInvoiceInfoView.setOnClickListener(this);

		// 金额信息
		mCouponEt = (EditText) this.findViewById(R.id.order_confirm_coupon_et);// 优惠码
		mCouponBtn = (Button) this.findViewById(R.id.order_confirm_coupon_btn);// 优惠码btn
		mCouponBtn.setOnClickListener(this);
		mPrice1View = (TextView) this.findViewById(R.id.order_confirm_price1);
		mPrice2View = (TextView) this.findViewById(R.id.order_confirm_price2);
		// mPrice3View = (TextView)
		// this.findViewById(R.id.order_confirm_price3);
		mPrice4View = (TextView) this.findViewById(R.id.order_confirm_price4);
		mPrice5View = (TextView) this.findViewById(R.id.order_confirm_price5);
		mPrice6View = (TextView) this.findViewById(R.id.order_confirm_price6);
		// mPrice7View = (TextView)
		// this.findViewById(R.id.order_confirm_price7);
		mPrice8View = (TextView) this.findViewById(R.id.order_confirm_price8);
		// mPrice9View = (TextView)
		// this.findViewById(R.id.order_confirm_price9);
		mPrice10View = (TextView) this.findViewById(R.id.order_confirm_price10);

		mPrice3Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price3_layout);
		mPrice4Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price4_layout);
		mPrice6Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price6_layout);
		// mPrice7Layout = (LinearLayout) this
		// .findViewById(R.id.order_confirm_price7_layout);
		mPrice8Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price8_layout);
		mPrice10Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price10_layout);

		mUnitPriceView = (TextView) this
				.findViewById(R.id.order_confirm_unit_price);
		mTotalMoneyView = (TextView) this
				.findViewById(R.id.order_confirm_total_money);
		mAmountPayableView = (TextView) this
				.findViewById(R.id.order_confirm_amount_payable);
		mPriceHintText = (TextView) this
				.findViewById(R.id.order_confirm_price_hint);

		mPayBtn = (Button) this.findViewById(R.id.order_confirm_pay_btn);
		mPayBtn.setOnClickListener(this);

		mAddressLayout.setOnClickListener(this);
		mPrice3Layout.setVisibility(View.GONE);
		mPrice4Layout.setVisibility(View.GONE);
		mPrice6Layout.setVisibility(View.GONE);

		/**************** 预约时间 *************/
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 2);// 当前日期+2（2天后）

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		StringBuilder date = new StringBuilder();
		date.append(year);
		if (month < 10) {
			date.append("-0").append(month);
		} else {
			date.append("-").append(month);
		}

		if (dayOfMonth < 10) {
			date.append("-0").append(dayOfMonth);
		} else {
			date.append("-").append(dayOfMonth);
		}
		date.append(" 10:00");

		mDateTimeView.setText(date.toString());// 设置默认预约时间

		mPriceHintText.setText("提示：应付定金为总金额的30%，若总金额的30%不足300元，则按300元收取。");

		Intent intent = getIntent();
		// 传过来的订单信息
		mType = intent.getStringExtra("type");// 类型（墙面更新、地板更新、壁纸更新）
		mOrderJson = (OrderJson) intent.getSerializableExtra("orderJson");
		mDemandItem = (DemandItem) intent.getSerializableExtra("demandItem");
		mDemandItem2 = (DemandItem) intent.getSerializableExtra("demandItem2");
		// mDemandItem3 = (DemandItem)
		// intent.getSerializableExtra("demandItem3");
		mDemandItem4 = (DemandItem) intent.getSerializableExtra("demandItem4");
		mDemandItem5 = (DemandItem) intent.getSerializableExtra("demandItem5");
		mDemandItem8 = (DemandItem) intent.getSerializableExtra("demandItem8");
		// mDemandItem9 = (DemandItem)
		// intent.getSerializableExtra("demandItem9");

		mServiceTypeView.setText(mOrderJson.getDemandProperties().get服务类型());
		String houseInfo = "";

		// 计算金额
		double price1 = Double.valueOf(mDemandItem.getPrice())
				* mDemandItem.getQuantity();
		double price2 = Double.valueOf(mDemandItem2.getPrice())
				* mDemandItem2.getQuantity();
		double price8 = Double.valueOf(mDemandItem8.getPrice())
				* mDemandItem8.getQuantity();
		mPrice1View.setText("￥" + mFormat.format(price1));// 主材费
		mPrice2View.setText("￥" + mFormat.format(price2));// 人工费
		// mPrice3View.setText("￥0.00");// 辅材费
		mPrice4View.setText("￥0.00");// 搬运费
		mPrice5View.setText("￥0.00");// 运输费
		mPrice8View.setText("￥" + mFormat.format(price8));// 拆除费
		if (price8 <= 0) {
			mPrice8Layout.setVisibility(View.GONE);
		}
		// mPrice9View.setText("￥0.00");// 测量费

		if (mType.equals("wall")) {// 墙面更新

			mPaintingInfoView.setText(mDemandItem.getProductCode() + "  "
					+ mDemandItem.getName());// 涂料信息
			mColorInfoView.setText(mDemandItem.getMemo());// 颜色信息

			houseInfo = mOrderJson.getDemandSpace() + "平方米【"
					+ mOrderJson.getDemandProperties().get现状信息() + "】+"
					+ mOrderJson.getDemandProperties().get电梯信息();

			mTotalPrice = Float.parseFloat(mFormat.format(price1))
					+ Float.parseFloat(mFormat.format(price2))
					+ Float.parseFloat(mFormat.format(price8));
			mUnitPriceView.setText("(单价：￥"
					+ mFormat.format(mTotalPrice
							/ Double.valueOf(mOrderJson.getDemandSpace()))
					+ "/平方米)");// 单价

			mTotalMoneyView.setText("￥" + mFormat.format(mTotalPrice));// 总金额

			double amountPay = mTotalPrice * 0.3;
			if (amountPay < 300) {
				amountPay = 300;
			}
			mAmountPayableView.setText("￥" + mFormat.format(amountPay));// 应付金额

		} else if (mType.equals("floor")) {// 地板更新

			mDemandItem6 = (DemandItem) intent
					.getSerializableExtra("demandItem6");

			houseInfo = mOrderJson.getDemandSpace() + "平方米+"
					+ mOrderJson.getDemandProperties().get电梯信息();

			mServiceTypeText1.setText("地板信息：");
			mServiceTypeText2.setText("踢脚线信息：");

			mPaintingInfoView.setText(mDemandItem.getMemo() + "  "
					+ mDemandItem.getName());// 地板信息
			mColorInfoView.setText(mDemandItem6.getName());// 踢脚线信息

			mPrice6Layout.setVisibility(View.VISIBLE);

			double price4 = Double.valueOf(mDemandItem4.getPrice())
					* mDemandItem4.getQuantity();
			mPrice4View.setText("￥" + mFormat.format(price4));// 搬运费
			if (price4 > 0) {
				mPrice4Layout.setVisibility(View.VISIBLE);
			}

			double price6 = Double.valueOf(mDemandItem6.getPrice())
					* mDemandItem6.getQuantity();
			mPrice6View.setText("￥" + mFormat.format(price6));// 踢脚线费

			mTotalPrice = Float.parseFloat(mFormat.format(price1))
					+ Float.parseFloat(mFormat.format(price2))
					+ Float.parseFloat(mFormat.format(price4))
					+ Float.parseFloat(mFormat.format(price6))
					+ Float.parseFloat(mFormat.format(price8));

			mTotalMoneyView.setText("￥" + mFormat.format(mTotalPrice));// 总金额
			mUnitPriceView.setText("(单价：￥"
					+ mFormat.format(mTotalPrice
							/ Double.valueOf(mOrderJson.getDemandSpace()))
					+ "/平方米)");// 单价
			double amountPay = mTotalPrice * 0.3;
			if (amountPay < 300) {
				amountPay = 300;
			}
			mAmountPayableView.setText("￥" + mFormat.format(amountPay));// 应付金额

		} else {// 壁纸更新
			houseInfo = mOrderJson.getDemandSpace() + "平方米+"
					+ mOrderJson.getDemandProperties().get电梯信息();

			mServiceTypeText2.setText("壁纸信息：");
			mServiceTypeLayout1.setVisibility(View.GONE);
			mColorInfoView.setText(mDemandItem.getMemo() + " "
					+ mDemandItem.getName());// 壁纸信息

			mTotalPrice = Float.parseFloat(mFormat.format(price1))
					+ Float.parseFloat(mFormat.format(price2))
					+ Float.parseFloat(mFormat.format(price8));
			mUnitPriceView.setText("(单价：￥"
					+ mFormat.format(mTotalPrice
							/ Double.valueOf(mOrderJson.getDemandSpace()))
					+ "/平方米)");// 单价

			mTotalMoneyView.setText("￥" + mFormat.format(mTotalPrice));// 总金额
			double amountPay = mTotalPrice * 0.3;
			if (amountPay < 300) {
				amountPay = 300;
			}
			mAmountPayableView.setText("￥" + mFormat.format(amountPay));// 应付金额

		}

		if (mOrderJson.getDemandProperties().get电梯信息().equals("无电梯")) {
			houseInfo = houseInfo + "+"
					+ mOrderJson.getDemandProperties().get楼层信息();
		}
		mHouseInfoView.setText(houseInfo);

		// 获取服务地址
		Map<String, String> params = new HashMap<String, String>();
		String dataString = "{ userId :'" + MyApplication.getUser().getUserId()
				+ "'}";
		params.put("data", dataString);
		post(ADDRESS_CODE, Constants.GET_USER_ADDRESS_URL, params);
		mProgressDialog = new MyProgressDialog(this, "正在加载");

		Log.i("订单Id===============" + mOrderId);

	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		Log.i("MainActivity", "网络请求开始");
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		mProgressDialog.dismiss();
		Log.i("MainActivity", jsonData);
		if (requestCode == ADDRESS_CODE) {// 服务地址
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 2000) {
					JSONArray array = jsonObject.optJSONObject("result")
							.optJSONArray("result");
					TypeToken<List<Address>> typeToken = new TypeToken<List<Address>>() {
					};
					List<Address> addresses = MyApplication.getGson().fromJson(
							array.toString(), typeToken.getType());
					if (addresses != null && addresses.size() > 0) {
						mAddress = addresses.get(0);
						showAddress(mAddress);
					} else {
						mAddAddressView.setVisibility(View.VISIBLE);
						mAddressLayout.setVisibility(View.GONE);
					}
				} else {
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == CREATE_ORDER_CODE) {// 创建订单
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 2000) {
					String orderId = jsonObject.getString("orderId");
					Toast.show(this, "订单：" + orderId + " 创建成功");
					mOrderId = orderId;
					// 支付前调用服务器数据
					getPayData();
				} else {
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == PAY_ORDER_CODE) {// 获取支付数据
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				String result = jsonObject.getString("retmsg");
				Log.i("result==" + result);
				if (result != null) {

					String[] results = result.split("&");
					Map<String, String> map = new HashMap<String, String>();
					for (int i = 0; i < results.length; i++) {
						String[] str = results[i].split("=");
						map.put(str[0], str[1]);
					}
					Log.i("" + map.get("body") + "--" + map.get("notify_url"));

					Alipay alipay = new Alipay();
					alipay.setBody(map.get("body").substring(1,
							map.get("body").length() - 1));
					alipay.set_input_charset("utf-8");
					alipay.setSubject(map.get("subject").substring(1,
							map.get("subject").length() - 1));

					alipay.setIt_b_pay(map.get("it_b_pay").substring(1,
							map.get("it_b_pay").length() - 1));
					alipay.setNotify_url(map.get("notify_url").substring(1,
							map.get("notify_url").length() - 1));
					alipay.setOut_trade_no(map.get("out_trade_no").substring(1,
							map.get("out_trade_no").length() - 1));

					alipay.setPartner(Keys.DEFAULT_PARTNER);
					alipay.setPayment_type(Keys.PAYMENY_TYPE);
					alipay.setSeller_id(Keys.DEFAULT_SELLER);
					alipay.setService(Keys.SERVICE_URL);
					// 支付金额
					alipay.setTotal_fee(map.get("total_fee").substring(1,
							map.get("total_fee").length() - 1));// 支付金额
					// alipay.setTotal_fee("0.01");// 支付金额
					Log.i("支付参数：" + alipay.toString());
					// 去支付
					PayOrder payOrder = new PayOrder(this, alipay);
					payOrder.pay();

				} else {
					Toast.show(this, "网络错误，请稍后再试");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == COUPON_ORDER_CODE) { // 优惠码
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				if (code == 2000) {

					JSONObject couponJson = jsonObject.optJSONObject("coupon");
					Coupon coupon = MyApplication.getGson().fromJson(
							couponJson.toString(), Coupon.class);
					Toast.show(this, "可优惠" + coupon.getAmount() + "元");
					Log.i("" + coupon);
					mPrice10Layout.setVisibility(View.VISIBLE);
					mPrice10 = coupon.getAmount();
					mPrice10View.setText("￥" + mFormat.format(mPrice10));

					mTotalMoneyView.setText("￥"
							+ mFormat.format(mTotalPrice + mPrice5 - mPrice10));// 总金额

					double amountPay = (mTotalPrice + mPrice5 - mPrice10) * 0.3;
					if (amountPay < 300) {
						amountPay = 300;
					}
					mAmountPayableView.setText("￥" + mFormat.format(amountPay));// 应付金额

					mUnitPriceView.setText("(单价：￥"
							+ mFormat.format((mTotalPrice + mPrice5 - mPrice10)
									/ Double.valueOf(mOrderJson
											.getDemandSpace())) + "/平方米)");// 单价

				} else {
					String msg = jsonObject.getString("msg");
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onNetworkFinish(int requestCode) {
		Log.i("MainActivity", "网络请求完成");
	}

	/**
	 * 显示收货地址
	 * 
	 * @param address
	 */
	private void showAddress(Address address) {
		mAddAddressView.setVisibility(View.GONE);
		mAddressLayout.setVisibility(View.VISIBLE);
		Log.i("MainActivity", address.toString());
		// 显示收货地址
		mConsigneeView.setText(address.getName());
		mPhoneView.setText(address.getMobile());
		String detailAddress = address.getProvince() + address.getCity()
				+ address.getArea() + address.getRoad() + address.getAddress();
		mAddressView.setText(String.format(
				getResources().getString(R.string.shipping_address),
				detailAddress, 1));

		int index = address.getRoad().indexOf("*");// 判断是否是五环以外
		if (index != -1) {
			mPrice5 = 300;
		} else {
			mPrice5 = 0;
		}
		mPrice5View.setText("￥" + mFormat.format(mPrice5));// 设置运输费

		mTotalMoneyView.setText("￥"
				+ mFormat.format(mTotalPrice + mPrice5 - mPrice10));// 总金额

		double amountPay = (mTotalPrice + mPrice5 - mPrice10) * 0.3;
		if (amountPay < 300) {
			amountPay = 300;
		}
		mAmountPayableView.setText("￥" + mFormat.format(amountPay));// 应付金额

		mUnitPriceView.setText("(单价：￥"
				+ mFormat.format((mTotalPrice + mPrice5 - mPrice10)
						/ Double.valueOf(mOrderJson.getDemandSpace()))
				+ "/平方米)");// 单价
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("MainActivity", "回传");
		if (requestCode == INVOICE_REQUEST_CODE) {// 发票信息
			if (resultCode == RESULT_OK) {
				Invoice invoice = (Invoice) data
						.getSerializableExtra("invoice");
				if (invoice.getType().equals("普通发票")) {
					mIsInvoiceLayout.setVisibility(View.VISIBLE);
					mIsInvoiceView.setText(invoice.getType());
					if (invoice.getTitle().equals("个人")) {
						mInvoiceTypeView.setText(invoice.getTitle());
					} else {
						mInvoiceTypeView.setText(invoice.getUnitName());
					}
					mInvoiceContent.setText(invoice.getContent());
				}
			}
		} else if (requestCode == ADD_ADDRESS_REQUEST_CODE) {// 添加地址
			if (resultCode == RESULT_OK) {
				mAddress = (Address) data.getSerializableExtra("Address");
				showAddress(mAddress);
			}
		} else if (requestCode == EDIT_ADDRESS_REQUEST_CODE) {// 修改地址
			if (resultCode == RESULT_OK) {
				mAddress = (Address) data.getSerializableExtra("Address");
				showAddress(mAddress);
			}
		}
	}

	/**
	 * 获取服务器支付数据
	 */
	private void getPayData() {
		Map<String, String> params = new HashMap<String, String>();

		String userId = MyApplication.getUser().getUserId();
		String type = "10";// 10-付定金 20-付尾款
//		 String money = "0.01";
		String money = mAmountPayableView.getText().toString().substring(1);

		params.put("data", mOrderId + "|" + money + "|" + userId + "|" + type);
		Log.i(mOrderId + "|" + money + "|" + userId + "|" + type);
		post(PAY_ORDER_CODE, Constants.PAY_SERVER_URL, params);
	}

	/**
	 * 去付款
	 */
	// private void payOrder() {
	// Alipay alipay = new Alipay();
	// alipay.set_input_charset("utf-8");
	// alipay.setBody(mServiceTypeView.getText().toString());
	// alipay.setSubject("帮您家下单支付");
	// alipay.setIt_b_pay("5m");
	//
	// alipay.setNotify_url(Constants.ALI_PAY_URL);
	//
	// // if (handleType == TYPE_MODIFY_ORDER) {
	// // alipay.setOut_trade_no(orderId + "-20");// 订单号
	// // } else {
	// // }
	// alipay.setOut_trade_no(mOrderId);// 订单号
	// alipay.setPartner(Keys.DEFAULT_PARTNER);
	// alipay.setPayment_type("1");
	// alipay.setSeller_id(Keys.DEFAULT_SELLER);
	// alipay.setService("mobile.securitypay.pay");
	// alipay.setTotal_fee("0.01");// 支付金额
	// Log.i("MainActivity", "支付金额："
	// + mAmountPayableView.getText().toString().substring(1));
	// // alipay.setTotal_fee(mFormat.format((mTotalPrice + mPrice5) * 0.3));//
	// // 支付金额
	//
	// PayOrder payOrder = new PayOrder(this, alipay);
	// payOrder.pay();
	// }

	/**
	 * 付款回调
	 * 
	 * @param result
	 * @param activity
	 */
	public static void getpayResult(String result, Activity activity,
			String orderId) {
		Log.i("MainActivity", "付款来了 " + result);
		Log.i("MainActivity", "付款订单Id:" + orderId);

		Result resultbean = new Result(result);
		resultbean.parseResult();

		if ("支付成功(9000)".equals(resultbean.resultStatus)) {
			Log.i("付款" + resultbean.resultStatus);
			Toast.show(activity, "付款成功");
			Intent intent = new Intent(activity, PaySuccessActivity.class);
			intent.putExtra("orderId", orderId);
			activity.startActivity(intent);
			// activity.finish();

		} else {
			Toast.show(activity, "付款失败");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.order_confirm_add_address:// 添加服务地址
			Intent addServiceIntent = new Intent(OrderConfirmActivity.this,
					EditAddressActivity.class);
			addServiceIntent.putExtra("option", "confirmOrder");
			startActivityForResult(addServiceIntent, ADD_ADDRESS_REQUEST_CODE);
			break;
		case R.id.order_confirm_address_layout:// 选择服务地址
			Intent editServiceIntent = new Intent(OrderConfirmActivity.this,
					AddressManageActivity.class);
			editServiceIntent.putExtra("option", "confirmOrder");
			startActivityForResult(editServiceIntent, EDIT_ADDRESS_REQUEST_CODE);
			break;
		case R.id.order_confirm_serviceinfo:// 服务信息
			this.finish();
			break;
		case R.id.order_confirm_appointment_time:// 预约时间
			fillAdapterData();
			DatetimePickerDialog();
			break;
		case R.id.order_confirm_invoice_info:// 发票信息
			Intent intent = new Intent(OrderConfirmActivity.this,
					InvoiceActivity.class);
			startActivityForResult(intent, INVOICE_REQUEST_CODE);
			break;
		case R.id.order_confirm_coupon_btn:// 使用优惠码
			useCoupon();
			break;
		case R.id.order_confirm_pay_btn:// 去付款

			if (!MyApplication.getComeFrom().equals("1")) {
				Toast.show(this, "您没有下单权限");
				return;
			}

			if (mAddress == null) {
				Toast.show(this, "请添加服务地址");
				return;
			}

			goPay();

			break;
		default:
			break;
		}
	}

	/**
	 * 使用优惠码
	 */
	private void useCoupon() {
		String code = mCouponEt.getText().toString();
		if (StringUtils.isNullOrEmpty(code)) {
			Toast.show(this, "请输入优惠码");
			return;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		JSONObject obj = new JSONObject();
		try {
			obj.put("code", code);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.put("data", obj.toString());
		post(COUPON_ORDER_CODE, Constants.COUPON_URL, params);
	}

	/**
	 * 去付款
	 */
	private void goPay() {
		// 创建订单
		Log.i("mOrderId==" + mOrderId);
		if (StringUtils.isNullOrEmpty(mOrderId)) {
			Map<String, String> params = new HashMap<String, String>();
			OrderData data = new OrderData();

			OrderCustomerJson customerJson = new OrderCustomerJson();
			customerJson.setAddress(mAddress.getAddress());// 具体地址
			customerJson.setArea(mAddress.getArea());// 城区
			customerJson.setCity(mAddress.getCity());// 城市
			customerJson.setMobile(mAddress.getMobile());// 手机
			customerJson.setName(mAddress.getName());// 用户姓名
			customerJson.setPhone(mAddress.getMobile());// 用户手机
			customerJson.setProvince(mAddress.getProvince());
			customerJson.setRoad(mAddress.getRoad());

			data.setCustomerJson(customerJson);

			OrderJson orderJson = new OrderJson();
			orderJson.setAppointmentDate(mDateTimeView.getText().toString()); // 预约时间
			orderJson.setBrandId(mOrderJson.getBrandId());// 品牌Id

			List<DemandItem> demandItems = new ArrayList<DemandItem>();

			demandItems.add(mDemandItem);// 主材费
			demandItems.add(mDemandItem2);// 人工费
			// demandItems.add(mDemandItem3);// 辅材费

			DemandItem demandItem5 = mDemandItem5;
			demandItem5.setPrice(mFormat.format(mPrice5));
			demandItems.add(demandItem5);// 运输费
			demandItems.add(mDemandItem8);// 拆除费

			if (mType.equals("floor")) {
				demandItems.add(mDemandItem4);// 搬运费
				demandItems.add(mDemandItem6);// 踢脚线费
			}

			if (mPrice10 > 0) {
				mDemandItem10 = new DemandItem();// 折扣金额
				mDemandItem10.setType(10);
				mDemandItem10.setQuantity(1);
				mDemandItem10.setPrice(mFormat.format(mPrice10));
				mDemandItem10.setProductCode(mDemandItem.getProductCode());
				mDemandItem10.setProductId(mDemandItem.getProductId());
				mDemandItem10.setBrandId(mDemandItem.getBrandId());
				demandItems.add(mDemandItem10);
			}

			orderJson.setDemandItems(demandItems);

			orderJson.setDemandProperties(mOrderJson.getDemandProperties());
			orderJson.setDemandSpace(mOrderJson.getDemandSpace());
			orderJson.setDemandType(mOrderJson.getDemandType());
			Log.i("MainActivity",
					"总金额："
							+ Double.valueOf(mTotalMoneyView.getText()
									.toString().substring(1)));
			orderJson.setEstimatePrice(mTotalMoneyView.getText().toString()
					.substring(1));// 总金额
			orderJson.setInvoiceMemo(mInvoiceTypeView.getText().toString());// 发票详情
			String typeStr = mInvoiceTypeView.getText().toString();
			String type = "0";
			if (typeStr.equals("单位")) {
				type = "1";
			}
			orderJson.setInvoiceType(type);
			String isInvoiceStr = mIsInvoiceView.getText().toString();
			int isInvoice = 0;
			if (isInvoiceStr.equals("普通发票")) {
				isInvoice = 1;
			}
			orderJson.setIsInvoice(isInvoice);
			orderJson.setSourceId(mOrderJson.getSourceId());
			orderJson.setUserId(MyApplication.getUser().getUserId());
			orderJson.setUserLevel(0);// 用户等级
			orderJson.setUserName(MyApplication.getUser().getUserName());
			orderJson.setServiceName(mOrderJson.getServiceName());
			orderJson.setProductImages(mOrderJson.getProductImages());

			data.setOrderJson(orderJson);

			String dataString = MyApplication.getGson().toJson(data);
			params.put("data", dataString);
			Log.i("OrderData==" + dataString);

			post(CREATE_ORDER_CODE, Constants.ORDER_CREATE_URL, params);
		} else {// 如果已经订单号已经创建直接去付款
			// payOrder();
			getPayData();
		}
	}

	/**
	 * 该方法用于生成日期时间选择的 ListView 的数据源
	 * 
	 */
	private void fillAdapterData() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		datepickerData = new ArrayList<String>();
		timepickerData = new ArrayList<String>();

		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 2);// 让日期加2(2天后)

		for (int i = 0; i < 7; i++) {

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

			StringBuilder date = new StringBuilder();
			date.append(year);
			if (month < 10) {
				date.append("-0").append(month);
			} else {
				date.append("-").append(month);
			}

			if (dayOfMonth < 10) {
				date.append("-0").append(dayOfMonth);
			} else {
				date.append("-").append(dayOfMonth);
			}

			Log.i("MainActivity", "星期：" + calendar.get(Calendar.DAY_OF_WEEK));
			date.append("(")
					.append(getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
					.append(")");

			datepickerData.add(date.toString());
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + 1);// 让日期加1
		}

		for (int i = 8; i < 19; i++) {
			String time = null;
			if (i < 10) {
				time = "0" + i + ":";
			} else {
				time = i + ":";
			}
			timepickerData.add(time + "00");
			timepickerData.add(time + "30");
		}
	}

	/**
	 * 该方法用于将一个 1-7 的数值转换成 星期x 的表达形式
	 * 
	 * @param dayNum
	 * @return
	 */
	private String getDayOfWeek(int dayNum) {
		switch (dayNum) {
		case 1:
			return "周日";

		case 2:
			return "周一";

		case 3:
			return "周二";

		case 4:
			return "周三";

		case 5:
			return "周四";

		case 6:
			return "周五";

		case 7:
			return "周六";
		}
		return null;
	}

	/**
	 * 显示一个自定义的日期时间选择对话框
	 */
	private void DatetimePickerDialog() {
		View dialogView = View.inflate(this, R.layout.dialog_timepicker, null);
		datepicker = (ListView) dialogView
				.findViewById(R.id.dialog_timepicker_date);
		timepicker = (ListView) dialogView
				.findViewById(R.id.dialog_timepicker_time);

		TextView confirmBtn = (TextView) dialogView
				.findViewById(R.id.datetime_confimr_btn);
		TextView cancelBtn = (TextView) dialogView
				.findViewById(R.id.datetime_cancel_btn);

		dateAdapter = new SimpleListViewAdapter(this, datepickerData);
		datepicker.setAdapter(dateAdapter);

		timeAdapter = new SimpleListViewAdapter(this, timepickerData);
		timepicker.setAdapter(timeAdapter);

		datepicker.setOnItemClickListener(this);
		timepicker.setOnItemClickListener(this);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(dialogView);

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isNullOrEmpty(mDate)
						&& !StringUtils.isNullOrEmpty(mTime)) {
					mDateTimeView.setText(mDate.substring(0, 10) + " " + mTime);
					dialog.dismiss();
				} else {
					Toast.show(OrderConfirmActivity.this, "请选择日期和时间");
				}
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog = builder.create();
		dialog.show();
	}

	// TODO 待优化
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.dialog_timepicker_date:
			dateAdapter.setData(datepickerData, position);
			dateAdapter.notifyDataSetChanged();
			mDate = datepickerData.get(position);
			Log.i("MainActivity", "日期：" + mDate);
			break;
		case R.id.dialog_timepicker_time:
			timeAdapter.setData(timepickerData, position);
			timeAdapter.notifyDataSetChanged();
			mTime = timepickerData.get(position);
			Log.i("MainActivity", "时间：" + mTime);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mOrderId = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderConfirmActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderConfirmActivity");
		MobclickAgent.onPause(this);
	}

}
