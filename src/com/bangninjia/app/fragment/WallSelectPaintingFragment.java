package com.bangninjia.app.fragment;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.bangninjia.R;
import com.bangninjia.app.adapter.ExpandableListAdapter;
import com.bangninjia.app.model.Brand;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.JsonUtils;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class WallSelectPaintingFragment extends BaseNetFragment {

	private ExpandableListView mWallBrandList;
	private ExpandableListAdapter mAdapter;

	private MyProgressDialog mProgressDialog;

	private String mPreImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wall_select_painting,
				container, false);

		mWallBrandList = (ExpandableListView) view
				.findViewById(R.id.wall_brand_list);
		mAdapter = new ExpandableListAdapter(getActivity());

		mWallBrandList.setGroupIndicator(null); // 去箭头
		mWallBrandList.setAdapter(mAdapter);

		// 设置只展开一个Group
		mWallBrandList.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < mAdapter.getGroupCount(); i++) {
					if (i != groupPosition && mWallBrandList.isGroupExpanded(i)) {
						mWallBrandList.collapseGroup(i);
					}
				}
			}
		});

		mProgressDialog = new MyProgressDialog(getActivity(), "正在获取数据...");
		initData();

		return view;
	}

	private void initData() {
		HashMap<String, String> params = new HashMap<String, String>();
		JSONObject obj = new JSONObject();
		try {
			obj.put("categoryId", Constants.BRAND_TYPE_WALL + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.put("data", obj.toString());

		post(Constants.REQUEST_CODE_BRAND, Constants.BRAND_PDT_SERVER, params);
	}

	public String getPreImg() {
		return mPreImg;
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i("MainActivity", "onNetworkSuccess");
		if (requestCode == Constants.REQUEST_CODE_BRAND) {
			List<Brand> data = JsonUtils.parseJsonOfBrand(jsonData);
			mPreImg = JsonUtils.getPreImg(jsonData);
			if (data == null) {
				Log.i("MainActivity", "--data--是空的");
			} else {
				// mAdapter.setPreimg(preImg);
				mAdapter.update(data);
				mAdapter.notifyDataSetChanged();
				// 设置默认展开第一组
				mWallBrandList.expandGroup(0, true);
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
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WallSelectPaintingFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WallSelectPaintingFragment");
	}
}
