package com.bangninjia.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.adapter.ColorListGridViewAdapter;
import com.bangninjia.app.model.Color;
import com.bangninjia.app.model.ColorData;
import com.bangninjia.app.model.ColorValue;
import com.bangninjia.app.model.OnColorListener;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class WallSelectColorFragment extends BaseNetFragment implements
		OnClickListener {

	/**
	 * 八大色系
	 */
	private TextView mColorTextRed, mColorTextOrange, mColorTextYellow,
			mColorTextGreen, mColorTextBlue, mColorTextPurple,
			mColorTextNeutral, mColorTextLightwhite;

	private TextView mFindSomeColor;// 共找到XX种颜色

	private List<Color> mColors;

	private Spinner mColorSpinner;
	private ArrayAdapter<String> mSpinnerAdapter;
	private int mSpinnerPosition = 0;

	private GridView mColorGridView;
	private ColorListGridViewAdapter mGridViewAdapter;
	// private int mCurrentGridViewPosition = 0;

	private String mBrandId;
	private String mColorSeriesId;

	private String[] mColorSeriesIds = { "101", "102", "103", "104", "105",
			"106", "107", "108" };

	private static final int COLOR_REQUEST = 1001;

	private MyProgressDialog mProgressDialog;
	
	private TextView mNoColorTv;

	private OnColorListener mColorListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wall_select_color,
				container, false);
		initView(view);

		mBrandId = getArguments().getString("brandId");
		Log.i("MainActivity", "BrandId:" + mBrandId);
		mColorSeriesId = mColorSeriesIds[0];// 默认为红色系

		getColorData();

		return view;
	}

	/**
	 * 初始化
	 * 
	 * @param view
	 */
	private void initView(View view) {
		mColorTextRed = (TextView) view.findViewById(R.id.wall_color_red);
		mColorTextOrange = (TextView) view.findViewById(R.id.wall_color_orange);
		mColorTextYellow = (TextView) view.findViewById(R.id.wall_color_yellow);

		mColorTextGreen = (TextView) view.findViewById(R.id.wall_color_green);
		mColorTextBlue = (TextView) view.findViewById(R.id.wall_color_blue);
		mColorTextPurple = (TextView) view.findViewById(R.id.wall_color_purple);

		mColorTextNeutral = (TextView) view
				.findViewById(R.id.wall_color_neutral);
		mColorTextLightwhite = (TextView) view
				.findViewById(R.id.wall_color_lightwhite);

		mFindSomeColor = (TextView) view.findViewById(R.id.find_some_color);
		mColorSpinner = (Spinner) view
				.findViewById(R.id.wall_color_colorChoice_sp);
		mColorGridView = (GridView) view.findViewById(R.id.wall_color_gridview);
		
		mNoColorTv = (TextView) view.findViewById(R.id.no_color);

		mColorTextRed.setOnClickListener(this);
		mColorTextOrange.setOnClickListener(this);
		mColorTextYellow.setOnClickListener(this);

		mColorTextGreen.setOnClickListener(this);
		mColorTextBlue.setOnClickListener(this);
		mColorTextPurple.setOnClickListener(this);

		mColorTextNeutral.setOnClickListener(this);
		mColorTextLightwhite.setOnClickListener(this);

		mSpinnerAdapter = new ArrayAdapter<>(getActivity(),
				R.layout.spinner_item, new ArrayList<String>());
		mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		mColorSpinner.setAdapter(mSpinnerAdapter);
		mColorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("MainActivity", "selected--->" + position);
				mGridViewAdapter.setData(mColors.get(position).getColors());
				mSpinnerPosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		mColorGridView.setSelector(getResources().getDrawable(
				R.drawable.gridview_item_press));
		mGridViewAdapter = new ColorListGridViewAdapter(getActivity(), null);
		mColorGridView.setAdapter(mGridViewAdapter);
		
		
		mColorGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// mCurrentGridViewPosition = position;

				ColorValue colorValue = mColors.get(mSpinnerPosition)
						.getColors().get(position);
				Log.i("MainActivity", "选择的颜色："+colorValue);
				String selectColor = colorValue.getColorName() + " "
						+ colorValue.getModelNum();
				mColorListener.onColorSelected(selectColor);
			}
		});

		mColors = new ArrayList<Color>();
		mColorListener = (OnColorListener) getActivity();

		mProgressDialog = new MyProgressDialog(getActivity(), "正在获取数据...");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wall_color_red:
			mColorSeriesId = mColorSeriesIds[0];
			break;
		case R.id.wall_color_orange:
			mColorSeriesId = mColorSeriesIds[1];
			break;
		case R.id.wall_color_yellow:
			mColorSeriesId = mColorSeriesIds[2];
			break;
		case R.id.wall_color_green:
			mColorSeriesId = mColorSeriesIds[3];
			break;
		case R.id.wall_color_blue:
			mColorSeriesId = mColorSeriesIds[4];
			break;
		case R.id.wall_color_purple:
			mColorSeriesId = mColorSeriesIds[5];
			break;
		case R.id.wall_color_neutral:
			mColorSeriesId = mColorSeriesIds[6];
			break;
		case R.id.wall_color_lightwhite:
			mColorSeriesId = mColorSeriesIds[7];
			break;
		default:
			mColorSeriesId = mColorSeriesIds[0];
			break;
		}
		getColorData();
	}

	/**
	 * 获取颜色
	 */
	private void getColorData() {
		ColorData colorData = new ColorData();
		colorData.setColorSeriesId(mColorSeriesId);
		colorData.setBrandId(mBrandId);

		String dataString = MyApplication.getGson().toJson(colorData);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);
		Log.i("MainActivity", "colorData-json:" + dataString);

		post(COLOR_REQUEST, Constants.GET_COLOR_URL, params);

	}

	// /**
	// * 选中的颜色
	// */
	// public String getColorSelected() {
	// // 获取当前选中的单个具体颜色值
	// ColorValue colorValue = mColors.get(mSpinnerPosition)
	// .getColors().get(mCurrentGridViewPosition);
	// return colorValue.getColorName() + "," + colorValue.getModelNum();
	// }

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i("MainActivity", "onNetworkSuccess--getColor--" + jsonData);

		if (requestCode == COLOR_REQUEST) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				if (code == 1000) {// 如果请求成功
					String jsonString = jsonObject.optString("colorTypes");
					JSONArray jsonArray = new JSONArray(jsonString);
					List<Color> colorList = new ArrayList<Color>();
					// 获取所有的颜色 --->spinner
					for (int i = 0; i < jsonArray.length(); i++) {
						Color color = null;
						JSONObject colorTypeObject = jsonArray.getJSONObject(i);
						JSONArray colorArray = colorTypeObject
								.getJSONArray("colors");
						// 获取对应所有的具体颜色 ---->gridview
						List<ColorValue> colorValues = new ArrayList<ColorValue>();
						for (int j = 0; j < colorArray.length(); j++) {
							JSONObject single = colorArray.getJSONObject(j);
							ColorValue colorValue = new ColorValue(
									single.optInt("id"),
									single.optString("colorName"),
									single.optString("modelNum"),
									single.optString("colorNum")

							);
							colorValues.add(colorValue);
						}
						color = new Color(colorTypeObject.optInt("id"),
								colorTypeObject.optString("typeName"),
								colorValues);
						colorList.add(color);
					}

					this.mColors = colorList;
					if (convertData(this.mColors).size() > 0) {
						mSpinnerAdapter.clear();
						mSpinnerAdapter.addAll(convertData(this.mColors));
						mGridViewAdapter.setData(mColors.get(0).getColors());
						mNoColorTv.setVisibility(View.GONE);
					} else {
						mNoColorTv.setVisibility(View.VISIBLE);
						mSpinnerAdapter.clear();
						mSpinnerAdapter.addAll(new ArrayList<String>());
						mGridViewAdapter.setData(null);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> convertData(List<Color> colors) {
		List<String> data = new ArrayList<String>();
		for (Color c : colors) {
			data.add(c.getTypeName());
		}
		mFindSomeColor
				.setText(String.format(
						getResources().getString(R.string.total_color),
						data.size(), 1));
		return data;
	}

	@Override
	public void onNetworkFinish(int requestCode) {
		Log.i("MainActivity", "onNetworkFinish--getColor");
		mProgressDialog.dismiss();
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		Log.i("MainActivity", "onNetworkFail--getColor");
		Toast.show(getActivity(), "获取失败，请检查您的网络");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WallSelectColorFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WallSelectColorFragment");
	}

	
}
