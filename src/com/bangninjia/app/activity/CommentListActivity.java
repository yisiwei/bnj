package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.adapter.CommentAdapter;
import com.bangninjia.app.model.CommentResultData;
import com.bangninjia.app.model.CommentResult;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

/**
 * 服务评价列表
 * 
 */
public class CommentListActivity extends BaseNetActivity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;
	
	private LinearLayout mNoCommentLayout;
	private Button mNoCommentBtn;

	private ListView mCommentListView;
	private CommentAdapter mAdapter;

	private MyProgressDialog mProgressDialog;

	private static final int COMMENT_REQUEST_CODE = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.service_comment);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mNoCommentLayout = (LinearLayout) this.findViewById(R.id.comment_null_layout);
		mNoCommentBtn = (Button) this.findViewById(R.id.comment_go_comment_btn);
		
		mNoCommentBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goComment();
			}
		});
		
		mCommentListView = (ListView) this.findViewById(R.id.comment_list);

		mProgressDialog = new MyProgressDialog(this, "正在加载");

		CommentResultData data = new CommentResultData();
		data.setUserId(MyApplication.getUser().getUserId());
		data.setStatus("all");
		data.setPageNum(1);
		data.setLimit(10);

		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i("MainActivity", "CommentResultData==" + dataString);
		post(COMMENT_REQUEST_CODE, Constants.ORDER_COMMENT_URL, params);
	}

	/**
	 * 显示数据
	 * @param comments
	 */
	private void showData(List<CommentResult> commentItems) {
		if (commentItems == null || commentItems.size() <= 0) {
			mNoCommentLayout.setVisibility(View.VISIBLE);
		} else {
			mNoCommentLayout.setVisibility(View.GONE);
			mAdapter = new CommentAdapter(this, commentItems);
			mCommentListView.setAdapter(mAdapter);
		}
	}
	
	/**
	 * 去评价
	 */
	private void goComment(){
		setResult(RESULT_OK);
		this.finish();
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
		if (requestCode == COMMENT_REQUEST_CODE) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 2000) {
					JSONArray array = jsonObject.optJSONObject("result")
							.optJSONArray("result");
					Log.i("========="+array.toString());
					TypeToken<List<CommentResult>> toTypeToken = new TypeToken<List<CommentResult>>() {
					};
					List<CommentResult> commentItems = MyApplication.getGson().fromJson(
							array.toString(), toTypeToken.getType());
					showData(commentItems);
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
		Toast.show(this, "加载失败，请检查您的网络");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("CommentListActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("CommentListActivity");
		MobclickAgent.onPause(this);
	}

}
