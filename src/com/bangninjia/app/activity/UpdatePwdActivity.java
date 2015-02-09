package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Map;

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
import com.bangninjia.app.model.ModifyPwdData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Md5Util;
import com.bangninjia.app.utils.PreferencesUtils;
import com.bangninjia.app.utils.RegexUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改密码
 * 
 */
public class UpdatePwdActivity extends BaseNetActivity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private EditText mOldPwdView;
	private EditText mNewPwdView;
	private EditText mConfirmNewPwdView;

	private Button mSubmitBtn;

	private static final int UPDATE_PWD_REQUEST_CODE = 2002;
	private MyProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_pwd);

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.update_password);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mOldPwdView = (EditText) this.findViewById(R.id.update_pwd_old_pwd);
		mNewPwdView = (EditText) this.findViewById(R.id.update_pwd_new_pwd);
		mConfirmNewPwdView = (EditText) this
				.findViewById(R.id.update_pwd_confirm_new_pwd);

		mSubmitBtn = (Button) this.findViewById(R.id.update_pwd_btn);

		mSubmitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updatePwd();
			}
		});

		mProgressDialog = new MyProgressDialog(this, "正在提交");
	}

	private void updatePwd() {
		String oldPwd = mOldPwdView.getText().toString();
		String newPwd = mNewPwdView.getText().toString();
		String confirmNewPwd = mConfirmNewPwdView.getText().toString();
		if (StringUtils.isNullOrEmpty(oldPwd)) {
			Toast.show(this, "请输入旧密码");
			return;
		}
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
		if (!confirmNewPwd.equals(newPwd)) {
			Toast.show(this, "两次密码输入不一致");
			return;
		}

		Map<String, String> params = new HashMap<String, String>();

		ModifyPwdData data = new ModifyPwdData();

		data.setUserId(MyApplication.getUser().getUserId());
		data.setUserName(MyApplication.getUser().getUserName());
		data.setPassword(Md5Util.md5(oldPwd));
		data.setNewPassword(Md5Util.md5(newPwd));
		data.setComefrom(MyApplication.getComeFrom());

		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i(dataString);

		post(UPDATE_PWD_REQUEST_CODE, Constants.MODIFY_PASS_URL, params);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == UPDATE_PWD_REQUEST_CODE) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				int code = jsonObject.getInt("code");
				String msg = jsonObject.getString("msg");
				if (code == 1000) {
					Toast.show(this, "修改成功");
					MyApplication.setUser(null);
					PreferencesUtils.putString(this, "userId", null);
					PreferencesUtils.putString(this, "userName", null);
					PreferencesUtils.putString(this, "comeFrom", null);
					setResult(RESULT_OK);
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
		Toast.show(this, "修改失败，请检查您的网络");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("UpdatePwdActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("UpdatePwdActivity");
		MobclickAgent.onPause(this);
	}

}
