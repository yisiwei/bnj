package com.bangninjia.app.fragment;

import java.text.DecimalFormat;
import java.text.Format;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.bangninjia.R;
import com.bangninjia.app.model.DemandProperties;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

public class FloorHouseInfoFragment extends Fragment {

	private EditText mServiceAreaView;// 服务面积View
	private EditText mSkirtingBoardView;// 踢脚线长度View

	private Spinner mElevatorSpinner;// 电梯View
	private Spinner mStoreySpinner;// 楼层View

	// 服务类型View
	private CheckBox mServiceType1, mServiceType2, mServiceType3;

	private String[] mElevatorData = { "有电梯", "无电梯" };
	private String[] mStoreyData = { "一层", "二层", "三层", "四层", "五层", "六层", "七层" };

	private String mElevator;// 电梯
	private String mStorey;// 楼层
	private String mServiceType;// 服务类型

	private static Format mFormat = new DecimalFormat("0.00");
	private ArrayAdapter<String> mElevatorAdapter;
	private ArrayAdapter<String> mStoreyAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_floor_house_info,
				container, false);

		initView(view);

		return view;
	}

	/**
	 * 初始化
	 * 
	 * @param view
	 */
	private void initView(View view) {
		mServiceAreaView = (EditText) view.findViewById(R.id.floor_step1_area);
		mSkirtingBoardView = (EditText) view
				.findViewById(R.id.floor_step1_skirting_board);

		mElevatorSpinner = (Spinner) view
				.findViewById(R.id.floor_step1_spinner1);
		mStoreySpinner = (Spinner) view.findViewById(R.id.floor_step1_spinner2);

		mServiceType1 = (CheckBox) view
				.findViewById(R.id.floor_step1_service_type1);
		mServiceType2 = (CheckBox) view
				.findViewById(R.id.floor_step1_service_type2);
		mServiceType3 = (CheckBox) view
				.findViewById(R.id.floor_step1_service_type3);

		// 电梯
		mElevatorAdapter = new ArrayAdapter<>(getActivity(),
				R.layout.spinner_item, mElevatorData);
		mElevatorAdapter.setDropDownViewResource(R.layout.spinner_item);
		mElevatorSpinner.setAdapter(mElevatorAdapter);
		mElevatorSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						mElevator = mElevatorData[position];
						Log.i("MainActivity", "elevator position:" + position
								+ "--" + mElevatorData[position]);
						if (position == 0) {
							mStoreySpinner.setVisibility(View.GONE);
						} else {
							mStoreySpinner.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		// 楼层
		mStoreyAdapter = new ArrayAdapter<>(getActivity(),
				R.layout.spinner_item, mStoreyData);
		mStoreyAdapter.setDropDownViewResource(R.layout.spinner_item);
		mStoreySpinner.setAdapter(mStoreyAdapter);
		mStoreySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mStorey = mStoreyData[position];
				Log.i("MainActivity", "storey position:" + position + "--"
						+ mStoreyData[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// 服务类型
		mServiceType1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mServiceType2.setChecked(false);
					mServiceType3.setChecked(false);
					mServiceType = mServiceType1.getText().toString();
					Log.i("MainActivity", "服务类型：" + mServiceType);
				} else {
					mServiceType = null;
				}
			}
		});
		mServiceType2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mServiceType1.setChecked(false);
					mServiceType3.setChecked(false);
					mServiceType = mServiceType2.getText().toString();
					Log.i("MainActivity", "服务类型：" + mServiceType);
				} else {
					mServiceType = null;
				}
			}
		});
		mServiceType3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mServiceType1.setChecked(false);
					mServiceType2.setChecked(false);
					mServiceType = mServiceType3.getText().toString();
					Log.i("MainActivity", "服务类型：" + mServiceType);
				} else {
					mServiceType = null;
				}
			}
		});
		
		
		Bundle bundle = getArguments();
		DemandProperties properties = null;
		if (bundle != null) {//修改房屋信息
			properties = (DemandProperties)bundle.getSerializable("demandProperties");
			double area = bundle.getDouble("area");
			Log.i("属性信息："+properties.toString());
			mServiceAreaView.setText(mFormat.format(area));
			String elevator = properties.get电梯信息();
			if (elevator != null) {
				for (int i = 0; i < mElevatorData.length; i++) {
					if (elevator.equals(mElevatorData[i])) {
						mElevatorSpinner.setSelection(i);
						break;
					}
				}
			}
			String storey = properties.get楼层信息();
			if (storey != null) {
				if (!"有电梯".equals(elevator)) {
					mStoreySpinner.setVisibility(View.VISIBLE);
					for (int i = 0; i < mStoreyData.length; i++) {
						if (storey.equals(mStoreyData[i])) {
							mStoreySpinner.setSelection(i);
							break;
						}
					}
				}
			}
			String serviceType = properties.get服务类型();
			if ("安装地板".equals(serviceType)) {
				mServiceType1.setChecked(true);
			}else if("更新地板".equals(serviceType)){
				mServiceType2.setChecked(true);
			}else{
				mServiceType3.setChecked(true);
			}
			
		}
	}

	/**
	 * 获取服务面积
	 * 
	 * @return
	 */
	public double getServiceArea() {
		String serviceArea = mServiceAreaView.getText().toString();
		if (StringUtils.isNullOrEmpty(serviceArea)) {
			return 0;
		} else {
			return Double.valueOf(serviceArea);
		}
	}

	/**
	 * 获取踢脚线长度
	 * 
	 * @return
	 */
	public double getSkirtingBoard() {
		String skirtingBoard = mSkirtingBoardView.getText().toString();
		if (StringUtils.isNullOrEmpty(skirtingBoard)) {
			return 0;
		} else {
			return Double.valueOf(skirtingBoard);
		}
	}

	/**
	 * 属性
	 * 
	 * @return
	 */
	public DemandProperties getDemandProperties() {
		DemandProperties properties = new DemandProperties();
		properties.set电梯信息(mElevator);
		properties.set楼层信息(mStorey);
		properties.set服务类型(mServiceType);
		Log.i("MainActivity", "属性：" + properties);
		return properties;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FloorHouseInfoFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FloorHouseInfoFragment");
	}

}
