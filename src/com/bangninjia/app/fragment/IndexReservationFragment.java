package com.bangninjia.app.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.ReservationData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.RegexUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;

/**
 * 快速预约
 * 
 */
public class IndexReservationFragment extends BaseNetFragment implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private EditText mNameEt;
	private EditText mPhoneEt;
	private EditText mCodeEt;
	private Button mGetCodeBtn;
	private Button mReservationBtn;
	private RadioGroup mServiceTypeRg;

	private String mServiceType;

	private MyProgressDialog mProgressDialog;

	private static final int GETCODE_REQUEST_CODE = 1000;
	private static final int RESERVATION_REQUEST_CODE = 1001;

	private boolean mFlag = false;
	private int mTime = 60; // 重新获取短信验证码的时限

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_index_reservation,
				container, false);

		initView(view);

		mProgressDialog = new MyProgressDialog(getActivity(), "正在加载");

		return view;
	}

	/**
	 * 初始化View
	 * 
	 * @param view
	 */
	private void initView(final View view) {
		// title
		mTitleLeftBtn = (ImageButton) view.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) view.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) view.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.now_reservation);
		mTitleLeftBtn.setVisibility(View.GONE);
		mTitleRightBtn.setVisibility(View.GONE);

		mNameEt = (EditText) view.findViewById(R.id.name);
		mPhoneEt = (EditText) view.findViewById(R.id.phone);
		mCodeEt = (EditText) view.findViewById(R.id.code);
		mGetCodeBtn = (Button) view.findViewById(R.id.get_code_btn);
		mReservationBtn = (Button) view.findViewById(R.id.reservation_btn);
		mServiceTypeRg = (RadioGroup) view.findViewById(R.id.reservation_rg);

		mGetCodeBtn.setOnClickListener(this);
		mReservationBtn.setOnClickListener(this);

		mServiceTypeRg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton radioButton = (RadioButton) view
								.findViewById(group.getCheckedRadioButtonId());
						mServiceType = radioButton.getText().toString();
						Log.i(mServiceType);
					}
				});

	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		mProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == GETCODE_REQUEST_CODE) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				String msg = jsonObject.getString("msg");
				Toast.show(getActivity(), msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if(requestCode == RESERVATION_REQUEST_CODE){
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				String msg = jsonObject.getString("msg");
				int code = jsonObject.optInt("code");
				if (code == 2002) {
					showSuccessDialog();
				}else{
					Toast.show(getActivity(), msg);
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
		Toast.show(getActivity(), "请检查您的网络");
	}
	
	/**
	 * 预约成功Dialog
	 */
	private void showSuccessDialog(){
		View view = View.inflate(getActivity(), R.layout.dialog_old_version, null);
		TextView text = (TextView) view.findViewById(R.id.text);
		TextView confirm = (TextView) view.findViewById(R.id.old_version_btn);
		
		text.setText("恭喜您提交成功！\n邦您家客服会在12小时内联系您。");

		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.setContentView(view);
		dialog.getWindow().setLayout(560, LayoutParams.WRAP_CONTENT);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				//TODO  清空输入框
				mNameEt.setText("");
				mPhoneEt.setText("");
				mCodeEt.setText("");
			}
		});
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		String phone = mPhoneEt.getText().toString();
		if (StringUtils.isNullOrEmpty(phone)) {
			Toast.show(getActivity(), "请输入手机号码");
			return;
		}

		if (!RegexUtil.isMobile(phone)) {
			Toast.show(getActivity(), "请输入正确的手机号");
			return;
		}

		Map<String, String> params = new HashMap<String, String>();

		JSONObject data = new JSONObject();
		try {
			data.put("mobile", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.put("data", data.toString());
		Log.i("MainActivity", "SmsData==" + data.toString());
		post(GETCODE_REQUEST_CODE, Constants.SMS_URL, params);
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
					mGetCodeBtn.setText("重新获取");
					mGetCodeBtn.setEnabled(true);
				} else {
					mGetCodeBtn.setText(msg.arg1 + "秒后重新获取");
				}

			}
		};
	};

	/**
	 * 立即预约
	 */
	private void reservation() {
		String name = mNameEt.getText().toString();
		String phone = mPhoneEt.getText().toString();
		String code = mCodeEt.getText().toString();

		if (StringUtils.isNullOrEmpty(name)) {
			Toast.show(getActivity(), "请输入姓名");
			return;
		}

		if (StringUtils.isNullOrEmpty(phone)) {
			Toast.show(getActivity(), "请输入手机号码");
			return;
		}

		if (!RegexUtil.isMobile(phone)) {
			Toast.show(getActivity(), "请输入正确的手机号");
			return;
		}

		if (StringUtils.isNullOrEmpty(code)) {
			Toast.show(getActivity(), "请输入验证码");
			return;
		}

		if (StringUtils.isNullOrEmpty(mServiceType)) {
			Toast.show(getActivity(), "请选择服务项目");
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		ReservationData data = new ReservationData();
		data.setName(mServiceType);
		data.setReceiverName(name);
		data.setReceiverMobile(phone);
		data.setCode(code);
		String dataString = MyApplication.getGson().toJson(data);
		params.put("data", dataString);
		Log.i("MainActivity", "ReservationData==" + dataString);
		post(RESERVATION_REQUEST_CODE, Constants.RESERVATION_URL, params);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_code_btn:// 获取验证码
			getCode();
			break;
		case R.id.reservation_btn:// 立即预约
			reservation();
			break;
		default:
			break;
		}
	}

}
