package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.ResetPwdData;
import com.bangninjia.app.model.ResultData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Md5Util;
import com.bangninjia.app.utils.RegexUtil;
import com.bangninjia.app.utils.ResultDataUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 忘记密码-重置密码
 * 
 */
public class ResetPwdActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private EditText mNewPwdEt;
	private Button mResetBtn;

	private MyProgressDialog mProgressDialog;

	private AlertDialog mDialog;
	private TextView mBacnLoginBtn;
	
	private String mPhone;
	private String mCode;
	
	private static final int RESET_PWD_REQUEST_CODE = 1100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);

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

		mTitleText.setText(R.string.reset_pwd);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		mNewPwdEt = (EditText) this.findViewById(R.id.forget_pwd_new_password);
		mResetBtn = (Button) this.findViewById(R.id.forget_pwd_reset_btn);

		mResetBtn.setOnClickListener(this);

		mProgressDialog = new MyProgressDialog(this, "正在加载");
		
		mPhone = getIntent().getStringExtra("phone");
		mCode = getIntent().getStringExtra("code");
	}

	/**
	 * 重置密码
	 */
	private void reset() {
		String newPwd = mNewPwdEt.getText().toString();

		if (StringUtils.isNullOrEmpty(newPwd)) {
			Toast.show(this, "请输入新密码");
			return;
		}

		if (RegexUtil.checkPassword(newPwd)) {
			Toast.show(this, "密码不能为纯数字或纯字母");
			return;
		}
		if (newPwd.trim().length() < 8 || newPwd.trim().length() > 16) {
			Toast.show(this, "密码长度为8-16位");
			return;
		}
		
		ResetPwdData data = new ResetPwdData();
		data.setKpac(mCode);
		data.setUserName(mPhone);
		data.setNewPassword(Md5Util.md5(newPwd));
		
		Map<String, String> params = new HashMap<String, String>();
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i(dataString);
		post(RESET_PWD_REQUEST_CODE, Constants.RESET_PASS_URL, params);
		
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}
	
	private int getWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		Log.i("屏幕宽度："+width);
		return width;
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == RESET_PWD_REQUEST_CODE) {
			ResultData data = ResultDataUtil.parse(jsonData);
			if (data.getCode().equals("1000")) {
				View view = View.inflate(this, R.layout.dialog_reset_pwd, null);
				mBacnLoginBtn = (TextView) view.findViewById(R.id.back_login_btn);
				mDialog = new AlertDialog.Builder(this).create();
				mDialog.setCancelable(false);
				mDialog.show();
				mDialog.setContentView(view);
				mDialog.getWindow().setLayout(getWidth()/3*2, LayoutParams.WRAP_CONTENT);
				mBacnLoginBtn.setOnClickListener(this);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.forget_pwd_reset_btn:// 重置密码
			reset();
			break;
		case R.id.back_login_btn:// 返回登录
			mDialog.dismiss();
			MyApplication.getInstance().removeActivities();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ResetPwdActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ResetPwdActivity");
		MobclickAgent.onPause(this);
	}

}
