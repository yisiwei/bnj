package com.bangninjia.app.activity;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.alipay.Keys;
import com.bangninjia.app.alipay.PayOrder;
import com.bangninjia.app.model.Alipay;
import com.bangninjia.app.model.DemandItem;
import com.bangninjia.app.model.OrderChangeData;
import com.bangninjia.app.model.OrderDetail;
import com.bangninjia.app.model.OrderDetailData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单详情
 * 
 */
public class OrderDetailActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mOrderIdTv;//订单编号
	private TextView mOrderStatusTv;//订单状态

	private TextView mConsigneeTv;//收货人
	private TextView mPhoneTv;//收货人电话
	private TextView mAddressTv;//收货人地址

	private TextView mServiceItemTv;// 服务项目
	private TextView mServiceTypeTv;// 服务类型
	private TextView mHouseInfoTv;// 房屋信息
	private TextView mAppointmentTimeTv;// 预约时间
	private TextView mWorkTimeTextTv;// 施工时间Text
	private TextView mWorkTimeTv;// 施工时间

	// 材料信息
	private TextView mMaterialBrandTv;// 主材品牌
	private TextView mMaterialModelTv;// 主材型号
	private TextView mMaterialTypeTv;// 主材材质
	private TextView mMaterialAddressTv;// 主材产地
	private TextView mMaterialAddressTextTv;// 主材产地Text
	private TextView mMaterialTypeTextTv;// 主材材质Text

	// 作业人员信息
	private RelativeLayout mWorkerLayout;
	private TextView mWorkerNumberTv;
	private TextView mWorkerNameTv;
	private TextView mWorkerPhoneTv;
	private TextView mWorkerAddressTv;

	// 供应商信息
	private RelativeLayout mSupplierLayout;
	private TextView mSupplierNameTv;
	private TextView mSupplierPhoneTv;
	private TextView mSupplierAddressTv;

	//服务费用
	private TextView mServicePrice4Tv;
	private TextView mServicePrice5Tv;
	private TextView mServicePrice6Tv;
	private TextView mServicePrice8Tv;
	private TextView mServicePrice10Tv;

	private TextView mPrice1Tv;
	private TextView mPrice2Tv;
	// private TextView mPrice3Tv;
	private TextView mPrice4Tv;
	private TextView mPrice5Tv;
	private TextView mPrice6Tv;
	// private TextView mPrice7Tv;
	private TextView mPrice8Tv;
	private TextView mPrice10Tv;//折扣金额
	private TextView mTotalPriceTv;

	//操作按钮
	private Button mCancelBtn;
	private Button mPayBtn;
	private Button mDeleteBtn;
	private Button mTrackingBtn;
	private Button mCommentBtn;
	private Button mUpdateBtn;

	private static final int DELETE_ORDER_REQUEST_CODE = 1001;
	private static final int ORDER_DETAIL_REQUEST_CODE = 1002;
	private static final int PAY_ORDER_CODE = 1003;// 付款前调用接口

	private static final int CANCEL_ORDER_REQUEST_CODE = 2001;

	private int mOrderId;
	private OrderDetail mOrderDetail;
	private MyProgressDialog mProgressDialog;

	private static Format mFormat = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);

		mOrderId = getIntent().getIntExtra("orderId", 0);

		initView();

		mProgressDialog = new MyProgressDialog(this, "正在加载");
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.order_detail);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		mOrderIdTv = (TextView) this.findViewById(R.id.order_detail_orderid);
		mOrderStatusTv = (TextView) this.findViewById(R.id.order_detail_status);

		mConsigneeTv = (TextView) this
				.findViewById(R.id.order_detail_consignee);
		mPhoneTv = (TextView) this.findViewById(R.id.order_detail_phone);
		mAddressTv = (TextView) this.findViewById(R.id.order_detail_address);

		mServiceItemTv = (TextView) this
				.findViewById(R.id.order_detail_service_items);
		mServiceTypeTv = (TextView) this
				.findViewById(R.id.order_detail_service_type);
		mHouseInfoTv = (TextView) this
				.findViewById(R.id.order_detail_house_info);
		mAppointmentTimeTv = (TextView) this
				.findViewById(R.id.order_detail_appointment_time);
		mWorkTimeTextTv = (TextView) this.findViewById(R.id.work_time);
		mWorkTimeTv = (TextView) this.findViewById(R.id.order_detail_work_time);
		
		mMaterialBrandTv = (TextView) this
				.findViewById(R.id.order_detail_material_brand);
		mMaterialModelTv = (TextView) this
				.findViewById(R.id.order_detail_material_model);
		mMaterialTypeTv = (TextView) this
				.findViewById(R.id.order_detail_material_type);
		mMaterialAddressTv = (TextView) this
				.findViewById(R.id.order_detail_material_address);
		mMaterialAddressTextTv = (TextView) this.findViewById(R.id.material_address);
		mMaterialTypeTextTv = (TextView) this.findViewById(R.id.material_type);

		mWorkerLayout = (RelativeLayout) this
				.findViewById(R.id.order_detail_worker_layout);
		mWorkerNumberTv = (TextView) this
				.findViewById(R.id.order_detail_worker_number);
		mWorkerNameTv = (TextView) this
				.findViewById(R.id.order_detail_worker_name);
		mWorkerPhoneTv = (TextView) this
				.findViewById(R.id.order_detail_worker_phone);
		mWorkerAddressTv = (TextView) this
				.findViewById(R.id.order_detail_worker_address);
		mWorkerPhoneTv.setOnClickListener(this);
		
		//供应商信息
		mSupplierLayout = (RelativeLayout) this.findViewById(R.id.order_detail_supplier_layout);
		mSupplierNameTv = (TextView) this.findViewById(R.id.order_detail_supplier_name);
		mSupplierPhoneTv = (TextView) this.findViewById(R.id.order_detail_supplier_phone);
		mSupplierAddressTv = (TextView) this.findViewById(R.id.order_detail_supplier_address);

		mServicePrice4Tv = (TextView) this.findViewById(R.id.service_price_4);
		mServicePrice5Tv = (TextView) this.findViewById(R.id.service_price_5);
		mServicePrice6Tv = (TextView) this.findViewById(R.id.service_price_6);
		mServicePrice8Tv = (TextView) this.findViewById(R.id.service_price_8);
		mServicePrice10Tv = (TextView) this.findViewById(R.id.service_price_10);

		mPrice1Tv = (TextView) this.findViewById(R.id.order_detail_price1);
		mPrice2Tv = (TextView) this.findViewById(R.id.order_detail_price2);
		// mPrice3Tv = (TextView) this.findViewById(R.id.order_detail_price3);
		mPrice4Tv = (TextView) this.findViewById(R.id.order_detail_price4);
		mPrice5Tv = (TextView) this.findViewById(R.id.order_detail_price5);
		mPrice6Tv = (TextView) this.findViewById(R.id.order_detail_price6);
		// mPrice7Tv = (TextView) this.findViewById(R.id.order_detail_price7);
		mPrice8Tv = (TextView) this.findViewById(R.id.order_detail_price8);
		mPrice10Tv = (TextView) this.findViewById(R.id.order_detail_price10);
		mTotalPriceTv = (TextView) this
				.findViewById(R.id.order_detail_total_pric);

		mCancelBtn = (Button) this.findViewById(R.id.order_cancel_btn);
		mPayBtn = (Button) this.findViewById(R.id.order_payment_btn);
		mDeleteBtn = (Button) this.findViewById(R.id.order_delete_btn);
		mTrackingBtn = (Button) this.findViewById(R.id.order_tracking_btn);
		mCommentBtn = (Button) this.findViewById(R.id.order_comment_btn);
		mUpdateBtn = (Button) this.findViewById(R.id.order_update_btn);

		mCancelBtn.setOnClickListener(this);
		mPayBtn.setOnClickListener(this);
		mDeleteBtn.setOnClickListener(this);
		mTrackingBtn.setOnClickListener(this);
		mCommentBtn.setOnClickListener(this);
		mUpdateBtn.setOnClickListener(this);

		mOrderIdTv.setText("" + mOrderId);

		getOrderData();

	}

	/**
	 * 获取订单信息
	 * 
	 */
	private void getOrderData() {
		OrderDetailData data = new OrderDetailData();
		data.setOrderId(String.valueOf(mOrderId));
		data.setType("normal");
		String dataString = MyApplication.getGson().toJson(data);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);
		post(ORDER_DETAIL_REQUEST_CODE, Constants.GET_ORDER_DETAIL_URL, params);
		Log.i(dataString+"--"+Constants.GET_ORDER_DETAIL_URL);
	}

	/**
	 * 显示数据
	 */
	private void showOrderData() {
		mOrderStatusTv.setText(showButtonAndStatus(Integer
				.parseInt(mOrderDetail.getOrderStatus())));

		mConsigneeTv.setText(mOrderDetail.getReceiverName());
		mPhoneTv.setText(mOrderDetail.getReceiverPhone());
		mAddressTv.setText(mOrderDetail.getReceiverProvince()
				+ mOrderDetail.getReceiverCity()
				+ mOrderDetail.getReceiverArea()
				+ mOrderDetail.getReceiverRoad()
				+ mOrderDetail.getReceiverAddress());

		mServiceItemTv.setText(mOrderDetail.getServiceName());
		mServiceTypeTv.setText(mOrderDetail.getOrderProperties().get服务类型());
		String houseInfo = null;
		if (StringUtils.isNullOrEmpty(mOrderDetail.getOrderProperties()
				.get现状信息())) {
			if ("有电梯".equals(mOrderDetail.getOrderProperties().get电梯信息())) {
				houseInfo = mOrderDetail.getDemandSpace() + "平方米+"
						+ mOrderDetail.getOrderProperties().get电梯信息();
			}else{
				houseInfo = mOrderDetail.getDemandSpace() + "平方米+"
						+ mOrderDetail.getOrderProperties().get电梯信息() + "+"
						+ mOrderDetail.getOrderProperties().get楼层信息();
			}
		} else {
			if ("有电梯".equals(mOrderDetail.getOrderProperties().get电梯信息())) {
				houseInfo = mOrderDetail.getDemandSpace() + "平方米+【"
						+ mOrderDetail.getOrderProperties().get现状信息() + "】+"
						+ mOrderDetail.getOrderProperties().get电梯信息();
			}else{
				houseInfo = mOrderDetail.getDemandSpace() + "平方米+【"
						+ mOrderDetail.getOrderProperties().get现状信息() + "】+"
						+ mOrderDetail.getOrderProperties().get电梯信息() + "+"
						+ mOrderDetail.getOrderProperties().get楼层信息();
			}
		}
		mHouseInfoTv.setText(houseInfo);
		mAppointmentTimeTv.setText(mOrderDetail.getAppointmentDate());// 预约时间
		if (mOrderDetail.getServiceDate() != null) {
			mWorkTimeTv.setVisibility(View.VISIBLE);
			mWorkTimeTextTv.setVisibility(View.VISIBLE);
			mWorkTimeTv.setText(mOrderDetail.getServiceDate().substring(0, 16));// 施工时间
		}

		List<DemandItem> demandItems = mOrderDetail.getOrderItems();
		for (DemandItem demandItem : demandItems) {
			if (demandItem.getType() == 1) {
				Map<String, String> map = new HashMap<String, String>();
				String memo = demandItem.getMemo();
				if (!StringUtils.isNullOrEmpty(memo)) {
					String[] momeItem = memo.split(",");
					Log.i(momeItem.toString());
					for (int i = 0; i < momeItem.length; i++) {
						String[] str = momeItem[i].split("=");
						map.put(str[0], str[1]);
					}

					mMaterialBrandTv.setText(map.get("brandName"));
					mMaterialModelTv.setText(map.get("model"));
					mMaterialTypeTv.setText(map.get("materialType"));
					mMaterialAddressTv.setText(map.get("origine"));
					
					if ("墙面更新".equals(mOrderDetail.getServiceName())) {
						mMaterialAddressTv.setVisibility(View.GONE);
						mMaterialAddressTextTv.setVisibility(View.GONE);
						mMaterialTypeTextTv.setText("油漆颜色：");
						mMaterialTypeTv.setText(demandItem.getSpecialProductType());
					}
				}

				mPrice1Tv.setText(mFormat.format(Float.valueOf(demandItem
						.getPrice()) * demandItem.getQuantity())
						+ "元");
			} else if (demandItem.getType() == 2) {
				mPrice2Tv.setText(mFormat.format(Float.valueOf(demandItem
						.getPrice()) * demandItem.getQuantity())
						+ "元");
			} else if (demandItem.getType() == 4) {
				if (!"0".equals(demandItem.getPrice())) {
					mPrice4Tv.setVisibility(View.VISIBLE);
					mServicePrice4Tv.setVisibility(View.VISIBLE);
					mPrice4Tv.setText(mFormat.format(Float.valueOf(demandItem
							.getPrice()) * demandItem.getQuantity())
							+ "元");
				}
			} else if (demandItem.getType() == 5) {
				if (!"0".equals(demandItem.getPrice())) {
					mServicePrice5Tv.setVisibility(View.VISIBLE);
					mPrice5Tv.setVisibility(View.VISIBLE);
					mPrice5Tv.setText(mFormat.format(Float.valueOf(demandItem
							.getPrice()) * demandItem.getQuantity())
							+ "元");
				}
			} else if (demandItem.getType() == 6) {
				if (!"0".equals(demandItem.getPrice())) {
					mServicePrice6Tv.setVisibility(View.VISIBLE);
					mPrice6Tv.setVisibility(View.VISIBLE);
					mPrice6Tv.setText(mFormat.format(Float.valueOf(demandItem
							.getPrice()) * demandItem.getQuantity())
							+ "元");
				}
			} else if (demandItem.getType() == 8) {
				if (!"0".equals(demandItem.getPrice())) {
					mServicePrice8Tv.setVisibility(View.VISIBLE);
					mPrice8Tv.setVisibility(View.VISIBLE);
					mPrice8Tv.setText(mFormat.format(Float.valueOf(demandItem
							.getPrice()) * demandItem.getQuantity())
							+ "元");
				}
			}else if(demandItem.getType() == 10){
				if (!"0".equals(demandItem.getPrice())) {
					mServicePrice10Tv.setVisibility(View.VISIBLE);
					mPrice10Tv.setVisibility(View.VISIBLE);
					mPrice10Tv.setText(mFormat.format(Float.valueOf(demandItem
							.getPrice()) * demandItem.getQuantity())
							+ "元");
				}
			}
		}

		mWorkerNumberTv.setText(mOrderDetail.getWorker().getId());
		mWorkerNameTv.setText(mOrderDetail.getWorker().getName());
		mWorkerPhoneTv.setText(mOrderDetail.getWorker().getMobile());
		mWorkerAddressTv.setText(mOrderDetail.getWorker().getAddress());
		
		mSupplierNameTv.setText(mOrderDetail.getSupplier().getName());
		mSupplierPhoneTv.setText(mOrderDetail.getSupplier().getTelPhone());
		mSupplierAddressTv.setText(mOrderDetail.getSupplier().getAddress());

		mTotalPriceTv.setText(mFormat.format(mOrderDetail.getRealPay()) + "元");
	}

	private String showButtonAndStatus(int orderStatus) {
		String statuString = null;

		mCancelBtn.setVisibility(View.GONE);
		mPayBtn.setVisibility(View.GONE);
		mDeleteBtn.setVisibility(View.GONE);
		mUpdateBtn.setVisibility(View.GONE);
		mCommentBtn.setVisibility(View.GONE);

		int comeFrom = Integer.valueOf(MyApplication.getComeFrom());

		switch (orderStatus) {

		case 100:
			statuString = "待付款";
			mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
			mWorkerLayout.setVisibility(View.GONE);// 隐藏作业人员信息
			switch (comeFrom) {
			case 1:
				mCancelBtn.setVisibility(View.VISIBLE);
				mPayBtn.setVisibility(View.VISIBLE);
				break;
			case 2://供应商
				break;
			case 3:
				break;
			}
			break;
		case 101:
			statuString = "待派单";
			mWorkerLayout.setVisibility(View.GONE);
			mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
			switch (comeFrom) {
			case 1:
				mCancelBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				break;
			case 3:
				break;
			}
			break;
		case 102:
			statuString = "已派单";
			switch (comeFrom) {
			case 1:
				mCancelBtn.setVisibility(View.VISIBLE);
				mUpdateBtn.setVisibility(View.VISIBLE);
				mWorkerLayout.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}
			break;
		case 103:
			statuString = "用户已确认";
			switch (comeFrom) {
			case 1:
				mWorkTimeTextTv.setVisibility(View.VISIBLE);
				mWorkTimeTv.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}
			break;
		case 104:
			statuString = "作业人员确认";
			switch (comeFrom) {
			case 1:
				mTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}
			break;
		case 105:
			statuString = "施工完成";
			switch (comeFrom) {
			case 1:
				mCommentBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}
			break;
		case 106:
			statuString = "已评价";
			switch (comeFrom) {
			case 1:
				mDeleteBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}
			break;
		case 200:
			statuString = "申请取消";
			break;
		case 201:
			statuString = "取消成功";
			switch (comeFrom) {
			case 1:
				mDeleteBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}
			break;
		case 202:
			statuString = "取消失败";
			switch (comeFrom) {
			case 1:
				mCancelBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}
			break;
		case 203:
			statuString = "派单失败";
			switch (comeFrom) {
			case 1:
				mCancelBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				mWorkerLayout.setVisibility(View.VISIBLE);
				mSupplierLayout.setVisibility(View.GONE);//隐藏供应商信息
				break;
			case 3:
				mWorkerLayout.setVisibility(View.GONE);//隐藏作业人员
				mSupplierLayout.setVisibility(View.VISIBLE);//显示供应商信息
				break;
			}

			break;
		default:
			statuString = "";
			break;
		}

		return statuString;
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i("" + jsonData);
		if (requestCode == DELETE_ORDER_REQUEST_CODE) {// 删除
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				if ("2000".equals(jsonObject.optString("code"))) {
					Toast.show(this, "删除成功");
					setResult(RESULT_OK);
					this.finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == ORDER_DETAIL_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				String jsonString = object.getString("result");
				mOrderDetail = MyApplication.getGson().fromJson(jsonString,
						OrderDetail.class);
				showOrderData();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == PAY_ORDER_CODE) {// 获取付款参数
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
//					alipay.setTotal_fee("0.01");// 支付金额
					alipay.setTotal_fee(map.get("total_fee").substring(1,
							map.get("total_fee").length() - 1));// 支付金额
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
		case R.id.order_cancel_btn:// 取消订单
			Intent intent = new Intent(this, OrderCancelActivity.class);
			intent.putExtra("orderId", mOrderId);
			startActivityForResult(intent, CANCEL_ORDER_REQUEST_CODE);
			break;
		case R.id.order_delete_btn:// 删除订单
			// 删除订单
			View view = View.inflate(this, R.layout.dialog_delete, null);
			TextView cancel = (TextView) view.findViewById(R.id.cancel);
			TextView confirm = (TextView) view.findViewById(R.id.confirm);

			final AlertDialog deleteDialog = new AlertDialog.Builder(this)
					.create();
			deleteDialog.setCancelable(false);
			deleteDialog.show();
			deleteDialog.setContentView(view);
			deleteDialog.getWindow().setLayout(600, LayoutParams.WRAP_CONTENT);

			confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteOrder();
					deleteDialog.dismiss();
				}
			});

			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteDialog.dismiss();
				}
			});
			break;
		case R.id.order_update_btn:// 修改订单
			// 修改订单
			Intent updateIntent = new Intent(this, OrderUpdateActivity.class);
			updateIntent.putExtra("orderId", mOrderId);
			startActivity(updateIntent);
			break;
		case R.id.order_tracking_btn:// 订单跟踪
			Intent trackingIntent = new Intent(this,
					OrderTrackingActivity.class);
			trackingIntent.putExtra("orderId", mOrderId);
			startActivity(trackingIntent);
			break;
		case R.id.order_comment_btn:// 去评价
			// 去评价
			Intent commentIntent = new Intent(this,
					ServiceCommentActivity.class);
			commentIntent.putExtra("orderId", mOrderId);
			startActivity(commentIntent);
			break;
		case R.id.order_payment_btn:// 去付款
			getPayData();
			break;
		case R.id.order_detail_worker_phone://拨打作业人员电话
			showCallDialog();
			break;
		default:
			break;
		}
	}
	
	private int getWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		Log.i("屏幕宽度："+width);
		return width;
	}
	
	private void showCallDialog(){
		View view = View.inflate(this, R.layout.dialog_contact_us,
				null);
		final TextView mPhone = (TextView) view.findViewById(R.id.contact_us_phone);
		TextView callText = (TextView) view.findViewById(R.id.call_text);
		TextView mCancel = (TextView) view.findViewById(R.id.contact_us_cancel);
		TextView mCall = (TextView) view.findViewById(R.id.contact_us_call);
		callText.setVisibility(View.GONE);
		mPhone.setText(mOrderDetail.getWorker().getMobile());

		final AlertDialog mDialog = new AlertDialog.Builder(this).create();
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setContentView(view);
		mDialog.getWindow().setLayout(getWidth()/3*2, LayoutParams.WRAP_CONTENT);

		mCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = mPhone.getText().toString();
				Log.i("MainActivity", "电话：" + phone);
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+phone));
				startActivity(intent);
				mDialog.dismiss();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("回传");
		if (requestCode == CANCEL_ORDER_REQUEST_CODE) {// 取消
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				// this.finish();
				getOrderData();
			}
		}
	}

	/**
	 * 删除订单
	 */
	private void deleteOrder() {
		OrderChangeData data = new OrderChangeData();
		data.setUserId(MyApplication.getUser().getUserId());
		data.setOrderId(String.valueOf(mOrderId));
		data.setOper("USER_DEL");

		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i(dataString);
		post(DELETE_ORDER_REQUEST_CODE, Constants.CHANGE_ORDER_URL, params);
	}

	/**
	 * 获取服务器支付数据
	 */
	private void getPayData() {
		Map<String, String> params = new HashMap<String, String>();

		String userId = MyApplication.getUser().getUserId();
		String type = "10";// 10-付定金 20-付尾款
		String money = "0.01";
		double price = mOrderDetail.getRealPay() * 0.3;
		if (price < 300) {
			money = "300.00";
		}else{
			money = mFormat.format(price);
		}

		params.put("data", mOrderId + "|" + money + "|" + userId + "|" + type);
		Log.i(mOrderId + "|" + money + "|" + userId + "|" + type);
		post(PAY_ORDER_CODE, Constants.PAY_SERVER_URL, params);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderDetailActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderDetailActivity");
		MobclickAgent.onPause(this);
	}

	/**
	 * 去付款
	 */
	// private void payOrder() {
	// Alipay alipay = new Alipay();
	// alipay.set_input_charset("utf-8");
	// alipay.setBody(mOrderDetail.getServiceName()
	// + mOrderDetail.getOrderProperties());
	// alipay.setSubject("订单号：" + mOrderId
	// + "，" + mOrderDetail.getServiceName());
	// alipay.setIt_b_pay(Keys.PAY_TIME);
	//
	// // alipay.setNotify_url(Constants.ALI_PAY_URL);
	// alipay.setNotify_url(Keys.NOTIFY_URL);
	//
	// //订单号
	// alipay.setOut_trade_no(mOrderDetail.getOrderId() + "");
	//
	// alipay.setPartner(Keys.DEFAULT_PARTNER);
	// alipay.setPayment_type(Keys.PAYMENY_TYPE);
	// alipay.setSeller_id(Keys.DEFAULT_SELLER);
	// alipay.setService(Keys.SERVICE_URL);
	// Log.i("MainActivity",
	// "支付金额：" + mFormat.format(Float.valueOf(mOrderDetail.getRealPay()) *
	// 0.3));
	// //
	// alipay.setTotal_fee(mFormat.format(Float.valueOf(mOrderDetail.getRealPay())
	// * 0.3));// 支付金额
	// alipay.setTotal_fee("0.01");// 支付金额
	//
	// PayOrder payOrder = new PayOrder(this, alipay);
	// payOrder.pay();
	// }

}
