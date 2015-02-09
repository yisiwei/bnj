package com.bangninjia.app.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.OrderCancelData;
import com.bangninjia.app.model.ResultData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.ResultDataUtil;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 取消订单
 *
 */
public class OrderCancelActivity extends BaseNetActivity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private RadioGroup mRadioGroup;
	private EditText mReasonText;
	private Button mSubmitBtn;

	private MyProgressDialog mProgressDialog;

	private String mReason;
	private int mOrderId;

	private boolean mIsSubmit = false;

	private static final int CANCEL_ORDER_REQUEST_CODE = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_cancel);

		initView();

		mProgressDialog = new MyProgressDialog(this, "正在提交");

		Intent intent = getIntent();
		mOrderId = intent.getIntExtra("orderId", 0);
	}

	/**
	 * 初始化
	 */
	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.order_cancel);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mRadioGroup = (RadioGroup) this
				.findViewById(R.id.order_cancel_reason_rg);
		mReasonText = (EditText) this.findViewById(R.id.reason_content);
		mSubmitBtn = (Button) this.findViewById(R.id.order_cancel_btn);

		mReasonText.setVisibility(View.GONE);

		mSubmitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelOrder();
			}
		});

		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton radioButton = (RadioButton) findViewById(group
						.getCheckedRadioButtonId());
				mReason = radioButton.getText().toString();
				if (group.getCheckedRadioButtonId() == R.id.order_cancel_reason5) {
					mReasonText.setVisibility(View.VISIBLE);
				} else {
					mReasonText.setVisibility(View.GONE);
				}
			}
		});

	}

	/**
	 * 取消订单
	 */
	private void cancelOrder() {
		
		if (StringUtils.isNullOrEmpty(mReason)) {
			Toast.show(this, "请选择一个理由");
			return;
		}

		String reason = null;
		if (mRadioGroup.getCheckedRadioButtonId() == R.id.order_cancel_reason5) {
			String str = mReasonText.getText().toString();
			if (StringUtils.isNullOrEmpty(str)) {
				reason = mReason;
			}else{
				reason = str;
			}
		}else{
			reason = mReason;
		}

		OrderCancelData data = new OrderCancelData();
		data.setUserId(MyApplication.getUser().getUserId());
		data.setOrderId(String.valueOf(mOrderId));
		data.setReason(reason);

		String dataString = MyApplication.getGson().toJson(data);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);
		Log.i(dataString);
		Log.i(Constants.CANCEL_ORDER_URL);
		if (!mIsSubmit) {
			post(CANCEL_ORDER_REQUEST_CODE, Constants.CANCEL_ORDER_URL, params);
		}else{
			Toast.show(this, "您的订单已申请取消，请勿重复提交");
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
		if (requestCode == CANCEL_ORDER_REQUEST_CODE) {
			ResultData result = ResultDataUtil.parse(jsonData);
			if ("2000".equals(result.getCode())) {
				mIsSubmit = true;
				Toast.show(this, "订单已经申请取消");
				setResult(RESULT_OK);
				this.finish();
			} else {
				Toast.show(this, result.getMsg());
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
		Toast.show(this, "取消失败，请检查您的网络");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderCancelActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderCancelActivity");
		MobclickAgent.onPause(this);
	}

}
