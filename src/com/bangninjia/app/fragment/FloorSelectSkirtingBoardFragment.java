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
import com.bangninjia.app.adapter.SkirtingBoardAdapter;
import com.bangninjia.app.model.SkirtingBoard;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.JsonUtils;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class FloorSelectSkirtingBoardFragment extends BaseNetFragment {

	private ExpandableListView mSkirtingBoardList;
	private SkirtingBoardAdapter mAdapter;

	private MyProgressDialog mProgressDialog;
	
	private String mBrandId;

	private static final int SKIRTING_BOARD_REQUEST_CODE = 1008;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_floor_select_skirting_board,
						container, false);

		mSkirtingBoardList = (ExpandableListView) view
				.findViewById(R.id.skirting_board_list);

		mSkirtingBoardList.setGroupIndicator(null); // 去箭头

		mAdapter = new SkirtingBoardAdapter(getActivity());
		mSkirtingBoardList.setAdapter(mAdapter);

		// 设置只展开一个Group
		mSkirtingBoardList
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						for (int i = 0; i < mAdapter.getGroupCount(); i++) {
							if (i != groupPosition
									&& mSkirtingBoardList.isGroupExpanded(i)) {
								mSkirtingBoardList.collapseGroup(i);
							}
						}
					}
				});

		mProgressDialog = new MyProgressDialog(getActivity(), "正在加载");

		mBrandId = getArguments().getString("brandId");
		Log.i("MainActivity", "BrandId:" + mBrandId);
		initData();

		return view;
	}

	private void initData() {
		HashMap<String, String> params = new HashMap<String, String>();
		JSONObject obj = new JSONObject();
		try {
			obj.put("brandId", mBrandId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.put("data", obj.toString());

		post(SKIRTING_BOARD_REQUEST_CODE, Constants.SKIRTING_BOARD_SERVER,
				params);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		mProgressDialog.dismiss();
		Log.i("MainActivity", "jsonData" + jsonData);
		if (requestCode == SKIRTING_BOARD_REQUEST_CODE) {
			List<SkirtingBoard> data = JsonUtils
					.parseJsonOfSkirtingBoard(jsonData);
			String preImg = JsonUtils.getPreImg(jsonData);
			if (data == null) {
				Log.i("MainActivity", "--data--是空的");
			} else {
				mAdapter.setPreImg(preImg);
				mAdapter.update(data);
				mAdapter.notifyDataSetChanged();
				// 设置默认展开第一组
				mSkirtingBoardList.expandGroup(0, true);
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
		MobclickAgent.onPageEnd("FloorSelectSkirtingBoardFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FloorSelectSkirtingBoardFragment");
	}

}
