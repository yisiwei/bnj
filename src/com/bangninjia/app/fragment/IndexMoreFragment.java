package com.bangninjia.app.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.activity.AboutUsActivity;
import com.bangninjia.app.activity.FeedBackActivity;
import com.bangninjia.app.activity.UserAgreementActivity;
import com.bangninjia.app.service.DownLoadService;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.PackageUtil;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 更多
 * 
 */
public class IndexMoreFragment extends BaseNetFragment implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mContactUsView;
	private TextView mVersionUpgradeView;
	private TextView mUserAgreementView;
	private TextView mFeedbackView;
	private TextView mAboutUsView;

	// 联系我们Dialog
	private AlertDialog mDialog;
	private TextView mPhone;
	private TextView mCancel;
	private TextView mCall;

	private static final int VERSION_REQUEST_CODE = 2002;

	private String mDownLoadUrl;

	private MyProgressDialog myProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index_more, container,
				false);

		initView(view);

		return view;
	}

	/**
	 * 初始化
	 * 
	 * @param view
	 */
	private void initView(View view) {
		// title
		mTitleLeftBtn = (ImageButton) view.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) view.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) view.findViewById(R.id.title_right_btn);

		mTitleLeftBtn.setVisibility(View.GONE);
		mTitleText.setText(R.string.more);
		mTitleRightBtn.setVisibility(View.GONE);

		mContactUsView = (TextView) view.findViewById(R.id.more_contact_us);
		mVersionUpgradeView = (TextView) view
				.findViewById(R.id.more_version_upgrade);
		mUserAgreementView = (TextView) view
				.findViewById(R.id.more_user_agreement);

		mFeedbackView = (TextView) view.findViewById(R.id.more_feedback);
		mAboutUsView = (TextView) view.findViewById(R.id.more_about_us);

		mContactUsView.setOnClickListener(this);
		mVersionUpgradeView.setOnClickListener(this);
		mUserAgreementView.setOnClickListener(this);

		mFeedbackView.setOnClickListener(this);
		mAboutUsView.setOnClickListener(this);

		myProgressDialog = new MyProgressDialog(getActivity(), "正在获取最新版本");

		mVersionUpgradeView.setText(getResources().getString(
				R.string.version_upgrade)
				+ PackageUtil.getVersionName(getActivity()));// 显示当前版本号
	}

	/**
	 * 联系我们对话框
	 */
	private void showContactUsDialog() {
		View view = View.inflate(getActivity(), R.layout.dialog_contact_us,
				null);
		mPhone = (TextView) view.findViewById(R.id.contact_us_phone);
		mCancel = (TextView) view.findViewById(R.id.contact_us_cancel);
		mCall = (TextView) view.findViewById(R.id.contact_us_call);

		mDialog = new AlertDialog.Builder(getActivity()).create();
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setContentView(view);
		mDialog.getWindow().setLayout(600, LayoutParams.WRAP_CONTENT);

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
		View view = View.inflate(getActivity(), R.layout.dialog_old_version,
				null);
		TextView confirm = (TextView) view.findViewById(R.id.old_version_btn);

		final AlertDialog oldVersionDialog = new AlertDialog.Builder(
				getActivity()).create();
		oldVersionDialog.setCancelable(false);
		oldVersionDialog.show();
		oldVersionDialog.setContentView(view);
		oldVersionDialog.getWindow().setLayout(560, LayoutParams.WRAP_CONTENT);

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
		View view = View.inflate(getActivity(), R.layout.dialog_new_version,
				null);
		TextView updateNow = (TextView) view
				.findViewById(R.id.new_version_update_now);
		TextView cancel = (TextView) view.findViewById(R.id.new_version_cancel);

		final AlertDialog newVersionDialog = new AlertDialog.Builder(
				getActivity()).create();
		newVersionDialog.setCancelable(false);
		newVersionDialog.show();
		newVersionDialog.setContentView(view);
		newVersionDialog.getWindow().setLayout(560, LayoutParams.WRAP_CONTENT);

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
		Intent intent = new Intent(getActivity(), DownLoadService.class);
		intent.putExtra("downloadUrl", mDownLoadUrl);
		getActivity().startService(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_contact_us:// 联系我们
			showContactUsDialog();
			break;
		case R.id.more_version_upgrade:// 版本升级
			versionUpgrade();
			break;
		case R.id.more_user_agreement:// 用户协议
			startActivity(new Intent(getActivity(), UserAgreementActivity.class));
			break;
		case R.id.more_feedback:// 意见反馈
			startActivity(new Intent(getActivity(), FeedBackActivity.class));
			break;
		case R.id.more_about_us:// 关于我们
			startActivity(new Intent(getActivity(), AboutUsActivity.class));
			break;
		case R.id.contact_us_cancel:// 取消联系我们对话框
			mDialog.dismiss();
			break;
		case R.id.contact_us_call:// 呼叫
			call();
			mDialog.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		myProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		myProgressDialog.dismiss();
		if (requestCode == VERSION_REQUEST_CODE) {
			Log.i("MainActivity", jsonData);
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				mDownLoadUrl = jsonObject.optString("download");
				boolean forceUpdate = jsonObject.optBoolean("forceUpdate");
				int verCode = jsonObject.optInt("verCode");
				Log.i("MainActivity", mDownLoadUrl + "-" + forceUpdate + "-"
						+ verCode);
				int currentVerCode = PackageUtil.getVersionCode(getActivity());
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

	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		myProgressDialog.dismiss();
		Toast.show(getActivity(), "获取失败，请检查您的网络");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("IndexMoreFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("IndexMoreFragment");
	}
	
}
