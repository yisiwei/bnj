package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Locale;
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
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.RegexUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 忘记密码
 * 
 */
public class ForgetPwdActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private EditText mPhoneEt;
	private EditText mCodeEt;
	private Button mGetCodeBtn;
	private Button mNextBtn;

	private MyProgressDialog mProgressDialog;

	private String mPhone;// 手机号
	private String mCode = "";

	private static final int EXIT_USER_REQUEST_CODE = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);

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

		mPhoneEt = (EditText) this.findViewById(R.id.forget_pwd_phone);
		mCodeEt = (EditText) this.findViewById(R.id.forget_pwd_code);

		mGetCodeBtn = (Button) this.findViewById(R.id.forget_pwd_getCode_btn);
		mNextBtn = (Button) this.findViewById(R.id.forget_pwd_next_btn);

		mGetCodeBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);

		getCode();
		mProgressDialog = new MyProgressDialog(this, "正在加载");
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		mCode = "";
		int codeLength = 4;
		String[] random = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
				"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };// 随机数
		for (int i = 0; i < codeLength; i++) {
			int index = (int) Math.floor(Math.random() * 36);
			mCode += random[index];
		}
		Log.i("验证码：" + mCode);
		mGetCodeBtn.setText(mCode);
	}

	/**
	 * 下一步
	 */
	private void next() {
		mPhone = mPhoneEt.getText().toString();
		String code = mCodeEt.getText().toString()
				.toUpperCase(Locale.getDefault());// 转化为大写
		if (StringUtils.isNullOrEmpty(mPhone)) {
			Toast.show(this, "请输入手机号");
			return;
		}

		if (StringUtils.isNullOrEmpty(code)) {
			Toast.show(this, "请输入验证码");
			return;
		}

		Log.i("输入code为：" + code);
		if (!code.equals(mCode)) {
			Toast.show(this, "验证码错误");
			return;
		}

		if (!RegexUtil.isMobile(mPhone)) {
			Toast.show(this, "请输入正确的手机号");
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		JSONObject obj = new JSONObject();
		try {
			obj.put("userName", mPhone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.put("data", obj.toString());
		Log.i("MainActivity", obj.toString());
		post(EXIT_USER_REQUEST_CODE, Constants.EXIT_USER_URL, params);

	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == EXIT_USER_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				boolean msg = object.optBoolean("msg");
				if (code == 1000) {
					if (msg) {
						Intent intent = new Intent(ForgetPwdActivity.this,
								ForgetPwdGetCodeActivity.class);
						intent.putExtra("phone", mPhone);
						startActivity(intent);
					} else {
						Toast.show(this, "该手机号未注册");
					}
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
		MobclickAgent.onPageStart("ForgetPwdActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ForgetPwdActivity");
		MobclickAgent.onPause(this);
	}

}
