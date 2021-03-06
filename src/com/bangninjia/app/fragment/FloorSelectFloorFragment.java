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
import android.widget.ExpandableListView.OnGroupClickListener;

import com.bangninjia.R;
import com.bangninjia.app.adapter.FloorListAdapter;
import com.bangninjia.app.model.Brand;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.JsonUtils;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 选择地板
 * 
 */
public class FloorSelectFloorFragment extends BaseNetFragment {

	private ExpandableListView mFloorList;
	private FloorListAdapter mAdapter;

	private MyProgressDialog mProgressDialog;

	private int sign = -1;// 控制列表的展开
	
	private String mPreImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_floor_select_floor,
				container, false);

		mFloorList = (ExpandableListView) view.findViewById(R.id.floor_list);

		mAdapter = new FloorListAdapter(getActivity());

		mFloorList.setGroupIndicator(null); // 去箭头
		mFloorList.setAdapter(mAdapter);

		// 设置只展开一个Group
		// mFloorList.setOnGroupExpandListener(new OnGroupExpandListener() {
		//
		// @Override
		// public void onGroupExpand(int groupPosition) {
		// for (int i = 0; i < mAdapter.getGroupCount(); i++) {
		// if (i != groupPosition && mFloorList.isGroupExpanded(i)) {
		// mFloorList.collapseGroup(i);
		// }
		// }
		// }
		// });

		// 只展开一个group的实现方法
		mFloorList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (sign == -1) {
					// 展开被选的group
					mFloorList.expandGroup(groupPosition);
					// 设置被选中的group置于顶端
					mFloorList.setSelectedGroup(groupPosition);
					sign = groupPosition;
				} else if (sign == groupPosition) {
					mFloorList.collapseGroup(sign);
					sign = -1;
				} else {
					mFloorList.collapseGroup(sign);
					// 展开被选的group
					mFloorList.expandGroup(groupPosition);
					// 设置被选中的group置于顶端
					mFloorList.setSelectedGroup(groupPosition);
					sign = groupPosition;
				}
				return true;
			}
		});

		mProgressDialog = new MyProgressDialog(getActivity(), "正在加载");

		initData();

		return view;
	}
	
	public String getPreImg(){
		return mPreImg;
	}

	private void initData() {
		HashMap<String, String> params = new HashMap<String, String>();
		JSONObject obj = new JSONObject();
		try {
			obj.put("categoryId", Constants.BRAND_TYPE_FLOOR + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.put("data", obj.toString());

		post(Constants.REQUEST_CODE_BRAND, Constants.BRAND_PDT_SERVER, params);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		mProgressDialog.dismiss();
		if (requestCode == Constants.REQUEST_CODE_BRAND) {
			List<Brand> data = JsonUtils.parseJsonOfBrand(jsonData);
			mPreImg = JsonUtils.getPreImg(jsonData);
			if (data == null) {
				Log.i("MainActivity", "--data--是空的");
			} else {
				mAdapter.setPreImg(mPreImg);
				mAdapter.update(data);
				mAdapter.notifyDataSetChanged();
				// 设置默认展开第一组
				mFloorList.expandGroup(0, true);
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
		Toast.show(getActivity(), "加载失败，请检查您的网络");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FloorSelectFloorFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FloorSelectFloorFragment");
	}

}
