package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.CommentData;
import com.bangninjia.app.model.ResultData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.ResultDataUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 服务评价
 * 
 */
public class ServiceCommentActivity extends BaseNetActivity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private RadioGroup mRadioGroup;
	private EditText mContentView;
	private Button mSubmitBtn;

	private String mSatisfaction;

	private String mOrderId;
	private boolean mIsComment = false;

	private static final int COMMENT_REQUEST_CODE = 1001;

	private MyProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_comment);

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

		mRadioGroup = (RadioGroup) this.findViewById(R.id.comment_radioGroup);
		mContentView = (EditText) this.findViewById(R.id.comment_content);
		mSubmitBtn = (Button) this.findViewById(R.id.comment_btn);

		// 获取传递过来的orderId
		mOrderId = String.valueOf(getIntent().getIntExtra("orderId", 0));

		mSubmitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});

		mProgressDialog = new MyProgressDialog(this, "正在提交");

	}

	private void getGrade() {
		int id = mRadioGroup.getCheckedRadioButtonId();
		switch (id) {
		case R.id.comment_very_satisfaction:
			mSatisfaction = "0";
			break;
		case R.id.comment_satisfaction:
			mSatisfaction = "1";
			break;
		case R.id.comment_dissatisfaction:
			mSatisfaction = "2";
			break;
		default:
			break;
		}
	}

	private void submit() {
		String content = mContentView.getText().toString();
		getGrade();
		if (StringUtils.isNullOrEmpty(mSatisfaction)) {
			Toast.show(this, "请选择满意度");
			return;
		}
		if (StringUtils.isNullOrEmpty(content)) {
			Toast.show(this, "请输入评语");
			return;
		}

		CommentData data = new CommentData();
		data.setUserId(MyApplication.getUser().getUserId());
		data.setOrderId(mOrderId);
		data.setGrade(mSatisfaction);
		data.setContent(content);

		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);

		if (!mIsComment) {
			post(COMMENT_REQUEST_CODE, Constants.EVAL_ORDER_URL, params);
		} else {
			Toast.show(this, "您已经提交评论了，请勿宠物提交");
		}

	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == COMMENT_REQUEST_CODE) {
			ResultData data = ResultDataUtil.parse(jsonData);

			if (data.getCode().equals("2000")) {
				mIsComment = true;
				Toast.show(this, "提交成功");
				setResult(RESULT_OK);
				this.finish();
			} else {
				Toast.show(this, data.getMsg());
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
		Toast.show(this, "请检查您的网络");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ServiceCommentActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ServiceCommentActivity");
		MobclickAgent.onPause(this);
	}

}
