package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.adapter.CancelProgressAdapter;
import com.bangninjia.app.model.OrderCancelDetail;
import com.bangninjia.app.model.OrderCancelDetailData;
import com.bangninjia.app.model.OrderLog;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 取消订单详情
 *
 */
public class CancelOrderDetailActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mOrderIdTv;
	private TextView mOrderStatusTv;

	private ListView mListView;
	private CancelProgressAdapter mAdapter;

	private TextView mDetailOrderIdTv;
	private TextView mOrderPayDetailTv;
	private TextView mOrderApplyReasonTv;
	private TextView mOrderContactsTv;
	private TextView mOrderContactPhoneTv;
	
	private Button mTrackingBtn;

	private int mOrderId;
	private OrderCancelDetail mOrderCancelDetail;

	private static final int ORDER_CANCEL_DETAIL_REQUEST_CODE = 1002;

	private MyProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancel_order_detail);

		mOrderId = getIntent().getIntExtra("orderId", 0);

		initView();

		mProgressDialog = new MyProgressDialog(this, "正在加载");

		getOrderData();
	}

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

		mListView = (ListView) this.findViewById(R.id.cancel_order_listview);

		mDetailOrderIdTv = (TextView) this
				.findViewById(R.id.cancel_order_detail_order_number);
		mOrderPayDetailTv = (TextView) this
				.findViewById(R.id.cancel_order_detail_pay_detail);
		mOrderApplyReasonTv = (TextView) this
				.findViewById(R.id.cancel_order_detail_apply_reason);
		mOrderContactsTv = (TextView) this
				.findViewById(R.id.cancel_order_detail_contacts);
		mOrderContactPhoneTv = (TextView) this
				.findViewById(R.id.cancel_order_detail_contact_phone);
		
		mTrackingBtn = (Button) this.findViewById(R.id.order_tracking_btn);
		mTrackingBtn.setOnClickListener(this);

		mOrderIdTv.setText(String.valueOf(mOrderId));
		mDetailOrderIdTv.setText(String.valueOf(mOrderId));
	}

	/**
	 * 获取订单信息
	 * 
	 */
	private void getOrderData() {
		OrderCancelDetailData data = new OrderCancelDetailData();
		data.setOrderId(String.valueOf(mOrderId));
		data.setUserId(MyApplication.getUser().getUserId());
		String dataString = MyApplication.getGson().toJson(data);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);
		Log.i(dataString);
		post(ORDER_CANCEL_DETAIL_REQUEST_CODE,
				Constants.GET_ORDER_CANNL_INFO_URL, params);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == ORDER_CANCEL_DETAIL_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.optString("msg");
				if (code == 2000) {
					String jsonString = object.getString("orderInfo");
					mOrderCancelDetail = MyApplication.getGson().fromJson(
							jsonString, OrderCancelDetail.class);
					showOrderData();
				} else {
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 显示数据
	 */
	private void showOrderData() {
		mOrderStatusTv.setText(Constants.orderStatus(Integer
				.valueOf(mOrderCancelDetail.getOrderStatus())));

		mOrderPayDetailTv
				.setText("支付金额" + mOrderCancelDetail.getHadPay() + "元");
		List<OrderLog> logs = mOrderCancelDetail.getOrderLogs();
		for (OrderLog orderLog : logs) {
			if (orderLog.getNewOrderStatus() == 200) {
				mOrderApplyReasonTv.setText(orderLog.getRemark());
				break;
			}
		}
		mOrderContactsTv.setText(mOrderCancelDetail.getReceiverName());
		mOrderContactPhoneTv.setText(mOrderCancelDetail.getReceiverPhone());
		
		mAdapter = new CancelProgressAdapter(this, logs);
		mListView.setAdapter(mAdapter);
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
		case R.id.title_left_btn:
			this.finish();
			break;
		case R.id.order_tracking_btn:
			Intent intent = new Intent(this, OrderTrackingActivity.class);
			intent.putExtra("orderId", mOrderId);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("CancelOrderDetailActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("CancelOrderDetailActivity");
		MobclickAgent.onPause(this);
	}
}
