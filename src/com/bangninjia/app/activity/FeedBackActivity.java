package com.bangninjia.app.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.FeedBackData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 意见反馈
 * 
 */
public class FeedBackActivity extends BaseNetActivity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private EditText mFeedBackContent;
	private Button mSubmitBtn;

	private MyProgressDialog mProgressDialog;

	private static final int FEEDBACK_REQUEST_CODE = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.feedback);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mFeedBackContent = (EditText) this.findViewById(R.id.feedback_content);
		mSubmitBtn = (Button) this.findViewById(R.id.feedback_btn);

		mSubmitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});

		mProgressDialog = new MyProgressDialog(this, "正在提交");
	}

	/**
	 * 提交
	 */
	private void submit() {
		String content = mFeedBackContent.getText().toString();
		if (StringUtils.isNullOrEmpty(content)) {
			Toast.show(this, R.string.feedback_hint);
			return;
		}
		FeedBackData data = new FeedBackData(content, "2");
		HashMap<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i("MainActivity", "FeedBackData:" + dataString);
		post(FEEDBACK_REQUEST_CODE, Constants.SUGGESTION_FEED_BACK_URL, params);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i("MainActivity", jsonData);
		if (requestCode == FEEDBACK_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.optString("msg");
				if (code == 1000) {
					Toast.show(this, "提交成功");
					this.finish();
				}else{
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
		Toast.show(this, "提交失败，请检查您的网络");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FeedBackActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FeedBackActivity");
		MobclickAgent.onPause(this);
	}

}
