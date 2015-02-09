package com.bangninjia.app.fragment;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.activity.CaseShareActivity;
import com.bangninjia.app.activity.FloorActivity;
import com.bangninjia.app.activity.SchoolActivity;
import com.bangninjia.app.activity.WallActivity;
import com.bangninjia.app.activity.WallpaperActivity;
import com.bangninjia.app.adapter.ImagePagerAdapter;
import com.bangninjia.app.utils.ListUtils;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.view.AutoScrollViewPager;
import com.umeng.analytics.MobclickAgent;

/**
 * 主页
 * 
 */
public class IndexHomeFragment extends Fragment implements OnClickListener {

	// private MyGallery gallery = null;
	// private ArrayList<Integer> imgList;
	private ArrayList<String> imgList; // 轮播图片List
	private ArrayList<ImageView> portImg;// 轮播圆点List
	private LinearLayout dotsLayout = null;// 圆点布局
	private int preSelImgIndex = 0;

	private AutoScrollViewPager viewPager;// 轮播ViewPager

	private ImageView mCallBtn;// 拨打电话按钮
	private TextView mCaseShare;// 案例分享
	private TextView mSchool;// 知识学堂

	private TextView mWallImg, mFloorImg, mWallPaperImg;// 墙面/地板/壁纸

	// private ImageView mExpectImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index_home, container,
				false);

		mCallBtn = (ImageView) view.findViewById(R.id.home_call_btn);
		mCallBtn.setOnClickListener(this);
		mCaseShare = (TextView) view.findViewById(R.id.home_case_share);
		mCaseShare.setOnClickListener(this);
		mSchool = (TextView) view.findViewById(R.id.home_school);
		mSchool.setOnClickListener(this);

		mWallImg = (TextView) view.findViewById(R.id.index_home_wall);
		mFloorImg = (TextView) view.findViewById(R.id.index_home_floor);
		mWallPaperImg = (TextView) view.findViewById(R.id.index_home_wallpaper);
		// mExpectImg = (ImageView) view.findViewById(R.id.index_home_expect);

		dotsLayout = (LinearLayout) view.findViewById(R.id.home_navig_dots);
		// imgList = new ArrayList<Integer>();
		imgList = new ArrayList<String>();
		imgList.add("http://styles.bangninjia.com/app/images/img_main_android_1.jpg");
		imgList.add("http://styles.bangninjia.com/app/images/img_main_android_2.jpg");
		imgList.add("http://styles.bangninjia.com/app/images/img_main_android_3.jpg");
		imgList.add("http://styles.bangninjia.com/app/images/img_main_android_4.jpg");
		// imgList.add(R.drawable.banner1);
		// imgList.add(R.drawable.banner2);
		// imgList.add(R.drawable.banner3);
		// imgList.add(R.drawable.banner4);

		viewPager = (AutoScrollViewPager) view.findViewById(R.id.view_pager);

		InitFocusIndicatorContainer();

		viewPager.setAdapter(new ImagePagerAdapter(getActivity(), imgList)
				.setInfiniteLoop(true));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setInterval(2400);
		// viewPager.startAutoScroll();
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% ListUtils.getSize(imgList));

		// gallery = (MyGallery) view.findViewById(R.id.myGallery);
		// gallery.setAdapter(new ImgAdapter(getActivity(), imgList));
		// gallery.setFocusable(true);
		// gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int selIndex, long arg3) {
		// selIndex = selIndex % imgList.size();
		// // 修改上一次选中项的背景
		// portImg.get(preSelImgIndex).setImageResource(
		// R.drawable.unselect_dot);
		// // 修改当前选中项的背景
		// portImg.get(selIndex).setImageResource(R.drawable.select_dot);
		// preSelImgIndex = selIndex;
		// }
		//
		// public void onNothingSelected(AdapterView<?> arg0) {
		// }
		// });

		mWallImg.setOnClickListener(this);
		mFloorImg.setOnClickListener(this);
		mWallPaperImg.setOnClickListener(this);
		// mExpectImg.setOnClickListener(this);

		return view;
	}

	/**
	 * ViewPager监听
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {

			position = position % imgList.size();
			// 修改上一次选中项的背景
			portImg.get(preSelImgIndex).setImageResource(
					R.drawable.unselect_dot);
			// 修改当前选中项的背景
			portImg.get(position).setImageResource(R.drawable.select_dot);
			preSelImgIndex = position;

		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 轮播圆点
	 */
	private void InitFocusIndicatorContainer() {
		portImg = new ArrayList<ImageView>();
		for (int i = 0; i < imgList.size(); i++) {
			ImageView localImageView = new ImageView(getActivity());
			localImageView.setId(i);
			ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
			localImageView.setScaleType(localScaleType);
			LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
					48, 48);
			localImageView.setLayoutParams(localLayoutParams);
			localImageView.setPadding(10, 10, 10, 10);
			localImageView.setImageResource(R.drawable.unselect_dot);
			portImg.add(localImageView);
			this.dotsLayout.addView(localImageView);
		}
	}

	/**
	 * 点击监听事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_home_wall:// 墙面更新
			startActivity(new Intent(getActivity(), WallActivity.class));
			break;
		case R.id.index_home_floor:// 地板更新
			startActivity(new Intent(getActivity(), FloorActivity.class));
			break;
		case R.id.index_home_wallpaper:// 壁纸更新
			startActivity(new Intent(getActivity(), WallpaperActivity.class));
			break;
		// case R.id.index_home_expect:// 敬请期待
		// startActivity(new Intent(getActivity(), ExpectActivity.class));
		// break;
		case R.id.home_call_btn:// 拨打电话
			showCallDialog();
			break;
		case R.id.home_case_share:// 案例分享
			startActivity(new Intent(getActivity(), CaseShareActivity.class));
			break;
		case R.id.home_school:// 知识学堂
			startActivity(new Intent(getActivity(), SchoolActivity.class));
			break;
		default:
			break;
		}

	}
	
	private int getWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		Log.i("屏幕宽度："+width);
		return width;
	}

	/**
	 * 显示呼叫对话框
	 */
	private void showCallDialog() {
		View view = View.inflate(getActivity(), R.layout.dialog_contact_us,
				null);
		TextView mPhone = (TextView) view.findViewById(R.id.contact_us_phone);
		TextView mCallText = (TextView) view.findViewById(R.id.call_text);
		mCallText.setVisibility(View.GONE);
		TextView mCancel = (TextView) view.findViewById(R.id.contact_us_cancel);
		TextView mCall = (TextView) view.findViewById(R.id.contact_us_call);
		mPhone.setText("400-650-6129");

		final AlertDialog mDialog = new AlertDialog.Builder(getActivity())
				.create();
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setContentView(view);
		mDialog.getWindow().setLayout(getWidth()/3*2, LayoutParams.WRAP_CONTENT);

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:4006506129"));
				startActivity(intent);
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		viewPager.stopAutoScroll();
		MobclickAgent.onPageEnd("IndexHomeFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		viewPager.startAutoScroll();
		MobclickAgent.onPageStart("IndexHomeFragment");
	}

}
