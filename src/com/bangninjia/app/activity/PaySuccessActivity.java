package com.bangninjia.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.umeng.analytics.MobclickAgent;

public class PaySuccessActivity extends BaseNetActivity implements
		OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private static Button paySuccessSer;
	private static Button paySuccessLook;
	private String orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paysuccess);
		orderId = getIntent().getStringExtra("orderId");
		initView();

	}

	private void initView() {

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.pay_result);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		paySuccessSer = (Button) findViewById(R.id.pay_successSer_btn);
		paySuccessLook = (Button) findViewById(R.id.pay_successLook_btn);
		paySuccessSer.setOnClickListener(this);
		paySuccessLook.setOnClickListener(this);

	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {

	}

	@Override
	public void onNetworkFinish(int requestCode) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_successSer_btn:
			startActivity(new Intent(PaySuccessActivity.this,
					MainActivity.class));
			break;

		case R.id.pay_successLook_btn:
			Intent intent = new Intent(PaySuccessActivity.this,
					OrderDetailActivity.class);
			intent.putExtra("orderId", Integer.valueOf(orderId));
			startActivity(intent);
			break;
		default:
			break;
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PaySuccessActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PaySuccessActivity");
		MobclickAgent.onPause(this);
	}

}
