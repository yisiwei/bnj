package com.bangninjia.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.bangninjia.R;
import com.bangninjia.app.utils.PackageUtil;
import com.bangninjia.app.utils.PreferencesUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 引导页
 * 
 */
public class GuideActivity extends Activity {

	private ViewPager mGuideView;
	private List<View> mViews;// 引导页界面

	private LinearLayout mLastGuideView;// 最后一页引导页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		mGuideView = (ViewPager) this.findViewById(R.id.guide_vp);

		mViews = new ArrayList<View>();
		mViews.add(View.inflate(this, R.layout.guide_one, null));
		mViews.add(View.inflate(this, R.layout.guide_two, null));
		mViews.add(View.inflate(this, R.layout.guide_three, null));
		mViews.add(View.inflate(this, R.layout.guide_four, null));

		mGuideView.setAdapter(new MyAdapter());

	}

	/**
	 * 自定义PagerAdapter
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mViews.get(position));

			if (position == mViews.size() - 1) { // 最后一页
				mLastGuideView = (LinearLayout) container
						.findViewById(R.id.guide_lastPage);
				mLastGuideView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//Toast.show(getApplicationContext(), "立即体验");
						goHome();
					}
				});
			}

			return mViews.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

	}

	/**
	 * 进入首页
	 */
	private void goHome() {
		startActivity(new Intent(GuideActivity.this, MainActivity.class));
		int versionCode = PackageUtil.getVersionCode(this);
		PreferencesUtils.putInt(this, "versionCode", versionCode);// 将当前版本号存入文件
		this.finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("GuideActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("GuideActivity");
		MobclickAgent.onPause(this);
	}
}
