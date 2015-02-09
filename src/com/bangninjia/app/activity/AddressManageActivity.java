package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.adapter.AddressAdapter;
import com.bangninjia.app.lib.swipmenu.SwipeMenu;
import com.bangninjia.app.lib.swipmenu.SwipeMenuCreator;
import com.bangninjia.app.lib.swipmenu.SwipeMenuItem;
import com.bangninjia.app.lib.swipmenu.SwipeMenuListView;
import com.bangninjia.app.lib.swipmenu.SwipeMenuListView.OnMenuItemClickListener;
import com.bangninjia.app.lib.swipmenu.SwipeMenuListView.OnSwipeListener;
import com.bangninjia.app.model.Address;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.DensityUtil;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

/**
 * 服务地址
 * 
 */
public class AddressManageActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mAddressTv;// 添加地址

	// private LinearLayout mAddressInfoLl;// 地址信息
	// private TextView mConsigneeTv;
	// private TextView mPhoneTv;
	// private TextView mAddressDetailTv;

	private SwipeMenuListView mAddressListView;
	private AddressAdapter mAdapter;

	private List<Address> mAddresses;

	private MyProgressDialog mProgressDialog;

	private static final int ADDRESS_REQUEST_CODE = 1001;
	private static final int EDIT_ADDRESS_REQUEST_CODE = 1002;
	private static final int ADD_ADDRESS_REQUEST_CODE = 1003;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_manage);

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

		mTitleLeftBtn.setOnClickListener(this);
		mTitleText.setText(R.string.address_manage);
		mTitleRightBtn.setVisibility(View.GONE);

		mAddressTv = (TextView) this.findViewById(R.id.address_add);
		mAddressTv.setOnClickListener(this);

		mAddressListView = (SwipeMenuListView) this
				.findViewById(R.id.address_listview);

		// mAddressInfoLl = (LinearLayout) this.findViewById(R.id.address_info);
		// mConsigneeTv = (TextView) this.findViewById(R.id.address_consignee);
		// mPhoneTv = (TextView) this.findViewById(R.id.address_phone);
		// mAddressDetailTv = (TextView) this.findViewById(R.id.address_detail);
		// mAddressInfoLl.setOnClickListener(this);

		String option = getIntent().getStringExtra("option");
		// 从确认订单跳转过来的 添加Item的点击事件
		if (null != option && "confirmOrder".equals(option)) {
			mAddressListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Address address = mAddresses.get(position);
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("Address", address);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		}

		initSwipeMenuCreator();

		mProgressDialog = new MyProgressDialog(this, "");

		// 获取服务地址
		getAddressFromServer();
	}

	// private List<Address> getData() {//测试数据
	// mAddresses = new ArrayList<Address>();
	// Address address1 = new Address(1, "1", "小不点", "18518753305", "北京市",
	// "北京市", "东城区", "二环到三环里", "雍和大厦");
	// Address address2 = new Address(2, "2", "张三", "18518753306", "北京市",
	// "北京市", "西城区", "二环到三环里", "西城大厦");
	// Address address3 = new Address(3, "3", "张三", "18518753307", "北京市",
	// "北京市", "朝阳区", "二环到三环里", "朝阳大厦");
	//
	// mAddresses.add(address1);
	// mAddresses.add(address2);
	// mAddresses.add(address3);
	// return mAddresses;
	// }

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	private void initSwipeMenuCreator() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem editItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				editItem.setBackground(R.color.title_color);
				editItem.setTitle("编辑");
				editItem.setTitleColor(getResources().getColor(
						R.color.TextColorWhite));
				editItem.setTitleSize(DensityUtil.px2dp(
						getApplicationContext(), 40));
				editItem.setWidth(dp2px(100));

				menu.addMenuItem(editItem);
			}
		};
		mAddressListView.setMenuCreator(creator);
		mAddressListView
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {

						// Address adress = mAddresses.get(position);
						// Intent intent = new
						// Intent(AddressManageActivity.this,
						// EditAddressActivity.class);
						// intent.putExtra("title", "编辑地址");
						// intent.putExtra("type", 2);
						// intent.putExtra("address", adress);
						// startActivityForResult(intent, 2);

						Address address = mAddresses.get(position);
						Intent intent = new Intent(getApplicationContext(),
								EditAddressActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("Address", address);
						bundle.putString("option", "edit");
						intent.putExtras(bundle);
						startActivityForResult(intent,
								EDIT_ADDRESS_REQUEST_CODE);

						return false;
					}
				});

		// set SwipeListener
		mAddressListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

	}

	/**
	 * 获取服务地址
	 */
	private void getAddressFromServer() {
		Map<String, String> params = new HashMap<String, String>();
		String dataString = "{ userId :'" + MyApplication.getUser().getUserId()
				+ "'}";
		params.put("data", dataString);
		post(ADDRESS_REQUEST_CODE, Constants.GET_USER_ADDRESS_URL, params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回按钮
			this.finish();
			break;
		case R.id.address_add:// 添加地址
			Intent add_intent = new Intent(AddressManageActivity.this,
					EditAddressActivity.class);
			add_intent.putExtra("option", "add");
			startActivityForResult(add_intent, ADD_ADDRESS_REQUEST_CODE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == EDIT_ADDRESS_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// mAddress = (Address) data.getSerializableExtra("Address");
				// Log.i("MainActivit", "回传：" + mAddress.toString());
				// showAddress(mAddress);
				getAddressFromServer();
			}
		} else if (requestCode == ADD_ADDRESS_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// mAddressTv.setVisibility(View.GONE);
				// mAddressInfoLl.setVisibility(View.VISIBLE);
				// Address address = (Address) data
				// .getSerializableExtra("Address");
				// Log.i("MainActivit", "回传："+address.toString());
				// showAddress(address);
				getAddressFromServer();
			}
		}
	}

	/**
	 * 显示地址信息
	 */
	private void showAddress(List<Address> addresses) {
		mAdapter = new AddressAdapter(getApplicationContext(), addresses);
		mAddressListView.setAdapter(mAdapter);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		mProgressDialog.dismiss();
		Log.i("MainActivity", jsonData);
		if (requestCode == ADDRESS_REQUEST_CODE) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 2000) {
					JSONArray array = jsonObject.optJSONObject("result")
							.optJSONArray("result");
					TypeToken<List<Address>> typeToken = new TypeToken<List<Address>>() {
					};
					mAddresses = MyApplication.getGson().fromJson(
							array.toString(), typeToken.getType());
					if (mAddresses != null && mAddresses.size() > 0) {
						showAddress(mAddresses);
					}
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

	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		mProgressDialog.dismiss();
		Toast.show(this, "请检查您的网络");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AddressManageActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AddressManageActivity");
		MobclickAgent.onPause(this);
	}
	
}
