package com.bangninjia.app.activity;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.adapter.SimpleListViewAdapter;
import com.bangninjia.app.alipay.Keys;
import com.bangninjia.app.alipay.PayOrder;
import com.bangninjia.app.model.Alipay;
import com.bangninjia.app.model.DemandItem;
import com.bangninjia.app.model.DemandProperties;
import com.bangninjia.app.model.Invoice;
import com.bangninjia.app.model.OrderCustomerJson;
import com.bangninjia.app.model.OrderData;
import com.bangninjia.app.model.OrderDetail;
import com.bangninjia.app.model.OrderDetailData;
import com.bangninjia.app.model.OrderJson;
import com.bangninjia.app.model.Product;
import com.bangninjia.app.model.SeriesProperty;
import com.bangninjia.app.model.SkirtingBoard;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改订单
 * 
 */
public class OrderUpdateActivity extends BaseNetActivity implements
		OnClickListener, OnItemClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	// 服务地址
	private TextView mConsigneeView;// 收件人
	private TextView mPhoneView;// 收件人电话
	private TextView mAddressView;// 收件人地址

	// 服务信息
	private LinearLayout mServiceInfoLayout;
	private LinearLayout mServiceTypeLayout1;
	private TextView mServiceTypeText1;
	private TextView mServiceTypeText2;
	private TextView mServiceTypeView;// 服务类型
	private TextView mHouseInfoView;// 房屋信息
	private TextView mPaintingInfoView;// 涂料信息
	private TextView mColorInfoView;// 颜色信息

	// 时间
	private AlertDialog dialog;
	private LinearLayout mAppointmentTimeView;// 施工时间
	private TextView mDateTimeView;
	private TextView mDateTimeText;
	private String mDate;// 用于保存选择的日期
	private String mTime;// 用于保存选择的时间

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
	private TextView mPrice4View;// 搬运费
	private TextView mPrice5View;// 运输费
	private TextView mPrice6View;// 踢脚线费
	private TextView mPrice8View;// 拆除费
	private TextView mUnitPriceView;// 单价
	private TextView mTotalMoneyView;// 总金额
	private TextView mAmountPayableView;// 应付金额
	private TextView mAmountPayableText;
	private TextView mPriceHintText;

	private LinearLayout mCouponLayout;
	private TextView mPrice10View;

	private float mPrice10 = 0;

	private float mTotalPrice;// 总价

	private LinearLayout mPrice4Layout;
	private LinearLayout mPrice5Layout;
	private LinearLayout mPrice6Layout;
	private LinearLayout mPrice8Layout;
	private LinearLayout mPrice10Layout;// 折扣金额

	private Button mPayBtn;// 付款按钮
	private int mOrderId;// 订单号
	private OrderDetail mOrderDetail;// 订单详情
	private String mImageProducts;

	private static Format mFormat = new DecimalFormat("0.00");

	private MyProgressDialog mProgressDialog;

	private static final int ORDER_DETAIL_REQUEST_CODE = 1001;// 订单信息requestCode
	private static final int ORDER_UPDATE_REQUEST_CODE = 1002;// 修改订单requestCode
	private static final int PAY_ORDER_CODE = 1003;// 付款前调用接口

	private static final int INVOICE_REQUEST_CODE = 2001;// 发票信息
	private static final int WALL_REQUEST_CODE = 101;// 墙面
	private static final int FLOOR_REQUEST_CODE = 102;// 地板
	private static final int WALLPAPER_REQUEST_CODE = 103;// 壁纸

	private double mArea;
	private double mSkirtingBoard;
	private DemandProperties mDemandProperties;
	private Product mProduct;
	private SkirtingBoard mSkirtingBoardProperty;
	private SeriesProperty mSeriesProperty;
	private double mSkirtingBoardLength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);

		initView();

		mProgressDialog = new MyProgressDialog(this, "正在加载");

		// 接收传过来的订单ID
		// mOrder = (Order) getIntent().getSerializableExtra("order");
		mOrderId = getIntent().getIntExtra("orderId", 0);

		// 获取订单信息
		OrderDetailData data = new OrderDetailData();
		data.setOrderId(String.valueOf(mOrderId));
		data.setType("normal");
		String dataString = MyApplication.getGson().toJson(data);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);
		Log.i(dataString);
		post(ORDER_DETAIL_REQUEST_CODE, Constants.GET_ORDER_DETAIL_URL, params);

	}

	/**
	 * 初始化View
	 */
	public void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.update_order);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		// 收货地址
		mConsigneeView = (TextView) this
				.findViewById(R.id.order_confirm_consignee);
		mPhoneView = (TextView) this.findViewById(R.id.order_confirm_phone);
		mAddressView = (TextView) this.findViewById(R.id.order_confirm_address);

		// 服务信息
		mServiceInfoLayout = (LinearLayout) this
				.findViewById(R.id.order_confirm_serviceinfo);
		mServiceInfoLayout.setOnClickListener(this);
		mServiceTypeLayout1 = (LinearLayout) this
				.findViewById(R.id.service_type_layout1);
		mServiceTypeText1 = (TextView) this
				.findViewById(R.id.service_type_text1);
		mServiceTypeText2 = (TextView) this
				.findViewById(R.id.service_type_text2);
		mServiceTypeView = (TextView) this
				.findViewById(R.id.order_confirm_servicetype);
		mHouseInfoView = (TextView) this
				.findViewById(R.id.order_confirm_house_info);
		mPaintingInfoView = (TextView) this
				.findViewById(R.id.order_confirm_painting_info);
		mColorInfoView = (TextView) this
				.findViewById(R.id.order_confirm_color_info);

		// 施工时间
		mAppointmentTimeView = (LinearLayout) this
				.findViewById(R.id.order_confirm_appointment_time);
		mDateTimeText = (TextView) this.findViewById(R.id.order_datetime_text);
		mDateTimeView = (TextView) this
				.findViewById(R.id.order_confirm_app_datetime);
		mAppointmentTimeView.setOnClickListener(this);// 点击监听事件

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
		mInvoiceInfoView.setOnClickListener(this);// 点击监听事件

		// 金额信息
		mPrice1View = (TextView) this.findViewById(R.id.order_confirm_price1);
		mPrice2View = (TextView) this.findViewById(R.id.order_confirm_price2);
		mPrice4View = (TextView) this.findViewById(R.id.order_confirm_price4);
		mPrice5View = (TextView) this.findViewById(R.id.order_confirm_price5);
		mPrice6View = (TextView) this.findViewById(R.id.order_confirm_price6);
		mPrice8View = (TextView) this.findViewById(R.id.order_confirm_price8);
		mAmountPayableText = (TextView) this
				.findViewById(R.id.order_confirm_amount_payable_text);
		mPriceHintText = (TextView) this
				.findViewById(R.id.order_confirm_price_hint);
		mUnitPriceView = (TextView) this
				.findViewById(R.id.order_confirm_unit_price);
		mTotalMoneyView = (TextView) this
				.findViewById(R.id.order_confirm_total_money);
		mAmountPayableView = (TextView) this
				.findViewById(R.id.order_confirm_amount_payable);

		mCouponLayout = (LinearLayout) this
				.findViewById(R.id.order_confirm_coupon_layout);
		mCouponLayout.setVisibility(View.GONE);
		mPrice10View = (TextView) this.findViewById(R.id.order_confirm_price10);

		mPrice4Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price4_layout);
		mPrice5Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price5_layout);
		mPrice6Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price6_layout);
		mPrice8Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price8_layout);
		mPrice10Layout = (LinearLayout) this
				.findViewById(R.id.order_confirm_price10_layout);
		mPrice4Layout.setVisibility(View.GONE);
		mPrice5Layout.setVisibility(View.GONE);
		mPrice6Layout.setVisibility(View.GONE);
		mPrice5View.setText("￥0.00");

		mPayBtn = (Button) this.findViewById(R.id.order_confirm_pay_btn);
		mPayBtn.setOnClickListener(this);

		/**************** 施工时间 *************/
		mDateTimeText.setText("施工时间");
		/*
		 * Calendar calendar = Calendar.getInstance();
		 * 
		 * calendar.set(Calendar.DAY_OF_MONTH,
		 * calendar.get(Calendar.DAY_OF_MONTH) + 2);// 当前日期+2（2天后）
		 * 
		 * int year = calendar.get(Calendar.YEAR); int month =
		 * calendar.get(Calendar.MONTH) + 1; int dayOfMonth =
		 * calendar.get(Calendar.DAY_OF_MONTH);
		 * 
		 * StringBuilder date = new StringBuilder(); date.append(year); if
		 * (month < 10) { date.append("-0").append(month); } else {
		 * date.append("-").append(month); }
		 * 
		 * if (dayOfMonth < 10) { date.append("-0").append(dayOfMonth); } else {
		 * date.append("-").append(dayOfMonth); } date.append(" 10:00");
		 * 
		 * mDateTimeView.setText(date.toString());// 设置施工时间
		 */

		mAmountPayableText.setText("应付尾款：");
		mPriceHintText
				.setText("提示：修改订单后需支付除定金外的所有尾款，作业人员实际测量后，如取消订单将收取测量费300元");
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 收货地址
		mConsigneeView.setText(mOrderDetail.getReceiverName());
		mPhoneView.setText(mOrderDetail.getReceiverPhone());
		mAddressView.setText(mOrderDetail.getReceiverProvince()
				+ mOrderDetail.getReceiverCity()
				+ mOrderDetail.getReceiverArea()
				+ mOrderDetail.getReceiverRoad()
				+ mOrderDetail.getReceiverAddress());

		// 服务信息
		mServiceTypeView.setText(mOrderDetail.getOrderProperties().get服务类型());
		StringBuilder houseInfo = new StringBuilder(
				mOrderDetail.getDemandSpace());
		houseInfo.append("平方米+");
		if ("墙面更新".equals(mOrderDetail.getServiceName())) {
			houseInfo.append("【" + mOrderDetail.getOrderProperties().get现状信息()
					+ "】+");
		} else if ("地板更新".equals(mOrderDetail.getServiceName())) {
			mServiceTypeText1.setText("地板信息：");
			mServiceTypeText2.setText("踢脚线信息：");
		} else {// 壁纸更新
			mServiceTypeLayout1.setVisibility(View.GONE);
			mServiceTypeText2.setText("壁纸信息：");

			houseInfo.append("【" + mOrderDetail.getOrderProperties().get现状信息()
					+ "】+");
		}
		if (StringUtils.isNullOrEmpty(mOrderDetail.getOrderProperties()
				.get楼层信息())) {
			houseInfo.append(mOrderDetail.getOrderProperties().get电梯信息());
		} else {
			houseInfo.append(mOrderDetail.getOrderProperties().get电梯信息() + "+"
					+ mOrderDetail.getOrderProperties().get楼层信息());
		}
		mHouseInfoView.setText(houseInfo.toString());

		// 发票信息
		mIsInvoiceView.setText("不开发票");
		mInvoiceTypeView.setText(mOrderDetail.getInvoiceType());
		mInvoiceContent.setText(mOrderDetail.getInvoiceMemo());

		// 金额
		List<DemandItem> items = mOrderDetail.getOrderItems();
		for (DemandItem demandItem : items) {
			if (demandItem.getType() == 1) {

				String style = demandItem.getMemo();
				String[] arr = style.split(",");
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < arr.length; i++) {
					String[] str = arr[i].split("=");
					map.put(str[0], str[1]);
				}

				mPaintingInfoView.setText(demandItem.getProductCode() + " "
						+ demandItem.getName());

				if ("墙面更新".equals(mOrderDetail.getServiceName())) {
					// mColorInfoView.setText(map.get("style"));
					mColorInfoView.setText(demandItem.getSpecialProductType());
				} else if ("壁纸更新".equals(mOrderDetail.getServiceName())) {
					mColorInfoView.setText(map.get("model") + " "
							+ demandItem.getName());
				} else {
					mPaintingInfoView.setText(map.get("model") + " "
							+ demandItem.getName());
				}

				mPrice1View.setText("￥"
						+ mFormat.format(Float.valueOf(demandItem.getPrice())
								* demandItem.getQuantity()));
			} else if (demandItem.getType() == 2) {
				mPrice2View.setText("￥"
						+ mFormat.format(Float.valueOf(demandItem.getPrice())
								* demandItem.getQuantity()));
			} else if (demandItem.getType() == 4) {
				if (Float.valueOf(demandItem.getPrice()) > 0) {
					mPrice4Layout.setVisibility(View.VISIBLE);
					mPrice4View.setText("￥"
							+ mFormat.format(Float.valueOf(demandItem
									.getPrice())));
				}
			} else if (demandItem.getType() == 5) {
				if (Float.valueOf(demandItem.getPrice()) > 0) {
					mPrice5Layout.setVisibility(View.VISIBLE);
					mPrice5View.setText("￥"
							+ mFormat.format(Float.valueOf(demandItem
									.getPrice())));
				}
			} else if (demandItem.getType() == 6) {
				mPrice6Layout.setVisibility(View.VISIBLE);// 显示踢脚线金额
				mColorInfoView.setText(demandItem.getName());// 踢脚线信息
				mPrice6View.setText("￥"
						+ mFormat.format(Float.valueOf(demandItem.getPrice())
								* demandItem.getQuantity()));
				mSkirtingBoard = demandItem.getQuantity();
			} else if (demandItem.getType() == 8) {
				if (Float.valueOf(demandItem.getPrice()) > 0) {
					mPrice8Layout.setVisibility(View.VISIBLE);
				} else {
					mPrice8Layout.setVisibility(View.GONE);
				}
				mPrice8View.setText("￥"
						+ mFormat.format(Float.valueOf(demandItem.getPrice())
								* demandItem.getQuantity()));
			} else if (demandItem.getType() == 10) {
				if (Float.valueOf(demandItem.getPrice()) > 0) {
					mPrice10Layout.setVisibility(View.VISIBLE);
				} else {
					mPrice10Layout.setVisibility(View.GONE);
				}
				mPrice10 = Float.valueOf(demandItem.getPrice())
						* demandItem.getQuantity();
				mPrice10View.setText("￥" + mFormat.format(mPrice10));
			}
		}

		mUnitPriceView.setText("(单价：￥"
				+ mFormat.format(Float.valueOf(mOrderDetail.getRealPay())
						/ Float.valueOf(mOrderDetail.getDemandSpace()))
				+ "/平方米)");
		mTotalPrice = mOrderDetail.getRealPay();
		mTotalMoneyView
				.setText("￥" + mFormat.format(mOrderDetail.getRealPay()));

		float mAmountPayble = Float.valueOf(mTotalMoneyView.getText()
				.toString().substring(1))
				- Float.valueOf(mOrderDetail.getHadPay());
		if (mAmountPayble < 0) {
			mAmountPayble = 0;
		}
		mAmountPayableView.setText("￥" + mFormat.format(mAmountPayble));

		if (mOrderDetail.getServiceDate() != null) {
			mDateTimeView.setText(mOrderDetail.getServiceDate()
					.substring(0, 16));
		}

		mDemandProperties = mOrderDetail.getOrderProperties();
		mArea = Float.valueOf(mOrderDetail.getDemandSpace());

	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == ORDER_DETAIL_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				JSONObject jsonString = object.optJSONObject("result");
				TypeToken<OrderDetail> typeToken = new TypeToken<OrderDetail>() {
				};
				mOrderDetail = MyApplication.getGson().fromJson(
						jsonString.toString(), typeToken.getType());
				initData();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == ORDER_UPDATE_REQUEST_CODE) {// 修改订单
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.getString("msg");
				Toast.show(this, msg);
				if (code == 2000) {
					getPayData();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == PAY_ORDER_CODE) {// 获取付款数据
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
		}
	}

	@Override
	public void onNetworkFinish(int requestCode) {
		mProgressDialog.dismiss();
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		Toast.show(this, "请检查您的网络");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.order_confirm_serviceinfo:// 服务信息
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putDouble("area", mArea);
			bundle.putDouble("skirtingBoard", mSkirtingBoard);
			bundle.putSerializable("demandProperties", mDemandProperties);
			intent.putExtras(bundle);
			if ("墙面更新".equals(mOrderDetail.getServiceName())) {
				intent.setClass(OrderUpdateActivity.this, WallActivity.class);
				startActivityForResult(intent, WALL_REQUEST_CODE);
			} else if ("地板更新".equals(mOrderDetail.getServiceName())) {
				intent.setClass(OrderUpdateActivity.this, FloorActivity.class);
				startActivityForResult(intent, FLOOR_REQUEST_CODE);
			} else {
				intent.setClass(OrderUpdateActivity.this,
						WallpaperActivity.class);
				startActivityForResult(intent, WALLPAPER_REQUEST_CODE);
			}

			break;
		case R.id.order_confirm_appointment_time:// 施工时间
			fillAdapterData();
			DatetimePickerDialog();
			break;
		case R.id.order_confirm_invoice_info:// 发票信息
			Intent invoiceIntent = new Intent(OrderUpdateActivity.this,
					InvoiceActivity.class);
			startActivityForResult(invoiceIntent, INVOICE_REQUEST_CODE);
			break;
		case R.id.order_confirm_pay_btn:// 去付款按钮
			updateOrder();
			break;
		default:
			break;
		}
	}

	/**
	 * 修改订单并付款
	 */
	private void updateOrder() {

		String serviceDate = mDateTimeView.getText().toString();
		if (StringUtils.isNullOrEmpty(serviceDate)) {
			Toast.show(this, "请选择施工时间");
			return;
		}

		// 修改订单
		if (mProduct != null) {
			Map<String, String> params = new HashMap<String, String>();
			OrderData data = new OrderData();

			OrderCustomerJson customerJson = new OrderCustomerJson();
			customerJson.setAddress(mOrderDetail.getReceiverAddress());// 具体地址
			customerJson.setArea(mOrderDetail.getReceiverArea());// 城区
			customerJson.setCity(mOrderDetail.getReceiverCity());// 城市
			customerJson.setMobile(mOrderDetail.getReceiverPhone());// 手机
			customerJson.setName(mOrderDetail.getReceiverName());// 用户姓名
			customerJson.setPhone(mOrderDetail.getReceiverPhone());// 用户手机
			customerJson.setProvince(mOrderDetail.getReceiverProvince());// 城市
			customerJson.setRoad(mOrderDetail.getReceiverRoad());// 环线

			data.setCustomerJson(customerJson);

			OrderJson orderJson = new OrderJson();
			orderJson.setAppointmentDate(mOrderDetail.getAppointmentDate()); // 预约时间

			data.setServiceDate(mDateTimeView.getText().toString()); // 施工时间
			data.setOrderId(String.valueOf(mOrderId));

			orderJson.setBrandId(mProduct.getBrandId());// 品牌Id
			orderJson.setDemandProperties(mDemandProperties);// 属性
			orderJson.setDemandSpace(String.valueOf(mArea));// 服务面积

			List<DemandItem> demandItems = new ArrayList<DemandItem>();

			// TODO 判断不同服务类型
			String serviceName = mOrderDetail.getServiceName();
			if ("墙面更新".equals(serviceName)) {
				orderJson.setDemandType(2);

				// 主材费
				DemandItem demandItem = new DemandItem();
				demandItem.setBrandId(mProduct.getBrandId());
				demandItem.setMemo(mColorInfoView.getText().toString());
				demandItem.setName(mProduct.getName());
				demandItem.setPrice(String.valueOf(mProduct.getBnjPrice()));
				demandItem.setProductCode(mProduct.getCode());
				demandItem.setProductId(mProduct.getId());
				double quantity = mArea / (mProduct.getPaintRisenum() * 7);
				demandItem.setQuantity((int) Math.ceil(quantity));
				demandItem.setSmallPic(mImageProducts);
				demandItem.setType(1);

				// 人工安装费
				DemandItem demandItem2 = new DemandItem();
				demandItem2.setBrandId(mProduct.getBrandId());

				String serviceType = mDemandProperties.get服务类型();
				double paintingPrice = 0;
				if (serviceType.equals("涂刷乳胶漆")) {
					paintingPrice = 14.99;
				} else if (serviceType.equals("更新乳胶漆")) {
					paintingPrice = 29.99;
				} else {
					paintingPrice = 39.99;
				}
				demandItem2.setPrice(mFormat.format(paintingPrice));
				demandItem2.setProductCode(mProduct.getCode());
				demandItem2.setProductId(mProduct.getId());
				demandItem2.setQuantity((int) Math.ceil(mArea));
				demandItem2.setType(2);

				// 拆除费
				DemandItem demandItem8 = new DemandItem();
				demandItem8.setBrandId(mProduct.getBrandId());
				demandItem8.setProductId(mProduct.getId());
				demandItem8.setProductCode(mProduct.getCode());
				demandItem8.setQuantity((int) Math.ceil(mArea));
				double price8 = 0;
				if (serviceType.equals("更新乳胶漆")) {
					price8 = 10;
				} else {
					price8 = 0;
				}
				demandItem8.setPrice(mFormat.format(price8));
				demandItem8.setType(8);

				demandItems.add(demandItem);
				demandItems.add(demandItem2);
				demandItems.add(demandItem8);

			} else if ("地板更新".equals(serviceName)) {
				orderJson.setDemandType(3);

				// 主材费
				DemandItem demandItem = new DemandItem();
				demandItem.setBrandId(mProduct.getBrandId());
				demandItem.setMemo(mProduct.getModel());
				demandItem.setName(mProduct.getName());
				demandItem.setPrice(String.valueOf(mProduct.getBnjPrice()));
				demandItem.setProductCode(mProduct.getCode());
				demandItem.setProductId(mProduct.getId());
				demandItem.setQuantity((int) Math.ceil(mArea));
				demandItem.setSmallPic(mImageProducts);
				demandItem.setType(1);

				// 人工安装费
				DemandItem demandItem2 = new DemandItem();
				demandItem2.setBrandId(mProduct.getBrandId());

				String serviceType = mDemandProperties.get服务类型();
				String materialld = mProduct.getMaterialId();
				Log.i("materialld======" + materialld);
				double price2 = 0;
				if ("1107".equals(materialld)) {
					price2 = 0;
				} else if ("1102".equals(materialld)) {
					price2 = 15;
				} else if ("1104".equals(materialld)
						|| "1105".equals(materialld)) {
					price2 = 10;
				} else if ("1103".equals(materialld)
						|| "1106".equals(materialld)) {
					price2 = 30;
				} else if ("1101".equals(materialld)) {
					price2 = 40;
				}

				demandItem2.setPrice(mFormat.format(price2));
				demandItem2.setProductCode(mProduct.getCode());
				demandItem2.setProductId(mProduct.getId());
				demandItem2.setQuantity((int) Math.ceil(mArea));
				demandItem2.setType(2);

				// 搬运费
				DemandItem demandItem4 = new DemandItem();
				demandItem4.setBrandId(mProduct.getBrandId());
				String elevator = mDemandProperties.get电梯信息();
				String storey = mDemandProperties.get楼层信息();
				double price = 0;
				if (elevator.equals("无电梯")) {
					if (storey.equals("二层")) {
						price = 2;
					} else if (storey.equals("三层")) {
						price = 4;
					} else if (storey.equals("四层")) {
						price = 6;
					} else if (storey.equals("五层")) {
						price = 8;
					} else if (storey.equals("六层")) {
						price = 10;
					} else if (storey.equals("七层")) {
						price = 12;
					} else {
						price = 0;
					}
				}
				demandItem4.setPrice(mFormat.format(price));
				demandItem4.setProductCode(mProduct.getCode());
				demandItem4.setProductId(mProduct.getId());
				demandItem4.setQuantity((int) Math.ceil(mArea));
				demandItem4.setType(4);

				// 踢脚线费
				DemandItem demandItem6 = new DemandItem();
				demandItem6.setBrandId(String.valueOf(mSkirtingBoardProperty
						.getId()));

				double unitLength = mSeriesProperty.getUnitLength();
				demandItem6.setPrice(String.valueOf(mSeriesProperty
						.getUnitPrice()));
				demandItem6.setName(mSeriesProperty.getName());
				demandItem6.setMemo(mSeriesProperty.getSpec());
				demandItem6.setProductCode(String.valueOf(mSeriesProperty
						.getId()));
				demandItem6.setProductId(mSeriesProperty.getId());
				demandItem6.setQuantity((int) Math.ceil(mSkirtingBoardLength
						/ unitLength));
				demandItem6.setType(6);

				// 拆除费
				DemandItem demandItem8 = new DemandItem();
				demandItem8.setBrandId(mProduct.getBrandId());
				demandItem8.setProductId(mProduct.getId());
				demandItem8.setProductCode(mProduct.getCode());
				demandItem8.setQuantity((int) Math.ceil(mArea));
				double price8 = 0;
				if (serviceType.equals("更新地板")) {
					price8 = 20;
				} else {
					price8 = 0;
				}
				demandItem8.setPrice(mFormat.format(price8));
				demandItem8.setType(8);

				demandItems.add(demandItem);
				demandItems.add(demandItem2);
				demandItems.add(demandItem4);
				demandItems.add(demandItem6);
				demandItems.add(demandItem8);

			} else {// 壁纸更新
				orderJson.setDemandType(1);
				// 主材费
				DemandItem demandItem = new DemandItem();
				demandItem.setBrandId(mProduct.getBrandId());
				demandItem.setMemo(mProduct.getModel());
				demandItem.setName(mProduct.getName());
				demandItem.setPrice(String.valueOf(mProduct.getBnjPrice()));
				demandItem.setProductCode(mProduct.getCode());
				demandItem.setProductId(mProduct.getId());
				double quantity = mArea / mProduct.getWallpaperContiare();
				demandItem.setSmallPic(mImageProducts);
				demandItem.setQuantity((int) Math.ceil(quantity));
				demandItem.setType(1);

				// 人工安装费
				DemandItem demandItem2 = new DemandItem();
				demandItem2.setBrandId(mProduct.getBrandId());
				String serviceType = mDemandProperties.get服务类型();
				double paintingPrice = 0;
				if (serviceType.equals("铺装壁纸")) {
					paintingPrice = 14.99;
				} else if (serviceType.equals("更新壁纸")) {
					paintingPrice = 29.99;
				} else {
					paintingPrice = 39.99;
				}
				demandItem2.setPrice(mFormat.format(paintingPrice));
				demandItem2.setProductCode(mProduct.getCode());
				demandItem2.setProductId(mProduct.getId());
				demandItem2.setQuantity((int) Math.ceil(mArea));
				demandItem2.setType(2);

				// 拆除费
				DemandItem demandItem8 = new DemandItem();
				demandItem8.setBrandId(mProduct.getBrandId());
				demandItem8.setProductId(mProduct.getId());
				demandItem8.setProductCode(mProduct.getCode());
				demandItem8.setQuantity((int) Math.ceil(mArea));
				double price8 = 0;
				if (serviceType.equals("更新壁纸")) {
					price8 = 10;
				}
				demandItem8.setPrice(mFormat.format(price8));
				demandItem8.setType(8);

				demandItems.add(demandItem);
				demandItems.add(demandItem2);
				demandItems.add(demandItem8);
			}

			// 运输费
			DemandItem demandItem5 = new DemandItem();
			demandItem5.setBrandId(mProduct.getBrandId());
			demandItem5.setProductId(mProduct.getId());
			demandItem5.setProductCode(mProduct.getCode());
			demandItem5.setQuantity(1);
			demandItem5.setPrice(mPrice5View.getText().toString().substring(1));
			demandItem5.setType(5);

			if (mPrice10 > 0) {
				DemandItem demandItem10 = new DemandItem();
				demandItem10 = new DemandItem();// 折扣金额
				demandItem10.setType(10);
				demandItem10.setQuantity(1);
				demandItem10.setPrice(mFormat.format(mPrice10));
				demandItem10.setProductCode(mProduct.getCode());
				demandItem10.setProductId(mProduct.getId());
				demandItem10.setBrandId(mProduct.getBrandId());
				demandItems.add(demandItem10);
			}

			demandItems.add(demandItem5);
			orderJson.setDemandItems(demandItems);

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
			orderJson.setSourceId(1);
			orderJson.setUserId(MyApplication.getUser().getUserId());
			orderJson.setUserLevel(0);// 用户等级
			orderJson.setUserName(MyApplication.getUser().getUserName());
			orderJson.setServiceName(mOrderDetail.getServiceName());
			orderJson.setProductImages(mImageProducts);// 产品图片

			data.setOrderJson(orderJson);

			String dataString = MyApplication.getGson().toJson(data);
			params.put("data", dataString);
			Log.i("OrderData==" + dataString);

			post(ORDER_UPDATE_REQUEST_CODE, Constants.ORDER_UPDATE_URL, params);

		} else {// 没有修改
			OrderData data = new OrderData();
			Log.i("施工时间：" + mDateTimeView.getText().toString());
			data.setServiceDate(mDateTimeView.getText().toString()); // 施工时间
			data.setOrderId(String.valueOf(mOrderId));

			OrderCustomerJson customerJson = new OrderCustomerJson();
			customerJson.setAddress(mOrderDetail.getReceiverAddress());// 具体地址
			customerJson.setArea(mOrderDetail.getReceiverArea());// 城区
			customerJson.setCity(mOrderDetail.getReceiverCity());// 城市
			customerJson.setMobile(mOrderDetail.getReceiverMobile());// 手机
			customerJson.setName(mOrderDetail.getReceiverName());// 用户姓名
			customerJson.setPhone(mOrderDetail.getReceiverPhone());// 用户手机
			customerJson.setProvince(mOrderDetail.getReceiverProvince());
			customerJson.setRoad(mOrderDetail.getReceiverRoad());

			data.setCustomerJson(customerJson);

			OrderJson orderJson = new OrderJson();
			orderJson.setAppointmentDate(mOrderDetail.getAppointmentDate()); // 预约时间
			orderJson.setBrandId(String.valueOf(mOrderDetail.getBrandId()));// 品牌Id

			orderJson.setDemandItems(mOrderDetail.getOrderItems());
			orderJson
					.setEstimatePrice(String.valueOf(mOrderDetail.getRealPay()));
			orderJson.setInvoiceType(mOrderDetail.getInvoiceType());
			orderJson.setIsInvoice(mOrderDetail.getInvoiceType() == null ? 0
					: 1);
			orderJson.setSourceId(Integer.valueOf(mOrderDetail.getSourceId()));
			orderJson.setUserId(MyApplication.getUser().getUserId());
			orderJson
					.setUserLevel(Integer.valueOf(mOrderDetail.getUserLevel()));// 用户等级
			orderJson.setUserName(mOrderDetail.getUserName());
			orderJson.setServiceName(mOrderDetail.getServiceName());
			orderJson.setProductImages(mOrderDetail.getProductImages());
			orderJson.setDemandProperties(mOrderDetail.getOrderProperties());// 属性
			orderJson.setDemandSpace(mOrderDetail.getDemandSpace());// 服务面积

			String type = mOrderDetail.getServiceName();
			int demandType = 0;
			if (type.equals("壁纸更新")) {
				demandType = 1;
			} else if (type.equals("墙面更新")) {
				demandType = 2;
			} else {
				demandType = 3;
			}
			orderJson.setDemandType(demandType);

			data.setOrderJson(orderJson);

			String dataString = MyApplication.getGson().toJson(data);

			Map<String, String> params = new HashMap<String, String>();
			params.put("data", dataString);
			Log.i("OrderData==" + dataString);
			post(ORDER_UPDATE_REQUEST_CODE, Constants.ORDER_UPDATE_URL, params);

			// getPayData();
		}
	}

	/**
	 * 获取服务器支付数据
	 */
	private void getPayData() {
		Map<String, String> params = new HashMap<String, String>();

		String userId = MyApplication.getUser().getUserId();
		String type = "20";// 10-付定金 20-付尾款
		// String money = "0.01";
		String money = mAmountPayableView.getText().toString().substring(1);

		params.put("data", mOrderId + "|" + money + "|" + userId + "|" + type);
		Log.i(mOrderId + "|" + money + "|" + userId + "|" + type);
		post(PAY_ORDER_CODE, Constants.PAY_SERVER_URL, params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("回传");
		if (requestCode == INVOICE_REQUEST_CODE) {
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
		} else if (requestCode == WALL_REQUEST_CODE) {// 墙面
			// TODO
			if (resultCode == RESULT_OK) {
				mArea = data.getDoubleExtra("area", 0);
				mDemandProperties = (DemandProperties) data
						.getSerializableExtra("properties");
				mProduct = (Product) data.getSerializableExtra("product");
				mImageProducts = data.getStringExtra("imageProduct");
				String color = data.getStringExtra("color");

				mServiceTypeView.setText(mDemandProperties.get服务类型());
				String houseInfo = "";
				if ("有电梯".equals(mDemandProperties.get电梯信息())) {
					houseInfo = mDemandProperties.get电梯信息();
				} else {
					houseInfo = mDemandProperties.get电梯信息() + "+"
							+ mDemandProperties.get楼层信息();
				}
				mHouseInfoView.setText(mFormat.format(mArea) + "平方米+【"
						+ mDemandProperties.get现状信息() + "】+" + houseInfo);
				mPaintingInfoView.setText(mProduct.getCode() + " "
						+ mProduct.getName());
				mColorInfoView.setText(color);

				int quantity = (int) Math.ceil(mArea);
				int quantity1 = (int) Math.ceil(mArea
						/ (mProduct.getPaintRisenum() * 7));
				mPrice1View.setText("￥"
						+ mFormat.format(mProduct.getBnjPrice() * quantity1));
				double price2 = 0;
				double price8 = 0;
				if (mDemandProperties.get服务类型().equals("涂刷乳胶漆")) {
					price2 = 14.99;
				} else if (mDemandProperties.get服务类型().equals("更新乳胶漆")) {
					price2 = 29.99;
					price8 = 10;
				} else {
					price2 = 39.99;
				}
				mPrice2View.setText("￥" + mFormat.format(quantity * price2));
				mPrice8View.setText("￥" + mFormat.format(quantity * price8));
				if (price8 > 0) {
					mPrice8Layout.setVisibility(View.VISIBLE);
				} else {
					mPrice8Layout.setVisibility(View.GONE);
				}
				mTotalPrice = Float.valueOf(mPrice1View.getText().toString()
						.substring(1))
						+ Float.valueOf(mPrice2View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice5View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice8View.getText().toString()
								.substring(1)) - mPrice10;
				mUnitPriceView.setText("(单价：￥"
						+ mFormat.format(mTotalPrice / mArea) + "/平方米)");
				mTotalMoneyView.setText("￥" + mFormat.format(mTotalPrice));

				mAmountPayableView.setText("￥"
						+ mFormat.format(mTotalPrice
								- Float.valueOf(mOrderDetail.getHadPay())));

			}

		} else if (requestCode == FLOOR_REQUEST_CODE) {// 地板更新
			// TODO
			if (resultCode == RESULT_OK) {
				mArea = data.getDoubleExtra("area", 0);
				mDemandProperties = (DemandProperties) data
						.getSerializableExtra("properties");
				mProduct = (Product) data.getSerializableExtra("product");
				mImageProducts = data.getStringExtra("imageProduct");
				mSkirtingBoardLength = data.getDoubleExtra(
						"skirtingBoardLength", 0);
				mSeriesProperty = (SeriesProperty) data
						.getSerializableExtra("seriesProperty");
				mSkirtingBoardProperty = (SkirtingBoard) data
						.getSerializableExtra("skirtingBoard");

				mServiceTypeView.setText(mDemandProperties.get服务类型());
				String houseInfo = "";
				if ("有电梯".equals(mDemandProperties.get电梯信息())) {
					houseInfo = mDemandProperties.get电梯信息();
				} else {
					houseInfo = mDemandProperties.get电梯信息() + "+"
							+ mDemandProperties.get楼层信息();
				}
				mHouseInfoView.setText(mFormat.format(mArea) + "平方米+"
						+ houseInfo);
				mPaintingInfoView.setText(mProduct.getModel() + " "
						+ mProduct.getName());
				mColorInfoView.setText(mSeriesProperty.getName());

				int quantity = (int) Math.ceil(mArea);
				mPrice1View.setText("￥"
						+ mFormat.format(mProduct.getBnjPrice() * quantity));

				String materialld = mProduct.getMaterialId();
				Log.i("materialld======" + materialld);
				double price2 = 0;
				if ("1107".equals(materialld)) {
					price2 = 0;
				} else if ("1102".equals(materialld)) {
					price2 = 15;
				} else if ("1104".equals(materialld)
						|| "1105".equals(materialld)) {
					price2 = 10;
				} else if ("1103".equals(materialld)
						|| "1106".equals(materialld)) {
					price2 = 30;
				} else if ("1101".equals(materialld)) {
					price2 = 40;
				}

				double price4 = 0;
				String elevator = mDemandProperties.get电梯信息();
				String storey = mDemandProperties.get楼层信息();
				if (elevator.equals("无电梯")) {
					if (storey.equals("二层")) {
						price4 = 2;
					} else if (storey.equals("三层")) {
						price4 = 4;
					} else if (storey.equals("四层")) {
						price4 = 6;
					} else if (storey.equals("五层")) {
						price4 = 8;
					} else if (storey.equals("六层")) {
						price4 = 10;
					} else if (storey.equals("七层")) {
						price4 = 12;
					} else {
						price4 = 0;
					}
				}

				double unitLength = mSeriesProperty.getUnitLength();
				int quantity6 = (int) Math.ceil(mSkirtingBoardLength
						/ unitLength);

				double price8 = 0;
				if (mDemandProperties.get服务类型().equals("更新地板")) {
					price8 = 20;
				} else {
					price8 = 0;
				}
				mPrice2View.setText("￥" + mFormat.format(quantity * price2));
				mPrice4View.setText("￥" + mFormat.format(quantity * price4));
				mPrice6View.setText("￥"
						+ mFormat.format(quantity6
								* mSeriesProperty.getUnitPrice()));
				mPrice8View.setText("￥" + mFormat.format(quantity * price8));
				if (price8 > 0) {
					mPrice8Layout.setVisibility(View.VISIBLE);
				} else {
					mPrice8Layout.setVisibility(View.GONE);
				}
				mTotalPrice = Float.valueOf(mPrice1View.getText().toString()
						.substring(1))
						+ Float.valueOf(mPrice2View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice4View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice5View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice6View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice8View.getText().toString()
								.substring(1)) - mPrice10;
				mUnitPriceView.setText("(单价：￥"
						+ mFormat.format(mTotalPrice / mArea) + "/平方米)");
				mTotalMoneyView.setText("￥" + mFormat.format(mTotalPrice));

				mAmountPayableView.setText("￥"
						+ mFormat.format(mTotalPrice
								- Float.valueOf(mOrderDetail.getHadPay())));

			}
		} else if (requestCode == WALLPAPER_REQUEST_CODE) {// 壁纸
			if (resultCode == RESULT_OK) {

				mArea = data.getDoubleExtra("area", 0);
				Log.i("修改的服务面积：" + mArea);
				mDemandProperties = (DemandProperties) data
						.getSerializableExtra("properties");
				Log.i("修改的属性：" + mDemandProperties.toString());
				mProduct = (Product) data.getSerializableExtra("product");
				Log.i("修改的产品：" + mProduct.toString());
				mImageProducts = data.getStringExtra("imageProduct");

				mServiceTypeView.setText(mDemandProperties.get服务类型());
				String houseInfo = "";
				if ("有电梯".equals(mDemandProperties.get电梯信息())) {
					houseInfo = mDemandProperties.get电梯信息();
				} else {
					houseInfo = mDemandProperties.get电梯信息() + "+"
							+ mDemandProperties.get楼层信息();
				}
				mHouseInfoView.setText(mFormat.format(mArea) + "平方米+【"
						+ mDemandProperties.get现状信息() + "】+" + houseInfo);
				mColorInfoView.setText(mProduct.getModel() + " "
						+ mProduct.getName());

				int quantity = (int) Math.ceil(mArea);
				int quantity1 = (int) Math.ceil(mArea
						/ mProduct.getWallpaperContiare());
				mPrice1View.setText("￥"
						+ mFormat.format(mProduct.getBnjPrice() * quantity1));
				double price2 = 0;
				double price8 = 0;
				if (mDemandProperties.get服务类型().equals("铺装壁纸")) {
					price2 = 14.99;
				} else if (mDemandProperties.get服务类型().equals("更新壁纸")) {
					price2 = 29.99;
					price8 = 10;
				} else {
					price2 = 39.99;
				}
				mPrice2View.setText("￥" + mFormat.format(quantity * price2));
				mPrice8View.setText("￥" + mFormat.format(quantity * price8));
				if (price8 > 0) {
					mPrice8Layout.setVisibility(View.VISIBLE);
				} else {
					mPrice8Layout.setVisibility(View.GONE);
				}
				mTotalPrice = Float.valueOf(mPrice1View.getText().toString()
						.substring(1))
						+ Float.valueOf(mPrice2View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice5View.getText().toString()
								.substring(1))
						+ Float.valueOf(mPrice8View.getText().toString()
								.substring(1)) - mPrice10;
				mUnitPriceView.setText("(单价：￥"
						+ mFormat.format(mTotalPrice / mArea) + "/平方米)");
				mTotalMoneyView.setText("￥" + mFormat.format(mTotalPrice));

				mAmountPayableView.setText("￥"
						+ mFormat.format(mTotalPrice
								- Float.valueOf(mOrderDetail.getHadPay())));
			}
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

		mOrderDetail.getAppointmentDate();

		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 1);// 让日期加2(2天后)

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
					Toast.show(OrderUpdateActivity.this, "请选择日期和时间");
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
	// alipay.setOut_trade_no(String.valueOf(mOrderId));// 订单号
	// alipay.setPartner(Keys.DEFAULT_PARTNER);
	// alipay.setPayment_type("1");
	// alipay.setSeller_id(Keys.DEFAULT_SELLER);
	// alipay.setService("mobile.securitypay.pay");
	// alipay.setTotal_fee("0.01");// 支付金额
	// Log.i("MainActivity", "支付金额："
	// + mAmountPayableView.getText().toString().substring(1));
	// // alipay.setTotal_fee(mFormat.format(mTotalPrice
	// // - Float.valueOf(mOrderDetail.getHadPay())));
	//
	// PayOrder payOrder = new PayOrder(this, alipay);
	// payOrder.pay();
	// }

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderUpdateActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderUpdateActivity");
		MobclickAgent.onPause(this);
	}
}
