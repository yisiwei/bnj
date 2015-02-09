package com.bangninjia.app.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangninjia.R;

/**
 * 自定义TitleBar
 * 
 */
public class TitleBar extends RelativeLayout {

	private ImageButton mLeftButton;
	private Button mRightButton;

	private TextView mTitleText;

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.title_bar, this);

		mTitleText = (TextView) findViewById(R.id.title_text);
		mLeftButton = (ImageButton) findViewById(R.id.title_left_btn);
		mRightButton = (Button) findViewById(R.id.title_right_btn);

		mLeftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((Activity) getContext()).finish();
			}
		});
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	public void setTitleText(String text) {
		mTitleText.setText(text);
	}

	/**
	 * 设置右边按钮文章
	 * 
	 * @param text
	 */
	public void setRightButtonText(String text) {
		mRightButton.setText(text);
	}

	/**
	 * 设置右边按钮点击事件
	 * 
	 * @param clickListener
	 */
	public void setRightButtonListener(OnClickListener clickListener) {
		mRightButton.setOnClickListener(clickListener);
	}

}
