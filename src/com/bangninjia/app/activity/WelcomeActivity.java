package com.bangninjia.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.bangninjia.R;
import com.bangninjia.app.utils.PreferencesUtils;
import com.umeng.analytics.MobclickAgent;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);
		
		MobclickAgent.openActivityDurationTrack(false);

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
		alphaAnimation.setDuration(2400);
		view.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				startApp();
			}
		});
	}

	private void startApp() {
		// 获取存储的版本号
		int versionCode = PreferencesUtils.getInt(this, "versionCode", 0);
		// 获取当前版本号
		//int currentVersionCode = PackageUtil.getVersionCode(this);

		Intent intent = null;
//		if (currentVersionCode > versionCode) {// 有新版本跳转到引导页
//			intent = new Intent(WelcomeActivity.this, GuideActivity.class);
//		} else {// 无新版本跳转到首页
//			intent = new Intent(WelcomeActivity.this, MainActivity.class);
//		}
		if (versionCode == 0) {// 首次安装
			intent = new Intent(WelcomeActivity.this, GuideActivity.class);
		} else {// 非首次安装
			intent = new Intent(WelcomeActivity.this, MainActivity.class);
		}
		startActivity(intent);
		this.finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WelcomeActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WelcomeActivity");
		MobclickAgent.onPause(this);
	}
}
