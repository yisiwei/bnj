package com.bangninjia.app.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.fragment.IndexAccountFragment;
import com.bangninjia.app.fragment.IndexHomeFragment;
import com.bangninjia.app.fragment.IndexOrderFragment;
import com.bangninjia.app.fragment.IndexReservationFragment;
import com.bangninjia.app.model.User;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseNetActivity implements OnClickListener {

	private FragmentManager mFragmentManager;
	private FragmentTransaction mTransaction;

	// Tab选项
	private TextView mTabHome, mTabOrder, mTabReservation, mTabAccount;

	private long mTouchTime = 0;
	private long mWaitTime = 2000;//再按一次退出等待时间

	private static final int LOGIN_REQUEST_CODE = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		
		mFragmentManager = getFragmentManager();
		setTabSelection(1);
	}

	/**
	 * 初始化View
	 */
	private void initView() {

		// Tab
		mTabHome = (TextView) this.findViewById(R.id.index_home);
		mTabOrder = (TextView) this.findViewById(R.id.index_order);
		mTabReservation = (TextView) this.findViewById(R.id.index_reservation);
		mTabAccount = (TextView) this.findViewById(R.id.index_account);

		mTabHome.setOnClickListener(this);
		mTabOrder.setOnClickListener(this);
		mTabReservation.setOnClickListener(this);
		mTabAccount.setOnClickListener(this);
		
	}

	private Drawable getTopDrawable(int resId) {
		Drawable drawable = getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		return drawable;
	}

	private void setTabSelection(int index) {
		Fragment fragment = null;
		mTransaction = mFragmentManager.beginTransaction();
		Drawable homeDrawable = null;
		Drawable orderDrawable = null;
		Drawable reservationDrawable = null;
		Drawable accountDrawable = null;
		switch (index) {
		case 1://主页
			fragment = new IndexHomeFragment();

			homeDrawable = getTopDrawable(R.drawable.index_icon1_on);
			orderDrawable = getTopDrawable(R.drawable.index_icon2);
			reservationDrawable = getTopDrawable(R.drawable.index_icon4);
			accountDrawable = getTopDrawable(R.drawable.index_icon3);

			mTabHome.setTextColor(getResources().getColor(R.color.tab_color_on));
			mTabOrder.setTextColor(getResources().getColor(R.color.tab_color));
			mTabReservation.setTextColor(getResources().getColor(
					R.color.tab_color));
			mTabAccount
					.setTextColor(getResources().getColor(R.color.tab_color));

			break;
		case 2://订单

			User user = MyApplication.getUser();
			if (user == null) {
				Toast.show(this, "请先登录");
				startActivityForResult(new Intent(MainActivity.this,
						LoginActivity.class), LOGIN_REQUEST_CODE);
				return;
			}

			fragment = new IndexOrderFragment();

			homeDrawable = getTopDrawable(R.drawable.index_icon1);
			orderDrawable = getTopDrawable(R.drawable.index_icon2_on);
			reservationDrawable = getTopDrawable(R.drawable.index_icon4);
			accountDrawable = getTopDrawable(R.drawable.index_icon3);

			mTabHome.setTextColor(getResources().getColor(R.color.tab_color));
			mTabOrder.setTextColor(getResources()
					.getColor(R.color.tab_color_on));
			mTabReservation.setTextColor(getResources().getColor(
					R.color.tab_color));
			mTabAccount
					.setTextColor(getResources().getColor(R.color.tab_color));

			break;
		case 3://预约
			fragment = new IndexReservationFragment();

			homeDrawable = getTopDrawable(R.drawable.index_icon1);
			orderDrawable = getTopDrawable(R.drawable.index_icon2);
			reservationDrawable = getTopDrawable(R.drawable.index_icon4_on);
			accountDrawable = getTopDrawable(R.drawable.index_icon3);

			mTabHome.setTextColor(getResources().getColor(R.color.tab_color));
			mTabOrder.setTextColor(getResources().getColor(R.color.tab_color));
			mTabReservation.setTextColor(getResources().getColor(
					R.color.tab_color_on));
			mTabAccount.setTextColor(getResources().getColor(
					R.color.tab_color));

			break;
		case 4://我的
			fragment = new IndexAccountFragment();

			homeDrawable = getTopDrawable(R.drawable.index_icon1);
			orderDrawable = getTopDrawable(R.drawable.index_icon2);
			reservationDrawable = getTopDrawable(R.drawable.index_icon4);
			accountDrawable = getTopDrawable(R.drawable.index_icon3_on);

			mTabHome.setTextColor(getResources().getColor(R.color.tab_color));
			mTabOrder.setTextColor(getResources().getColor(R.color.tab_color));
			mTabReservation.setTextColor(getResources().getColor(
					R.color.tab_color));
			mTabAccount
			.setTextColor(getResources().getColor(R.color.tab_color_on));

			break;
		default:
			fragment = new IndexHomeFragment();
			break;
		}

		mTabHome.setCompoundDrawables(null, homeDrawable, null, null);
		mTabOrder.setCompoundDrawables(null, orderDrawable, null, null);
		mTabReservation.setCompoundDrawables(null, reservationDrawable, null, null);
		mTabAccount.setCompoundDrawables(null, accountDrawable, null, null);

		mTransaction.replace(R.id.index_content, fragment);
		mTransaction.commit();
	}

	/**
	 * 主动调用点击事件
	 * @param index
	 */
	public void setTab(int index) {
		switch (index) {
		case 1:
			mTabHome.performClick();
			break;
		case 2:
			mTabOrder.performClick();
			break;
		case 3:
			mTabReservation.performClick();
			break;
		case 4:
			mTabAccount.performClick();
			break;
		default:
			mTabHome.performClick();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_home:
			setTabSelection(1);
			break;
		case R.id.index_order:
			setTabSelection(2);
			break;
		case R.id.index_reservation:
			setTabSelection(3);
			break;
		case R.id.index_account:
			setTabSelection(4);
			break;
		default:
			setTabSelection(1);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("MainActivity", "回传");
		if (requestCode == LOGIN_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				setTabSelection(2);
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {//返回键监听
			long currentTime = System.currentTimeMillis();
			if ((currentTime - mTouchTime) > mWaitTime) {
				Toast.show(this, "再按一次退出程序");
				mTouchTime = currentTime;
			} else {
				this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onNetworkFinish(int requestCode) {

	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("退出");
		//清除缓存图片
		MyApplication.getImageLoader().clearDiskCache();
		MyApplication.getImageLoader().clearMemoryCache();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
