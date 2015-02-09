package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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
import com.bangninjia.app.model.LoginData;
import com.bangninjia.app.model.User;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Md5Util;
import com.bangninjia.app.utils.PreferencesUtils;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseNetActivity implements OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private EditText mUsername;
	private EditText mPassword;
	private String mComeFrom;

	private RadioGroup mRadioGroup;

	private TextView mForgetPwd;

	private Button mLoginBtn, mRegisterBtn;

	private MyProgressDialog mProgressDialog;

	private static final int LOGIN_CODE = 101;

	// private String mOption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// mOption = getIntent().getStringExtra("option");
		initView();
	}

	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.login);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mUsername = (EditText) this.findViewById(R.id.login_phone);
		mPassword = (EditText) this.findViewById(R.id.login_pwd);

		mForgetPwd = (TextView) this.findViewById(R.id.login_forget_pwd);

		mRadioGroup = (RadioGroup) this.findViewById(R.id.login_radioGroup);

		mLoginBtn = (Button) this.findViewById(R.id.login_btn);
		mRegisterBtn = (Button) this.findViewById(R.id.login_register_btn);

		mForgetPwd.setOnClickListener(this);

		mLoginBtn.setOnClickListener(this);
		mRegisterBtn.setOnClickListener(this);

	}

	private void login() {

		String username = mUsername.getText().toString();
		String password = mPassword.getText().toString();
		String md5Pwd = null;

		if (StringUtils.isNullOrEmpty(username)) {
			Toast.show(getApplicationContext(), "请输入手机号");
			return;
		}

		if (StringUtils.isNullOrEmpty(password)) {
			Toast.show(getApplicationContext(), "请输入密码");
			return;
		}

		switch (mRadioGroup.getCheckedRadioButtonId()) {
		case R.id.login_rb_user:
			mComeFrom = "1";
			break;
		case R.id.login_rb_supplier:
			mComeFrom = "2";
			break;
		case R.id.login_rb_workers:
			mComeFrom = "3";
			break;

		default:
			break;
		}

		md5Pwd = Md5Util.md5(password);
		Log.i("MainActivity", "ComeFrom==" + mComeFrom);

		LoginData loginData = new LoginData(username, md5Pwd, mComeFrom);

		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(loginData);
		params.put("data", dataString);
		Log.i("MainActivity", "loginData==" + dataString);
		post(LOGIN_CODE, Constants.LOGIN_SEND_INFO_URL, params);
		mProgressDialog = new MyProgressDialog(this, "正在登录..");
		mProgressDialog.show();
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		Log.i("MainActivity", "login--onNetworkFail--" + reason);
		mProgressDialog.dismiss();
		Toast.show(this, "登录失败，请检查您的网络");
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		mProgressDialog.dismiss();
		Log.i("MainActivity", "login--onNetworkSuccess" + jsonData);
		JSONObject jsonObject;
		int code = -1;
		String msg = null;
		try {
			jsonObject = new JSONObject(jsonData);
			code = jsonObject.getInt("code");
			msg = jsonObject.getString("msg");
			Log.i("MainActivity", "code=" + code);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Toast.show(this, msg);
		if (code == 1000) {
			TypeToken<User> typeToken = new TypeToken<User>() {
			};
			User user = MyApplication.getGson().fromJson(jsonData,
					typeToken.getType());
			// 放到全局变量中
			MyApplication.setUser(user);
			MyApplication.setComeFrom(mComeFrom);
			// 存到SharePreferences
			PreferencesUtils.putString(this, "userId", user.getUserId());
			PreferencesUtils.putString(this, "userName", user.getUserName());
			PreferencesUtils.putString(this, "comeFrom", mComeFrom);
			
			setResult(RESULT_OK);
			this.finish();
		}

	}

	@Override
	public void onNetworkFinish(int requestCode) {
		Log.i("MainActivity", "login--onNetworkFinish");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_forget_pwd:// 忘记密码
			startActivity(new Intent(LoginActivity.this,
					ForgetPwdActivity.class));
			break;
		case R.id.login_btn:// 登录
			login();
			break;
		case R.id.login_register_btn:// 注册
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LoginActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LoginActivity");
		MobclickAgent.onPause(this);
	}

}
