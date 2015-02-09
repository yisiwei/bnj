package com.bangninjia.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.service.DownLoadService;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.PackageUtil;
import com.bangninjia.app.utils.PreferencesUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 更多设置
 * 
 */
public class MoreSettingActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mContactUsView;// 联系我们
	private TextView mVersionUpgradeView;// 版本升级
	private TextView mUserAgreementView;// 用户协议
	private TextView mFeedbackView;// 用户反馈
	private TextView mAboutUsView;// 关于我们

	private Button mExitBtn;// 退出

	// 联系我们Dialog
	private AlertDialog mDialog;
	private TextView mPhone;
	private TextView mCancel;
	private TextView mCall;

	private static final int VERSION_REQUEST_CODE = 2002;// 版本升级code

	private String mDownLoadUrl;// 下载地址

	private MyProgressDialog myProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_setting);

		initView();

		myProgressDialog = new MyProgressDialog(this, "正在获取最新版本");

		mVersionUpgradeView.setText(getResources().getString(
				R.string.version_upgrade)
				+ PackageUtil.getVersionName(this));// 显示当前版本号
	}

	/**
	 * 初始化
	 */
	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleLeftBtn.setOnClickListener(this);
		mTitleText.setText(R.string.more_setting);
		mTitleRightBtn.setVisibility(View.GONE);

		mContactUsView = (TextView) this.findViewById(R.id.more_contact_us);
		mVersionUpgradeView = (TextView) this
				.findViewById(R.id.more_version_upgrade);
		mUserAgreementView = (TextView) this
				.findViewById(R.id.more_user_agreement);

		mFeedbackView = (TextView) this.findViewById(R.id.more_feedback);
		mAboutUsView = (TextView) this.findViewById(R.id.more_about_us);

		mExitBtn = (Button) this.findViewById(R.id.more_exit_btn);
		mExitBtn.setOnClickListener(this);

		mContactUsView.setOnClickListener(this);
		mVersionUpgradeView.setOnClickListener(this);
		mUserAgreementView.setOnClickListener(this);

		mFeedbackView.setOnClickListener(this);
		mAboutUsView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.more_contact_us:// 联系我们
			showContactUsDialog();
			break;
		case R.id.more_version_upgrade:// 版本升级
			versionUpgrade();
			break;
		case R.id.more_user_agreement:// 用户协议
			startActivity(new Intent(this, UserAgreementActivity.class));
			break;
		case R.id.more_feedback:// 意见反馈
			startActivity(new Intent(this, FeedBackActivity.class));
			break;
		case R.id.more_about_us:// 关于我们
			startActivity(new Intent(this, AboutUsActivity.class));
			break;
		case R.id.contact_us_cancel:// 取消联系我们对话框
			mDialog.dismiss();
			break;
		case R.id.contact_us_call:// 呼叫
			call();
			mDialog.dismiss();
			break;
		case R.id.more_exit_btn:// 退出
			exit();
			break;
		default:
			break;
		}
	}

	/**
	 * 退出登录
	 */
	private void exit() {
		if (MyApplication.getUser() == null) {
			Toast.show(this, "您还未登录");
			return;
		}
		MyApplication.setUser(null);
		PreferencesUtils.putString(this, "userId", null);
		PreferencesUtils.putString(this, "userName", null);
		PreferencesUtils.putString(this, "comeFrom", null);
		this.finish();
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	private int getWidth() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		Log.i("屏幕宽度：" + width);
		return width;
	}

	/**
	 * 联系我们对话框
	 */
	private void showContactUsDialog() {

		View view = View.inflate(this, R.layout.dialog_contact_us, null);
		mPhone = (TextView) view.findViewById(R.id.contact_us_phone);
		mCancel = (TextView) view.findViewById(R.id.contact_us_cancel);
		mCall = (TextView) view.findViewById(R.id.contact_us_call);

		mDialog = new AlertDialog.Builder(this).create();
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setContentView(view);
		mDialog.getWindow().setLayout(getWidth() / 3 * 2,
				LayoutParams.WRAP_CONTENT);

		mCancel.setOnClickListener(this);
		mCall.setOnClickListener(this);
	}

	/**
	 * 呼叫
	 */
	private void call() {
		String phone = mPhone.getText().toString();
		Log.i("MainActivity", "电话：" + phone);
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:4006506129"));
		startActivity(intent);
	}

	/**
	 * 版本升级
	 */
	private void versionUpgrade() {
		post(VERSION_REQUEST_CODE, Constants.VERSION_EXAM_URL);
	}

	/**
	 * 已是最新版本dialog
	 */
	private void showOldVersionDialog() {
		View view = View.inflate(this, R.layout.dialog_old_version, null);
		TextView confirm = (TextView) view.findViewById(R.id.old_version_btn);

		final AlertDialog oldVersionDialog = new AlertDialog.Builder(this)
				.create();
		oldVersionDialog.setCancelable(false);
		oldVersionDialog.show();
		oldVersionDialog.setContentView(view);
		oldVersionDialog.getWindow().setLayout(getWidth() / 3 * 2,
				LayoutParams.WRAP_CONTENT);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				oldVersionDialog.dismiss();
			}
		});
	}

	/**
	 * 检测到新版本dialog
	 */
	private void showNewVersionDialog() {
		View view = View.inflate(this, R.layout.dialog_new_version, null);
		TextView updateNow = (TextView) view
				.findViewById(R.id.new_version_update_now);
		TextView cancel = (TextView) view.findViewById(R.id.new_version_cancel);

		final AlertDialog newVersionDialog = new AlertDialog.Builder(this)
				.create();
		newVersionDialog.setCancelable(false);
		newVersionDialog.show();
		newVersionDialog.setContentView(view);
		newVersionDialog.getWindow().setLayout(getWidth() / 3 * 2,
				LayoutParams.WRAP_CONTENT);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newVersionDialog.dismiss();
			}
		});

		updateNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downLoad();
				newVersionDialog.dismiss();
			}
		});
	}

	/**
	 * 下载最新版本
	 */
	private void downLoad() {
		Intent intent = new Intent(this, DownLoadService.class);
		intent.putExtra("downloadUrl", mDownLoadUrl);
		startService(intent);

	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		myProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == VERSION_REQUEST_CODE) {
			Log.i("MainActivity", jsonData);
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				mDownLoadUrl = jsonObject.optString("download");
				boolean forceUpdate = jsonObject.optBoolean("forceUpdate");
				int verCode = jsonObject.optInt("verCode");
				Log.i("MainActivity", mDownLoadUrl + "-" + forceUpdate + "-"
						+ verCode);
				int currentVerCode = PackageUtil.getVersionCode(this);
				Log.i("MainActivity", "currentVerCode==" + currentVerCode);
				if (verCode <= currentVerCode) {
					showOldVersionDialog();
				} else {
					showNewVersionDialog();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onNetworkFinish(int requestCode) {
		myProgressDialog.dismiss();
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		Toast.show(this, "请检查您的网络");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MoreSettingActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MoreSettingActivity");
		MobclickAgent.onPause(this);
	}
}
