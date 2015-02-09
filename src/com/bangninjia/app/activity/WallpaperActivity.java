package com.bangninjia.app.activity;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.fragment.WallpaperHouseInfoFragment;
import com.bangninjia.app.fragment.WallpaperSelectWallpaperFragment;
import com.bangninjia.app.model.Brand;
import com.bangninjia.app.model.DemandItem;
import com.bangninjia.app.model.DemandProperties;
import com.bangninjia.app.model.OnSelectedListener;
import com.bangninjia.app.model.OrderJson;
import com.bangninjia.app.model.Product;
import com.bangninjia.app.model.User;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.umeng.analytics.MobclickAgent;

/**
 * 壁纸更新
 * 
 */
public class WallpaperActivity extends Activity implements OnClickListener,
		OnSelectedListener {

	private FragmentManager mFragmentManager;
	private FragmentTransaction mTransaction;

	private List<Fragment> mFragments;
	private WallpaperHouseInfoFragment mHouseInfoFragment;
	private WallpaperSelectWallpaperFragment mWallpaperFragment;

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	// tab
	private ImageView mWallpaperLeftImg, mWallpaperRightImg;
	private ImageView mDotImg1, mDotImg2;
	private TextView mTitle1, mTitle2;

	private int mCurrent = 1;

	// private Brand mBrand;
	private Product mProduct;
	private double mArea;// 服务面积
	private DemandProperties mProperties;// 属性
	
	private DemandProperties mUpdateDemandProperties;//修改订单传过来的属性
	private double mUpdateArea;

	private static final int LOGIN_REQUEST_CODE = 1001;
	
	private static Format mFormat = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper);
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

		mTitleText.setText(R.string.wallpaper_update);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		mWallpaperLeftImg = (ImageView) this
				.findViewById(R.id.wallpaper_left_img);
		mWallpaperRightImg = (ImageView) this
				.findViewById(R.id.wallpaper_right_img);

		mDotImg1 = (ImageView) this
				.findViewById(R.id.wallpaper_banner_nav_dot1);
		mDotImg2 = (ImageView) this
				.findViewById(R.id.wallpaper_banner_nav_dot2);

		mTitle1 = (TextView) this
				.findViewById(R.id.wallpaper_banner_nav_title1);
		mTitle2 = (TextView) this
				.findViewById(R.id.wallpaper_banner_nav_title2);

		mWallpaperLeftImg.setOnClickListener(this);
		mWallpaperRightImg.setOnClickListener(this);

		mFragmentManager = getFragmentManager();

		mFragments = new ArrayList<Fragment>();
		mHouseInfoFragment = new WallpaperHouseInfoFragment();
		mWallpaperFragment = new WallpaperSelectWallpaperFragment();
		mFragments.add(mHouseInfoFragment);
		mFragments.add(mWallpaperFragment);

		mUpdateArea = getIntent().getDoubleExtra("area", 0);
		mUpdateDemandProperties = (DemandProperties) getIntent().getSerializableExtra("demandProperties");
		
		step(1);

	}

	/**
	 * 步骤
	 */
	private void step(int index) {
		if (index != 3) {
			mTransaction = mFragmentManager.beginTransaction();
		}
		switch (index) {
		case 1:// 房屋信息
			mDotImg1.setImageResource(R.drawable.dot_selected);
			mDotImg2.setImageResource(R.drawable.dot_normal);

			mTitle1.setTextColor(getResources().getColor(R.color.title_color));
			mTitle2.setTextColor(getResources().getColor(R.color.color_666666));

			mWallpaperLeftImg.setImageResource(R.drawable.left_arrow_unable);
			
			if (mUpdateDemandProperties != null) {
				Bundle args = new Bundle();
				args.putDouble("area", mUpdateArea);
				args.putSerializable("demandProperties", mUpdateDemandProperties);
				mHouseInfoFragment.setArguments(args);
			}

			mTransaction.replace(R.id.wallpaper_content, mHouseInfoFragment);

			break;
		case 2:// 选择壁纸

			mArea = mHouseInfoFragment.getServiceArea();
			if (mArea == 0) {
				Toast.show(this, "请输入服务面积");
				mCurrent--;
				return;
			}
			
			if (mArea < 20) {
				Toast.show(this, "服务面积必须大于等于20");
				mCurrent--;
				return;
			}

			int posDot = String.valueOf(mArea).indexOf(".");
			if (String.valueOf(mArea).length() - posDot - 1 > 2) {
				Toast.show(this, "服务面积只能输入2位小数");
				mCurrent--;
				return;
			}
			
			Log.i("MainActivity", "服务面积：" + mArea);
			mProperties = mHouseInfoFragment.getDemandProperties();
			if (StringUtils.isNullOrEmpty(mProperties.get现状信息())) {
				Toast.show(this, "请选择现状信息");
				mCurrent--;
				return;
			}
			
			if (StringUtils.isNullOrEmpty(mProperties.get服务类型())) {
				Toast.show(this, "请选择服务类型");
				mCurrent--;
				return;
			}

			mDotImg1.setImageResource(R.drawable.dot_normal);
			mDotImg2.setImageResource(R.drawable.dot_selected);

			mTitle1.setTextColor(getResources().getColor(R.color.color_666666));
			mTitle2.setTextColor(getResources().getColor(R.color.title_color));

			mWallpaperLeftImg.setImageResource(R.drawable.left_arrow);
			
			mTransaction.replace(R.id.wallpaper_content, mWallpaperFragment);
			break;
		case 3:// 确认订单
			
			if (mProduct == null) {
				Toast.show(this, "请选择壁纸");
				mCurrent--;
				return;
			}
			
			if (mUpdateDemandProperties != null) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putDouble("area",mArea);//服务面积
				bundle.putSerializable("properties", mProperties);
				bundle.putSerializable("product", mProduct);
				bundle.putSerializable("imageProduct", mWallpaperFragment.getPreImg()+mProduct.getImageList().get(0));
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				this.finish();
				return;
			}
			
			User user = MyApplication.getUser();
			// 未登录跳转到登录界面
			if (user == null) {
				Intent intent = new Intent(WallpaperActivity.this,
						LoginActivity.class);
				intent.putExtra("option", "confirmOrder");
				startActivityForResult(intent, LOGIN_REQUEST_CODE);
				mCurrent--;
				return;
			}
			goConfirmOrder();
			mCurrent--;
			break;
		default:
			return;
		}
		Log.i("MainActivity", "当前步骤：" + mCurrent);
		if (index != 3) {
			mTransaction.commit();
		}
	}

	/**
	 * 跳转到确认订单界面
	 */
	private void goConfirmOrder() {
		Intent intent = new Intent(WallpaperActivity.this,
				OrderConfirmActivity.class);
		OrderJson orderJson = new OrderJson();
		orderJson.setBrandId(mProduct.getBrandId());// 品牌id
		orderJson.setDemandProperties(mProperties);// 属性
		orderJson.setDemandSpace(String.valueOf(mArea));// 作业面积

		orderJson.setDemandType(1);
		orderJson.setSourceId(1);// 来源 1.Android 2.IOS
		orderJson.setServiceName("壁纸更新");
		orderJson.setProductImages(mWallpaperFragment.getPreImg()
				+ mProduct.getImageList().get(0));

		// 主材费
		DemandItem demandItem = new DemandItem();
		demandItem.setBrandId(mProduct.getBrandId());
		demandItem.setMemo(mProduct.getModel());
		demandItem.setName(mProduct.getName());
		demandItem.setPrice(String.valueOf(mProduct.getBnjPrice()));
		demandItem.setProductCode(mProduct.getCode());
		demandItem.setProductId(mProduct.getId());
		double quantity = mArea / mProduct.getWallpaperContiare();
		demandItem.setSmallPic(mWallpaperFragment.getPreImg()+mProduct.getImageList().get(0));
		demandItem.setQuantity((int)Math.ceil(quantity));
		demandItem.setType(1);// 主材费

		// 人工安装费
		DemandItem demandItem2 = new DemandItem();
		demandItem2.setBrandId(mProduct.getBrandId());

		String serviceType = mProperties.get服务类型();
		double paintingPrice = 0;
		if (serviceType.equals("铺装壁纸")) {
			paintingPrice = 14.99;
		} else if (serviceType.equals("更新壁纸")) {
			paintingPrice = 29.99;
		} else {
			paintingPrice = 39.99;
		}
		demandItem2.setPrice(mFormat.format(paintingPrice));
		demandItem2.setProductCode(mProduct.getCode());
		demandItem2.setProductId(mProduct.getId());
		demandItem2.setQuantity((int)Math.ceil(mArea));
		demandItem2.setType(2);

		// 辅材费
		DemandItem demandItem3 = new DemandItem();
		demandItem3.setBrandId(mProduct.getBrandId());
		demandItem3.setProductId(mProduct.getId());
		demandItem3.setProductCode(mProduct.getCode());
		demandItem3.setQuantity(0);
		demandItem3.setPrice(String.valueOf(0));
		demandItem3.setType(3);

		// 搬运费
		DemandItem demandItem4 = new DemandItem();
		demandItem4.setBrandId(mProduct.getBrandId());
		demandItem4.setProductId(mProduct.getId());
		demandItem4.setProductCode(mProduct.getCode());
		demandItem4.setQuantity(1);
		demandItem4.setPrice(String.valueOf(0));
		demandItem4.setType(4);

		// 运输费
		DemandItem demandItem5 = new DemandItem();
		demandItem5.setBrandId(mProduct.getBrandId());
		demandItem5.setProductId(mProduct.getId());
		demandItem5.setProductCode(mProduct.getCode());
		demandItem5.setQuantity(1);
		demandItem5.setPrice(String.valueOf(0));
		demandItem5.setType(5);
		
		// 壁纸胶费
		DemandItem demandItem7 = new DemandItem();
		demandItem7.setBrandId(mProduct.getBrandId());
		demandItem7.setProductId(mProduct.getId());
		demandItem7.setProductCode(mProduct.getCode());
		demandItem7.setQuantity(1);
		demandItem7.setPrice(String.valueOf(0));
		demandItem7.setType(7);

		// 拆除费
		DemandItem demandItem8 = new DemandItem();
		demandItem8.setBrandId(mProduct.getBrandId());
		demandItem8.setProductId(mProduct.getId());
		demandItem8.setProductCode(mProduct.getCode());
		demandItem8.setQuantity((int)Math.ceil(mArea));
		double price8 = 0;
		if (serviceType.equals("更新壁纸")) {
			price8 = 10;
		}
		demandItem8.setPrice(mFormat.format(price8));
		demandItem8.setType(8);

		// 测量费
		DemandItem demandItem9 = new DemandItem();
		demandItem9.setBrandId(mProduct.getBrandId());
		demandItem9.setProductId(mProduct.getId());
		demandItem9.setProductCode(mProduct.getCode());
		demandItem9.setQuantity(1);
		demandItem9.setPrice(String.valueOf(0));
		demandItem9.setType(9);

		Bundle bundle = new Bundle();
		bundle.putSerializable("orderJson", orderJson);
		bundle.putSerializable("demandItem", demandItem);
		bundle.putSerializable("demandItem2", demandItem2);
		bundle.putSerializable("demandItem3", demandItem3);
		bundle.putSerializable("demandItem4", demandItem4);
		bundle.putSerializable("demandItem5", demandItem5);
		bundle.putSerializable("demandItem7", demandItem7);
		bundle.putSerializable("demandItem8", demandItem8);
		bundle.putSerializable("demandItem9", demandItem9);

		bundle.putString("type", "wallpaper");
		intent.putExtras(bundle);

		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.wallpaper_left_img:// 上一步
			if (mCurrent > 1) {
				mCurrent--;
				step(mCurrent);
			}

			break;
		case R.id.wallpaper_right_img:// 下一步
			if (mCurrent < 4) {
				mCurrent++;
				step(mCurrent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LOGIN_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.i("MainActivity", "回传");
				goConfirmOrder();// 登录成功跳转到确认订单那界面
			}
		}
	}

	@Override
	public void onUserSelected(Brand brandSelected) {
		// mBrand = brandSelected;
	}

	@Override
	public void onUserSelected(Product productSelected) {
		mProduct = productSelected;
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
