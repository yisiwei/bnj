package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.ResultData;
import com.bangninjia.app.model.SmsData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.ResultDataUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 忘记密码-获取短信验证码
 * 
 */
public class ForgetPwdGetCodeActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mPhoneTv;
	private EditText mCodeEt;
	private Button mGetCodeBtn;
	private Button mNextBtn;
	
	private String mPhone;
	private boolean mFlag = true;
	private int mTime = 60; // 重新获取短信验证码的时限
	
	private static final int GET_CODE_REQUEST_CODE = 2000;

	private MyProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd_getcode);

		MyApplication.getInstance().addActivity(this);

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

		mTitleText.setText(R.string.forget_password);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		mPhoneTv = (TextView) this.findViewById(R.id.forget_pwd_phone);
		mCodeEt = (EditText) this.findViewById(R.id.forget_pwd_code);

		mGetCodeBtn = (Button) this.findViewById(R.id.forget_pwd_getCode_btn);
		mNextBtn = (Button) this.findViewById(R.id.forget_pwd_next_btn);

		mGetCodeBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);

		Intent intent = getIntent();
		mPhone = intent.getStringExtra("phone");
		String phone = mPhone.replace(mPhone.substring(3, 7), "****");
		mPhoneTv.setText(phone);

		mProgressDialog = new MyProgressDialog(this, "正在加载");
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		Map<String, String> params = new HashMap<String, String>();

		SmsData data = new SmsData();
		data.setPhone(mPhone);
		data.setComefrom("pwd");
		data.setSource("2");
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i(dataString);
		post(GET_CODE_REQUEST_CODE, Constants.REGIST_SMS_VALIDE, params);
	}

	/**
	 * 下一步
	 */
	private void next() {
		String code = mCodeEt.getText().toString();

		if (StringUtils.isNullOrEmpty(code)) {
			Toast.show(this, "请输入验证码");
			return;
		}
		Intent intent = new Intent(ForgetPwdGetCodeActivity.this,
				ResetPwdActivity.class);
		intent.putExtra("phone", mPhone);
		intent.putExtra("code", code);
		startActivity(intent);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == GET_CODE_REQUEST_CODE) {
			ResultData data = ResultDataUtil.parse(jsonData);
			if(data.getCode().equals("1000")){
				showValiteTime();
			}
			Toast.show(this,data.getMsg());
		}
	}
	
	private void showValiteTime() {
		mGetCodeBtn.setEnabled(false);
		mFlag = true;
		new Thread() {
			@Override
			public void run() {
				while (mFlag) {
					Message message = Message.obtain();
					message.what = GET_CODE_REQUEST_CODE;
					message.arg1 = mTime;
					mHandler.sendMessage(message);
					try {
						Thread.sleep(1000);
						mTime--;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == GET_CODE_REQUEST_CODE) {
				if (msg.arg1 == 0) {
					mFlag = false;
					mTime = 60;
					mGetCodeBtn.setText("重新获取验证码");
					mGetCodeBtn.setEnabled(true);
				} else {
					mGetCodeBtn.setText(msg.arg1 + "秒后重新获取");
				}
			}

		};
	};

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.forget_pwd_getCode_btn:// 获取验证码
			getCode();
			break;
		case R.id.forget_pwd_next_btn:// 下一步
			next();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ForgetPwdGetCodeActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ForgetPwdGetCodeActivity");
		MobclickAgent.onPause(this);
	}

}
