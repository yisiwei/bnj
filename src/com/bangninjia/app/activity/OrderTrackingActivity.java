package com.bangninjia.app.activity;

import java.util.ArrayList;
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
import com.bangninjia.app.adapter.OrderTrackingAdapter;
import com.bangninjia.app.model.OrderNodeData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.DateUtil;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单跟踪
 * 
 */
public class OrderTrackingActivity extends BaseNetActivity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mOrderIdTv;

	private ListView mOrderTrackingView;
	private OrderTrackingAdapter mAdapter;

	private static final int GET_ORDER_NODE_REQUEST_CODE = 1003;

	private MyProgressDialog mProgressDialog;

	private int mOrderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_tracking);

		Intent intent = getIntent();
		mOrderId = intent.getIntExtra("orderId", 0);

		initView();

		mProgressDialog = new MyProgressDialog(this, "正在加载");

		// 获取订单时间节点信息
		getOrderNode();
	}

	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.order_tracking);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mOrderIdTv = (TextView) this.findViewById(R.id.order_tracking_orderid);
		mOrderTrackingView = (ListView) this
				.findViewById(R.id.order_tracking_listview);

		mOrderIdTv.setText(String.valueOf(mOrderId));
	}

	/**
	 * 获取订单时间节点信息
	 */
	private void getOrderNode() {
		OrderNodeData data = new OrderNodeData();
		data.setOrderId(String.valueOf(mOrderId));
		data.setUserId(MyApplication.getUser().getUserId());

		String dataString = MyApplication.getGson().toJson(data);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);
		post(GET_ORDER_NODE_REQUEST_CODE, Constants.GET_ORDER_NODE_URL, params);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == GET_ORDER_NODE_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				String msg = object.optString("msg");
				if (object.optInt("code") == 2000) {
					JSONObject infos = object.getJSONObject("orderInfo")
							.getJSONObject("result");
					Log.i(infos.toString());
					List<String> contents = new ArrayList<String>();
					List<String> datetime = new ArrayList<String>();
					if (infos.has("createDate")) {
						datetime.add(DateUtil.parseLongDate(infos
								.optLong("createDate")));
						contents.add("订单已提交，等待付款");
					}
					if (infos.has("depositDate")) {
						datetime.add(DateUtil.parseLongDate(infos
								.optLong("depositDate")));
						contents.add("付款成功，等待系统确认");
					}
					if (infos.has("sendDate")) {
						datetime.add(DateUtil.parseLongDate(infos
								.optLong("sendDate")));
						contents.add("系统已派单，等待作业人员上门");
					}
//					if (infos.has("serviceDate")) {
//						datetime.add(DateUtil.parseLongDate(infos
//								.optLong("serviceDate")));
//						contents.add("用户修改订单");
//					}
					if (infos.has("userConfirmDate")) {
						datetime.add(DateUtil.parseLongDate(infos
								.optLong("userConfirmDate")));
						contents.add("用户修改订单，再次确认完成");
					}
					if (infos.has("workerConfirmDate")) {
						datetime.add(DateUtil.parseLongDate(infos
								.optLong("workerConfirmDate")));
						contents.add("作业人员已确定");
					}
					if (infos.has("finishDate")) {
						datetime.add(DateUtil.parseLongDate(infos
								.optLong("finishDate")));
						contents.add("已评价，订单完成");
					}
					if (infos.has("cancelDate")) {
						datetime.add(DateUtil.parseLongDate(infos
								.optLong("cancelDate")));
						contents.add("用户申请取消订单");
					}
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					for (int i = 0; i < contents.size(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", contents.get(i));
						map.put("datetime", datetime.get(i));
						list.add(map);
					}
					mAdapter = new OrderTrackingAdapter(this, list);
					mOrderTrackingView.setAdapter(mAdapter);

				} else {
					Toast.show(this, msg);
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
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderTrackingActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderTrackingActivity");
		MobclickAgent.onPause(this);
	}

}
