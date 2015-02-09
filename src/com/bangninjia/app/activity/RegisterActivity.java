package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.RegisterData;
import com.bangninjia.app.model.ResultData;
import com.bangninjia.app.model.SmsData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Md5Util;
import com.bangninjia.app.utils.RegexUtil;
import com.bangninjia.app.utils.ResultDataUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.umeng.analytics.MobclickAgent;

public class RegisterActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private EditText mPhoneView;
	private EditText mPasswordView;
	private EditText mConfirmPwdView;
	private EditText mCodeView;

	private Button mRegisterBtn;
	private Button mGetCodeBtn;
	private CheckBox mReadAndAgreeCb;
	private TextView mUerAgreementView;

	private static final int GETCODE_REQUEST_CODE = 1000;
	private static final int REGISTER_REQUEST_CODE = 1001;

	private boolean mFlag = false;
	private int mTime = 60; // 重新获取短信验证码的时限

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();
	}

	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.register);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		mPhoneView = (EditText) this.findViewById(R.id.register_phone);
		mPasswordView = (EditText) this.findViewById(R.id.register_pwd);
		mConfirmPwdView = (EditText) this
				.findViewById(R.id.register_confirm_pwd);
		mCodeView = (EditText) this.findViewById(R.id.register_code);

		mRegisterBtn = (Button) this.findViewById(R.id.register_btn);
		mGetCodeBtn = (Button) this.findViewById(R.id.register_getCode_btn);
		mReadAndAgreeCb = (CheckBox) this.findViewById(R.id.register_checkBox);
		mUerAgreementView = (TextView) this
				.findViewById(R.id.register_user_agreement);

		mRegisterBtn.setOnClickListener(this);
		mGetCodeBtn.setOnClickListener(this);
		mUerAgreementView.setOnClickListener(this);

	}

	/**
	 * 注册
	 */
	private void register() {

		String phone = mPhoneView.getText().toString();
		String password = mPasswordView.getText().toString();
		String confirmPwd = mConfirmPwdView.getText().toString();
		String code = mCodeView.getText().toString();

		if (StringUtils.isNullOrEmpty(phone)) {
			Toast.show(this, "请输入手机号");
			return;
		}
		if (!RegexUtil.isMobile(phone)) {
			Toast.show(this, "请输入正确的手机号");
			return;
		}
		if (StringUtils.isNullOrEmpty(password)) {
			Toast.show(this, "请输入密码");
			return;
		}
		if (RegexUtil.checkPassword(password)) {
			Toast.show(this, "密码不能为纯数字或纯字母");
			return;
		}
		if (password.trim().length() < 8 || password.trim().length() > 16) {
			Toast.show(this, "密码长度为8-16位");
			return;
		}
		if (StringUtils.isNullOrEmpty(confirmPwd)) {
			Toast.show(this, "请确认密码");
			return;
		}
		if (!password.equals(confirmPwd)) {
			Toast.show(this, "两次密码输入不一致");
			return;
		}
		if (StringUtils.isNullOrEmpty(code)) {
			Toast.show(this, "请输入与验证码");
			return;
		}
		if (!mReadAndAgreeCb.isChecked()) {
			Toast.show(this, "您还未同意邦您家用户协议");
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		RegisterData data = new RegisterData();
		data.setUserName(phone);
		data.setPassword(Md5Util.md5(password));
		data.setKpac(code);
		data.setSource("2");
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i("MainActivity", "RegisterData==" + dataString);
		post(REGISTER_REQUEST_CODE, Constants.REGIST_SEND_INFO_URL, params);
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		String phone = mPhoneView.getText().toString();
		if (StringUtils.isNullOrEmpty(phone)) {
			Toast.show(this, "请输入手机号");
			return;
		}
		if (!RegexUtil.isMobile(phone)) {
			Toast.show(this, "请输入正确的手机号");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		SmsData data = new SmsData();
		data.setPhone(phone);
		data.setComefrom("reg");
		data.setSource("2");
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i("MainActivity", "SmsData==" + dataString);
		post(GETCODE_REQUEST_CODE, Constants.REGIST_SMS_VALIDE, params);
		showSmsTime();
	}

	private void showSmsTime() {
		mGetCodeBtn.setEnabled(false);
		mFlag = true;
		new Thread() {
			@Override
			public void run() {
				while (mFlag) {
					Message message = Message.obtain();
					message.what = GETCODE_REQUEST_CODE;
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
			if (msg.what == GETCODE_REQUEST_CODE) {

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
	public void onNetworkSuccess(int requestCode, String jsonData) {
		if (requestCode == GETCODE_REQUEST_CODE) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				String msg = jsonObject.getString("msg");
				Toast.show(this, msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == REGISTER_REQUEST_CODE) {
			ResultData data = ResultDataUtil.parse(jsonData);
			if (data.getCode().equals("1000")) {
				Toast.show(this, "注册成功");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
			} else{
				Toast.show(this, data.getMsg());
			}
		}
	}

	@Override
	public void onNetworkFinish(int requestCode) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			this.finish();
			break;
		case R.id.register_getCode_btn:// 获取短信验证码
			getCode();
			break;
		case R.id.register_btn:// 注册
			register();
			break;
		case R.id.register_user_agreement:// 用户协议
			startActivity(new Intent(RegisterActivity.this,
					UserAgreementActivity.class));
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("RegisterActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("RegisterActivity");
		MobclickAgent.onPause(this);
	}

}
