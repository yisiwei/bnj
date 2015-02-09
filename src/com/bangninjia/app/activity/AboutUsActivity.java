package com.bangninjia.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们
 * 
 */
public class AboutUsActivity extends Activity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.about_us);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AboutUsActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AboutUsActivity");
		MobclickAgent.onPause(this);
	}
}
