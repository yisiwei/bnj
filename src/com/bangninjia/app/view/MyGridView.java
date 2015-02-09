package com.bangninjia.app.view;

import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView {

	// public MyGridView(Context context) {
	// super(context);
	// this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// this.setNumColumns(3);
	// this.setGravity(Gravity.CENTER);
	// this.setBackgroundColor(0xcccccc);
	// }

	public MyGridView(android.content.Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
