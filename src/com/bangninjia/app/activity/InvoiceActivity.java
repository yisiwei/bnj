package com.bangninjia.app.activity;

import android.app.Activity;
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
import com.bangninjia.app.model.Invoice;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.umeng.analytics.MobclickAgent;

public class InvoiceActivity extends Activity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private RadioButton mInvoiceTypePlain;
	private RadioGroup mInvoiceTitle;
	private EditText mUnitName;
	private RadioGroup mInvoiceContent;

	private String mTitle = "个人";
	private String mContent = "明细";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice);
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

		mTitleText.setText(R.string.invoice_info);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setText(R.string.save);
		mTitleRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				save();
			}
		});

		mInvoiceTypePlain = (RadioButton) this
				.findViewById(R.id.invoice_type_plain_rb);

		mInvoiceTitle = (RadioGroup) this.findViewById(R.id.invoice_title_rg);
		mUnitName = (EditText) this.findViewById(R.id.invoice_title_unit_name);

		mInvoiceContent = (RadioGroup) this
				.findViewById(R.id.invoice_content_rg);

		mInvoiceTitle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioButtonId = group.getCheckedRadioButtonId();
				if (radioButtonId == R.id.invoice_title_person_rb) {
					mUnitName.setVisibility(View.GONE);
				} else {
					mUnitName.setVisibility(View.VISIBLE);
				}
				RadioButton rb = (RadioButton) InvoiceActivity.this
						.findViewById(radioButtonId);
				mTitle = rb.getText().toString();
				Log.i("MainActivity", "title=" + mTitle);
			}
		});

		mInvoiceContent
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						int radioButtonId = group.getCheckedRadioButtonId();
						RadioButton rb = (RadioButton) InvoiceActivity.this
								.findViewById(radioButtonId);
						mContent = rb.getText().toString();
						Log.i("MainActivity", "content=" + mContent);
					}
				});

	}

	/**
	 * 保存
	 */
	private void save() {
		String type = mInvoiceTypePlain.getText().toString();
		String unitName = mUnitName.getText().toString();
		if (mTitle.equals("单位")) {
			if (StringUtils.isNullOrEmpty(unitName)) {
				Toast.show(this, "请填写单位名称");
				return;
			}
		}
		Invoice invoice = new Invoice(type, mTitle, unitName, mContent);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("invoice", invoice);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		this.finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("InvoiceActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("InvoiceActivity");
		MobclickAgent.onPause(this);
	}
}
