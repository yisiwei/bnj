package com.bangninjia.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.Address;
import com.bangninjia.app.model.AddressData;
import com.bangninjia.app.model.City;
import com.bangninjia.app.model.Road;
import com.bangninjia.app.utils.CityPullParseXml;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class EditAddressActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private MyProgressDialog mProgressDialog;

	private EditText mNameEt;
	private EditText mPhoneEt;
	private TextView mProvinceCityDistrictTv;
	private TextView mRingTownTv;
	private EditText mDetailAddressEt;

	private String mOption;
	private Address mAddress;

	private Address mNewAddress;

	private List<String> mProvinces;
	private List<City> mCities;
	private List<Road> mRoads;

	private List<String> mCityNames;
	private List<String> mAreaNames;
	private List<String> mRoadNames;

	private String mProvince;
	private String mCity;
	private String mArea;
	private String mRoad;

	private static final int SAVE_ADDRESS_REQUEST_CODE = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_address);

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
		mTitleRightBtn.setText("保存");
		mTitleRightBtn.setOnClickListener(this);

		mNameEt = (EditText) this.findViewById(R.id.name);
		mPhoneEt = (EditText) this.findViewById(R.id.phone);
		mProvinceCityDistrictTv = (TextView) this
				.findViewById(R.id.province_city_district);
		mRingTownTv = (TextView) this.findViewById(R.id.ring_town);
		mDetailAddressEt = (EditText) this.findViewById(R.id.detail_address);

		mProvinceCityDistrictTv.setOnClickListener(this);
		mRingTownTv.setOnClickListener(this);

		Intent intent = getIntent();
		mOption = intent.getStringExtra("option");
		mAddress = (Address) intent.getSerializableExtra("Address");
		Log.i("MainActivity", "option==" + mOption);
		if (mOption.equals("edit")) {
			mTitleText.setText(R.string.eidt_address);

			mNameEt.setText(mAddress.getName());
			mPhoneEt.setText(mAddress.getMobile());
			mProvinceCityDistrictTv.setText(mAddress.getProvince()
					+ mAddress.getCity() + mAddress.getArea());
			mRingTownTv.setText(mAddress.getRoad());
			mDetailAddressEt.setText(mAddress.getAddress());
		} else {
			mTitleText.setText(R.string.new_address);
		}

		mProgressDialog = new MyProgressDialog(this, "");
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
		if (requestCode == SAVE_ADDRESS_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.optString("msg");
				if (code == 2000) {
					Toast.show(this, "保存成功");
					Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("Address", mNewAddress);
					data.putExtras(bundle);
					setResult(RESULT_OK, data);
					this.finish();
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
		Toast.show(this, "保存失败，请检查您的网络");
	}

	private void saveNewAddress(AddressData data) {
		mNewAddress = new Address();

		mNewAddress.setId(data.getId());
		mNewAddress.setName(data.getName());
		mNewAddress.setMobile(data.getPhone());

		mNewAddress.setProvince(data.getProvince());
		mNewAddress.setCity(data.getCity());
		mNewAddress.setArea(data.getArea());

		mNewAddress.setRoad(data.getRoad());
		mNewAddress.setAddress(data.getAddress());
		mNewAddress.setUserId(data.getUserId());
	}

	/**
	 * 保存地址
	 */
	private void saveAddress() {

		String name = mNameEt.getText().toString();
		String phone = mPhoneEt.getText().toString();
		String addressString = mDetailAddressEt.getText().toString();
		String road = mRingTownTv.getText().toString();
		String provinceCityArea = mProvinceCityDistrictTv.getText().toString();
		if (StringUtils.isNullOrEmpty(name)) {
			Toast.show(this, "请输入姓名");
			return;
		}
		if (StringUtils.isNullOrEmpty(phone)) {
			Toast.show(this, "请输入手机号");
			return;
		}
		if (StringUtils.isNullOrEmpty(provinceCityArea)) {
			Toast.show(this, "请选择省、市、区");
			return;
		}
		if (StringUtils.isNullOrEmpty(road)) {
			Toast.show(this, "请选择环线");
			return;
		}
		if (StringUtils.isNullOrEmpty(addressString)) {
			Toast.show(this, "请输入详细地址");
			return;
		}

		AddressData data = new AddressData();

		if (mOption.equals("edit")) {
			data.setId(mAddress.getId());
		}
		data.setName(name);
		data.setPhone(phone);

		data.setProvince(mProvince);
		data.setCity(mCity);
		data.setArea(mArea);

		data.setRoad(mRoad);
		data.setAddress(addressString);
		data.setUserId(MyApplication.getUser().getUserId());

		saveNewAddress(data);

		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		post(SAVE_ADDRESS_REQUEST_CODE, Constants.SAVE_USER_ADDRESS_URL, params);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			this.finish();
			break;
		case R.id.province_city_district:// 省市区
			showProvinceCityDistrictDialog();
			break;
		case R.id.ring_town:// 环镇
			showRoadDialog();
			break;
		case R.id.title_right_btn:// 保存
			saveAddress();
			break;
		default:
			break;
		}
	}

	private void initProvince() {
		mProvinces = new ArrayList<String>();
		mProvinces.add("北京市");
	}

	/**
	 * 省市区Dialog
	 */
	private void showProvinceCityDistrictDialog() {
		View view = View.inflate(this, R.layout.dialog_city, null);

		Spinner provinceSpn = (Spinner) view.findViewById(R.id.spn_provice);
		final Spinner citySpn = (Spinner) view.findViewById(R.id.spn_city);
		final Spinner areaSpn = (Spinner) view.findViewById(R.id.spn_area);

		TextView confirm = (TextView) view.findViewById(R.id.confirm);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);

		final AlertDialog mDialog = new AlertDialog.Builder(this).create();
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setContentView(view);
		mDialog.getWindow().setLayout(600, LayoutParams.WRAP_CONTENT);

		initProvince();

		final ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(
				this, R.layout.spinner_item, mProvinces);
		provinceAdapter.setDropDownViewResource(R.layout.spinner_item);
		provinceSpn.setAdapter(provinceAdapter);

		// 省份spinner
		provinceSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("MainActivity", mProvinces.get(position).toString());
				mProvince = mProvinces.get(position);

				try {
					mCities = CityPullParseXml.getCities(
							getApplicationContext(), mProvince);
					mCityNames = new ArrayList<String>();
					for (int i = 0; i < mCities.size(); i++) {
						mCityNames.add(mCities.get(i).getCityName());
					}
					ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
							EditAddressActivity.this, R.layout.spinner_item,
							mCityNames);
					cityAdapter.setDropDownViewResource(R.layout.spinner_item);
					citySpn.setAdapter(cityAdapter);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// 城市spinner
		citySpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("MainActivity", "mCityNames==" + mCityNames.get(position));

				mCity = mCityNames.get(position);

				mAreaNames = new ArrayList<String>();
				mAreaNames = mCities.get(position).getAreaList();

				ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(
						EditAddressActivity.this, R.layout.spinner_item,
						mAreaNames);
				areaAdapter.setDropDownViewResource(R.layout.spinner_item);
				areaSpn.setAdapter(areaAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// 区县spinner
		areaSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("MainActivity", "mAreaName==" + mAreaNames.get(position));
				mArea = mAreaNames.get(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// 确认
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				mProvinceCityDistrictTv.setText(mProvince + mCity + mArea);
				try {
					mRoads = CityPullParseXml.getRoads(getApplicationContext(),
							mCity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// 取消
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

	}

	/**
	 * 环/镇dialog
	 */
	private void showRoadDialog() {
		if (mRoads == null) {
			Toast.show(this, "请先选择城区");
			return;
		}

		View view = View.inflate(this, R.layout.dialog_road, null);
		Spinner roadSpn = (Spinner) view.findViewById(R.id.spn_road);
		TextView confirm = (TextView) view.findViewById(R.id.confirm);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);

		final AlertDialog mDialog = new AlertDialog.Builder(this).create();
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setContentView(view);
		mDialog.getWindow().setLayout(600, LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < mRoads.size(); i++) {
			if (mArea.equals(mRoads.get(i).getAreaName())) {
				mRoadNames = mRoads.get(i).getRoadList();
				break;
			}
		}

		ArrayAdapter<String> roadAdapter = new ArrayAdapter<>(this,
				R.layout.spinner_item, mRoadNames);
		roadAdapter.setDropDownViewResource(R.layout.spinner_item);
		roadSpn.setAdapter(roadAdapter);

		roadSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("MainActivity", mRoadNames.get(position));
				mRoad = mRoadNames.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				mRingTownTv.setText(mRoad);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("EditAddressActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("EditAddressActivity");
		MobclickAgent.onPause(this);
	}

}
