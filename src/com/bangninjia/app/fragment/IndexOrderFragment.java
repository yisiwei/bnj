package com.bangninjia.app.fragment;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.activity.CancelOrderDetailActivity;
import com.bangninjia.app.activity.OrderCancelActivity;
import com.bangninjia.app.activity.OrderDetailActivity;
import com.bangninjia.app.activity.OrderTrackingActivity;
import com.bangninjia.app.activity.OrderUpdateActivity;
import com.bangninjia.app.activity.ServiceCommentActivity;
import com.bangninjia.app.adapter.OrderAdapter;
import com.bangninjia.app.adapter.OrderAdapter.OnOrderListener;
import com.bangninjia.app.alipay.Keys;
import com.bangninjia.app.alipay.PayOrder;
import com.bangninjia.app.model.Alipay;
import com.bangninjia.app.model.Order;
import com.bangninjia.app.model.OrderChangeData;
import com.bangninjia.app.model.OrderListData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单
 * 
 */
public class IndexOrderFragment extends BaseNetFragment implements
		OnOrderListener, OnRefreshListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private LinearLayout mNoOrderLayout;
	private LinearLayout mNoNetworkLayout;
	private Button mNoNetworkBtn;

	private ListView mOrderListView;
	private List<Order> mOrders;
	private OrderAdapter mAdapter;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private MyProgressDialog myProgressDialog;

	private static final int GET_ORDER_LIST_REQUEST_CODE = 1000;
	private static final int DELETE_ORDER_REQUEST_CODE = 1001;
	private static final int CONFIRM_ORDER_REQUEST_CODE = 1002;
	private static final int PAY_ORDER_CODE = 1003;// 付款前调用接口

	private static final int CANCEL_ORDER_REQUEST_CODE = 2001;
	private static final int GO_ORDER_DETAIL_REQUEST_CODE = 2002;
	private static final int GO_COMMENT_REQUEST_CODE = 2003;

	 private static Format mFormat = new DecimalFormat("0.00");

	private int mDeleteOrderPosition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index_order, container,
				false);

		// title
		mTitleLeftBtn = (ImageButton) view.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) view.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) view.findViewById(R.id.title_right_btn);

		mTitleLeftBtn.setVisibility(View.GONE);
		mTitleText.setText(R.string.my_order);
		mTitleRightBtn.setVisibility(View.GONE);

		mNoOrderLayout = (LinearLayout) view.findViewById(R.id.no_order_layout);
		mNoNetworkLayout = (LinearLayout) view
				.findViewById(R.id.no_network_layout);
		mNoNetworkBtn = (Button) view.findViewById(R.id.no_network_btn);
		mNoNetworkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initData();
			}
		});

		mOrderListView = (ListView) view.findViewById(R.id.order_list);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.order_swipeRefreshLayout);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		myProgressDialog = new MyProgressDialog(getActivity(), "正在加载");

		initData();

		return view;
	}

	/**
	 * 查询数据
	 */
	private void initData() {
		OrderListData data = new OrderListData();

		data.setUserId(MyApplication.getUser().getUserId());
		data.setStatus("all");
		Integer comeFrom = Integer.valueOf(MyApplication.getComeFrom());
		switch (comeFrom) {
		case 1:
			data.setType("user");
			break;
		case 2:
			data.setType("vendor");// 商家
			break;
		case 3:
			data.setType("worker");
			break;
		default:
			break;
		}
		data.setPageNum(1);
		data.setLimit(100);

		String dataString = MyApplication.getGson().toJson(data);
		Log.i("MainActivity", "OrderListData==" + dataString);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);

		post(GET_ORDER_LIST_REQUEST_CODE, Constants.GET_ORDER_LIST_URL, params);

	}

	/**
	 * 显示数据
	 * 
	 * @param orders
	 */
	private void showOrderList() {
		mAdapter = new OrderAdapter(getActivity(), mOrders, this);
		mOrderListView.setAdapter(mAdapter);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		myProgressDialog.show();
		mNoNetworkLayout.setVisibility(View.GONE);
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == GET_ORDER_LIST_REQUEST_CODE) {// 获取订单列表
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 2000) {
					int pageCount = jsonObject.optJSONObject("result").getInt(
							"pageCount");
					Log.i("pageCount==" + pageCount);
					if (pageCount == 0) {// 没有订单信息
						mNoOrderLayout.setVisibility(View.VISIBLE);
					} else {
						JSONArray array = jsonObject.optJSONObject("result")
								.optJSONArray("result");
						Log.i("MainActivity",
								"order_list_json==" + array.toString());
						TypeToken<List<Order>> toTypeToken = new TypeToken<List<Order>>() {
						};
						mOrders = MyApplication.getGson().fromJson(
								array.toString(), toTypeToken.getType());
						if (mOrders != null && mOrders.size() > 0) {
							Log.i("MainActivity", "orders=======" + mOrders);
							showOrderList();
						} else {
							mNoOrderLayout.setVisibility(View.VISIBLE);
						}
					}
				} else {
					Toast.show(getActivity(), msg);
					mNoOrderLayout.setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (requestCode == DELETE_ORDER_REQUEST_CODE) {// 删除订单
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				if ("2000".equals(jsonObject.optString("code"))) {
					Toast.show(getActivity(), "删除成功");
					mOrders.remove(mDeleteOrderPosition);
					mAdapter.notifyDataSetChanged();
					// initData();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == CONFIRM_ORDER_REQUEST_CODE) {// 确认订单
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				if ("2000".equals(jsonObject.optString("code"))) {
					Toast.show(getActivity(), "确认成功");
					mOrders.remove(mDeleteOrderPosition);
					mAdapter.notifyDataSetChanged();
					initData();
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
//					alipay.setTotal_fee("0.01");// 支付金额
					alipay.setTotal_fee(map.get("total_fee").substring(1,
							map.get("total_fee").length() - 1));// 支付金额
					Log.i("支付参数：" + alipay.toString());
					// 去支付
					PayOrder payOrder = new PayOrder(getActivity(), alipay);
					payOrder.pay();

				} else {
					Toast.show(getActivity(), "网络错误，请稍后再试");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onNetworkFinish(int requestCode) {
		myProgressDialog.dismiss();
		mSwipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		Toast.show(getActivity(), "获取数据失败，请检查您的网络");
		mNoNetworkLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(int position) {
		// 去订单详情
		int status = mOrders.get(position).getOrderStatus();
		if (status == 200 || status == 201 || status == 202) {
			Intent intent = new Intent(getActivity(),
					CancelOrderDetailActivity.class);
			intent.putExtra("orderId", mOrders.get(position).getOrderId());
			startActivity(intent);
		} else {
			Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
			intent.putExtra("orderId", mOrders.get(position).getOrderId());
			startActivityForResult(intent, GO_ORDER_DETAIL_REQUEST_CODE);
		}
	}

	@Override
	public void onCancelClick(int position) {
		// 取消订单
		Intent intent = new Intent(getActivity(), OrderCancelActivity.class);
		intent.putExtra("orderId", mOrders.get(position).getOrderId());
		startActivityForResult(intent, CANCEL_ORDER_REQUEST_CODE);
	}

	@Override
	public void onUpdateClick(int position) {
		// 修改订单
		Intent intent = new Intent(getActivity(), OrderUpdateActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("order", mOrders.get(position));
		// intent.putExtras(bundle);
		intent.putExtra("orderId", mOrders.get(position).getOrderId());
		startActivity(intent);
	}

	@Override
	public void onPayClick(int position) {
		// 去支付
		// payOrder(position);
		getPayData(position);
	}

	@Override
	public void onDeleteClick(final int position) {
		// 删除订单
		View view = View.inflate(getActivity(), R.layout.dialog_delete, null);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		TextView confirm = (TextView) view.findViewById(R.id.confirm);

		final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity())
				.create();
		deleteDialog.setCancelable(false);
		deleteDialog.show();
		deleteDialog.setContentView(view);
		deleteDialog.getWindow().setLayout(600, LayoutParams.WRAP_CONTENT);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteOrder(position);
				deleteDialog.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
	}

	@Override
	public void onCommentClick(int position) {
		// 去评价
		Intent intent = new Intent(getActivity(), ServiceCommentActivity.class);
		intent.putExtra("orderId", mOrders.get(position).getOrderId());
		startActivityForResult(intent, GO_COMMENT_REQUEST_CODE);
	}

	@Override
	public void onTrackingClick(int position) {
		// 订单跟踪
		Intent intent = new Intent(getActivity(), OrderTrackingActivity.class);
		intent.putExtra("orderId", mOrders.get(position).getOrderId());
		Log.i("Position==" + position + ",OrderId====="
				+ mOrders.get(position).getOrderId());
		startActivity(intent);
	}

	/**
	 * 删除订单
	 */
	private void deleteOrder(int position) {
		OrderChangeData data = new OrderChangeData();
		data.setUserId(MyApplication.getUser().getUserId());
		data.setOrderId(String.valueOf(mOrders.get(position).getOrderId()));
		data.setOper("USER_DEL");

		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i(dataString);
		mDeleteOrderPosition = position;
		post(DELETE_ORDER_REQUEST_CODE, Constants.CHANGE_ORDER_URL, params);
	}

	/**
	 * 获取服务器支付数据
	 */
	private void getPayData(int position) {
		Map<String, String> params = new HashMap<String, String>();

		String userId = MyApplication.getUser().getUserId();
		String type = "10";// 10-付定金 20-付尾款
		String money = "0.01";
		double price = mOrders.get(position).getRealPay() * 0.3 ;
		if (price < 300) {
			money = "300.00";
		}else{
			money = mFormat.format(price);
		}
		params.put("data", mOrders.get(position).getOrderId() + "|" + money
				+ "|" + userId + "|" + type);
		Log.i(mOrders.get(position).getOrderId() + "|" + money + "|" + userId
				+ "|" + type);
		post(PAY_ORDER_CODE, Constants.PAY_SERVER_URL, params);
	}

	/**
	 * 去付款
	 */
	// private void payOrder(int position) {
	// Alipay alipay = new Alipay();
	// alipay.set_input_charset("utf-8");
	// alipay.setBody(mOrders.get(position).getServiceName()
	// + mOrders.get(position).getOrderProperties());
	// alipay.setSubject("订单号：" + mOrders.get(position).getOrderId() + "，"
	// + mOrders.get(position).getServiceName());
	// alipay.setIt_b_pay(Keys.PAY_TIME);
	//
	// alipay.setNotify_url(Constants.ALI_PAY_URL);
	// // alipay.setNotify_url(Keys.NOTIFY_URL);
	//
	// alipay.setOut_trade_no(mOrders.get(position).getOrderId() + "");// 订单号
	// alipay.setPartner(Keys.DEFAULT_PARTNER);
	// alipay.setPayment_type(Keys.PAYMENY_TYPE);
	// alipay.setSeller_id(Keys.DEFAULT_SELLER);
	// alipay.setService(Keys.SERVICE_URL);
	// Log.i("MainActivity",
	// "支付金额："
	// + mFormat
	// .format(mOrders.get(position).getRealPay() * 0.3));
	// // alipay.setTotal_fee(mFormat
	// // .format(mOrders.get(position).getRealPay() * 0.3));// 支付金额
	// alipay.setTotal_fee("0.01");// 支付金额
	// PayOrder payOrder = new PayOrder(getActivity(), alipay);
	// payOrder.pay();
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("回传");
		if (requestCode == CANCEL_ORDER_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Log.i("订单已申请取消");
				initData();// 刷新数据
			}
		} else if (requestCode == GO_ORDER_DETAIL_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				initData();// 刷新数据
			}
		}else if(requestCode == GO_COMMENT_REQUEST_CODE){
			if (resultCode == Activity.RESULT_OK) {
				initData();// 刷新数据
			}
		}
	}

	@Override
	public void onRefresh() {
		initData();
	}

	@Override
	public void onConfirmClick(final int position) {
		// 确认订单
		View view = View.inflate(getActivity(), R.layout.dialog_delete, null);
		TextView text = (TextView) view.findViewById(R.id.delete_text);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		TextView confirm = (TextView) view.findViewById(R.id.confirm);
		text.setText("确定要确认此订单吗？");

		final AlertDialog confirmDialog = new AlertDialog.Builder(getActivity())
				.create();
		confirmDialog.setCancelable(false);
		confirmDialog.show();
		confirmDialog.setContentView(view);
		confirmDialog.getWindow().setLayout(600, LayoutParams.WRAP_CONTENT);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmOrder(position);
				confirmDialog.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmDialog.dismiss();
			}
		});
	}

	/**
	 * 确认订单
	 */
	private void confirmOrder(int position) {
		OrderChangeData data = new OrderChangeData();
		data.setUserId(MyApplication.getUser().getUserId());
		data.setOrderId(String.valueOf(mOrders.get(position).getOrderId()));

		if (MyApplication.getComeFrom().equals("1")) {// 用户确认
			data.setOper("USER_CONFIRM");
		} else {// 作业人员确认
			data.setOper("WORKER_CONFIRM");
		}

		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i(dataString);
		post(CONFIRM_ORDER_REQUEST_CODE, Constants.CHANGE_ORDER_URL, params);
	}

	@Override
	public void onLookClick(int position) {
		// 查看订单
		// int status = mOrders.get(position).getOrderStatus();
		// if (status == 200 || status == 201 || status == 202) {
		// Intent intent = new Intent(getActivity(),
		// CancelOrderDetailActivity.class);
		// intent.putExtra("orderId", mOrders.get(position).getOrderId());
		// startActivity(intent);
		// } else {
		// Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
		// intent.putExtra("orderId", mOrders.get(position).getOrderId());
		// startActivityForResult(intent, GO_ORDER_DETAIL_REQUEST_CODE);
		// }

		Intent intent = new Intent(getActivity(), OrderTrackingActivity.class);
		intent.putExtra("orderId", mOrders.get(position).getOrderId());
		Log.i("Position==" + position + ",OrderId====="
				+ mOrders.get(position).getOrderId());
		startActivity(intent);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("IndexOrderFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("IndexOrderFragment");
	}

}
