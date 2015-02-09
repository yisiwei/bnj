package com.bangninjia.app.activity;

import java.text.DecimalFormat;
import java.text.Format;

import android.app.Activity;
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
import com.bangninjia.app.fragment.FloorHouseInfoFragment;
import com.bangninjia.app.fragment.FloorSelectFloorFragment;
import com.bangninjia.app.fragment.FloorSelectSkirtingBoardFragment;
import com.bangninjia.app.model.Brand;
import com.bangninjia.app.model.DemandItem;
import com.bangninjia.app.model.DemandProperties;
import com.bangninjia.app.model.OnSelectedListener;
import com.bangninjia.app.model.OnSkirtingBoardListener;
import com.bangninjia.app.model.OrderJson;
import com.bangninjia.app.model.Product;
import com.bangninjia.app.model.SeriesProperty;
import com.bangninjia.app.model.SkirtingBoard;
import com.bangninjia.app.model.User;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.umeng.analytics.MobclickAgent;

/**
 * 地板更新
 * 
 */
public class FloorActivity extends Activity implements OnClickListener,
		OnSelectedListener, OnSkirtingBoardListener {

	private FragmentManager mFragmentManager;
	private FragmentTransaction mTransaction;

	private FloorHouseInfoFragment mHouseInfoFragment;
	private FloorSelectFloorFragment mFloorFragment;
	private FloorSelectSkirtingBoardFragment mSkirtingBoardFragment;

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	// tab
	private ImageView mFloorLeftImg, mFloorRightImg;
	private ImageView mDotImg1, mDotImg2, mDotImg3;
	private TextView mTitle1, mTitle2, mTitle3;

	private double mArea;// 服务面积
	private double mSkirtingBoardLength;// 踢脚线长度
	private DemandProperties mProperties;// 属性

	private Brand mBrand;
	private Product mProduct;

	private SkirtingBoard mSkirtingBoard;
	private SeriesProperty mSeriesProperty;

	private int mCurrent = 1;
	private static final int LOGIN_REQUEST_CODE = 1001;
	
	private DemandProperties mUpdateDemandProperties;//修改订单传过来的属性
	private double mUpdateArea;

	
	private static Format mFormat = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_floor);

		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.floor_update);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		mFloorLeftImg = (ImageView) this.findViewById(R.id.floor_left_img);
		mFloorRightImg = (ImageView) this.findViewById(R.id.floor_right_img);

		mDotImg1 = (ImageView) this.findViewById(R.id.floor_banner_nav_dot1);
		mDotImg2 = (ImageView) this.findViewById(R.id.floor_banner_nav_dot2);
		mDotImg3 = (ImageView) this.findViewById(R.id.floor_banner_nav_dot3);

		mTitle1 = (TextView) this.findViewById(R.id.floor_banner_nav_title1);
		mTitle2 = (TextView) this.findViewById(R.id.floor_banner_nav_title2);
		mTitle3 = (TextView) this.findViewById(R.id.floor_banner_nav_title3);

		mFloorLeftImg.setOnClickListener(this);
		mFloorRightImg.setOnClickListener(this);

		mFragmentManager = getFragmentManager();

		mHouseInfoFragment = new FloorHouseInfoFragment();
		mFloorFragment = new FloorSelectFloorFragment();
		mSkirtingBoardFragment = new FloorSelectSkirtingBoardFragment();
		
		mUpdateArea = getIntent().getDoubleExtra("area", 0);
		mUpdateDemandProperties = (DemandProperties) getIntent().getSerializableExtra("demandProperties");
		
		step(1);

	}

	/**
	 * 步骤
	 */
	private void step(int index) {
		if (index != 4) {
			mTransaction = mFragmentManager.beginTransaction();
		}
		switch (index) {
		case 1:// 房屋信息
			mDotImg1.setImageResource(R.drawable.dot_selected);
			mDotImg2.setImageResource(R.drawable.dot_normal);
			mDotImg3.setImageResource(R.drawable.dot_normal);

			mTitle1.setTextColor(getResources().getColor(R.color.title_color));
			mTitle2.setTextColor(getResources().getColor(R.color.color_666666));
			mTitle3.setTextColor(getResources().getColor(R.color.color_666666));

			mFloorLeftImg.setImageResource(R.drawable.left_arrow_unable);

			if (mUpdateDemandProperties != null) {
				Bundle args = new Bundle();
				args.putDouble("area", mUpdateArea);
				args.putSerializable("demandProperties", mUpdateDemandProperties);
				mHouseInfoFragment.setArguments(args);
			}
			
			mTransaction.replace(R.id.floor_content, mHouseInfoFragment);

			break;
		case 2:// 选择地板

			mArea = mHouseInfoFragment.getServiceArea();
			if (mArea == 0) {
				Toast.show(this, "请输入服务面积");
				mCurrent--;
				return;
			}
			
			if (mArea < 10) {
				Toast.show(this, "服务面积必须大于等于10");
				mCurrent--;
				return;
			}

			int posDot = String.valueOf(mArea).indexOf(".");
			if (String.valueOf(mArea).length() - posDot - 1 > 2) {
				Toast.show(this, "服务面积只能输入2位小数");
				mCurrent--;
				return;
			}

			mSkirtingBoardLength = mHouseInfoFragment.getSkirtingBoard();
			if (mSkirtingBoardLength == 0) {
				Toast.show(this, "请输入踢脚线长度");
				mCurrent--;
				return;
			}
			
			int posDot2 = String.valueOf(mSkirtingBoardLength).indexOf(".");
			if (String.valueOf(mSkirtingBoardLength).length() - posDot2 - 1 > 2) {
				Toast.show(this, "踢脚线只能输入2位小数");
				mCurrent--;
				return;
			}

			Log.i("MainActivity", "服务面积：" + mArea + "--踢脚线长度："
					+ mSkirtingBoardLength);

			mProperties = mHouseInfoFragment.getDemandProperties();
			if (StringUtils.isNullOrEmpty(mProperties.get服务类型())) {
				Toast.show(this, "请选择服务类型");
				mCurrent--;
				return;
			}

			mDotImg1.setImageResource(R.drawable.dot_normal);
			mDotImg2.setImageResource(R.drawable.dot_selected);
			mDotImg3.setImageResource(R.drawable.dot_normal);

			mTitle1.setTextColor(getResources().getColor(R.color.color_666666));
			mTitle2.setTextColor(getResources().getColor(R.color.title_color));
			mTitle3.setTextColor(getResources().getColor(R.color.color_666666));

			mFloorLeftImg.setImageResource(R.drawable.left_arrow);

			mTransaction.replace(R.id.floor_content, mFloorFragment);
			break;
		case 3:// 选择踢脚线

			if (mProduct == null) {
				Toast.show(this, "请选择地板");
				mCurrent--;
				return;
			}

			Bundle bundle = new Bundle();
			if (mBrand != null) {
				bundle.putString("brandId", mBrand.getId());
				mSkirtingBoardFragment.setArguments(bundle);
				Log.i("MainActivity", "品牌id:" + mBrand.getId());
			}

			mDotImg1.setImageResource(R.drawable.dot_normal);
			mDotImg2.setImageResource(R.drawable.dot_normal);
			mDotImg3.setImageResource(R.drawable.dot_selected);

			mTitle1.setTextColor(getResources().getColor(R.color.color_666666));
			mTitle2.setTextColor(getResources().getColor(R.color.color_666666));
			mTitle3.setTextColor(getResources().getColor(R.color.title_color));

			mFloorLeftImg.setImageResource(R.drawable.left_arrow);

			mTransaction.replace(R.id.floor_content, mSkirtingBoardFragment);
			break;
		case 4:// 确认订单
			if (mSeriesProperty == null) {
				Toast.show(this, "请选择踢脚线");
				mCurrent--;
				return;
			}
			
			if (mUpdateDemandProperties != null) {
				Intent intent = new Intent();
				Bundle args = new Bundle();
				args.putDouble("area",mArea);//服务面积
				args.putSerializable("properties", mProperties);
				args.putSerializable("product", mProduct);
				args.putDouble("skirtingBoardLength", mSkirtingBoardLength);
				args.putSerializable("seriesProperty", mSeriesProperty);
				args.putSerializable("skirtingBoard", mSkirtingBoard);
				args.putSerializable("imageProduct", mFloorFragment.getPreImg()+mProduct.getImageList().get(0));
				intent.putExtras(args);
				setResult(RESULT_OK, intent);
				this.finish();
				return;
			}
			
			
			User user = MyApplication.getUser();
			// 未登录跳转到登录界面
			if (user == null) {
				Intent intent = new Intent(FloorActivity.this,
						LoginActivity.class);
				intent.putExtra("option", "confirmOrder");
				startActivityForResult(intent, LOGIN_REQUEST_CODE);
				mCurrent--;
				return;
			}
			Log.i("MainActivity", "SeriesProperty==" + mSeriesProperty);
			goConfirmOrder();
			mCurrent--;
			break;
		default:
			return;
		}
		Log.i("MainActivity", "当前步骤：" + mCurrent);
		if (index != 4) {
			mTransaction.commit();
		}
	}

	/**
	 * 跳转到确认订单
	 */
	private void goConfirmOrder() {
		Intent intent = new Intent(FloorActivity.this,
				OrderConfirmActivity.class);
		OrderJson orderJson = new OrderJson();
		orderJson.setBrandId(mProduct.getBrandId());// 品牌id
		orderJson.setDemandProperties(mProperties);// 属性
		orderJson.setDemandSpace(String.valueOf(mArea));// 作业面积

		orderJson.setDemandType(3);
		orderJson.setSourceId(1);// 来源 1.Android 2.IOS
		orderJson.setServiceName("地板更新");
		orderJson.setProductImages(mFloorFragment.getPreImg()
				+ mProduct.getImageList().get(0));

		// 主材费
		DemandItem demandItem = new DemandItem();
		demandItem.setBrandId(mProduct.getBrandId());
		demandItem.setMemo(mProduct.getModel());
		demandItem.setName(mProduct.getName());
		demandItem.setPrice(String.valueOf(mProduct.getBnjPrice()));
		demandItem.setProductCode(mProduct.getCode());
		demandItem.setProductId(mProduct.getId());
		demandItem.setQuantity((int)Math.ceil(mArea));
		demandItem.setSmallPic(mFloorFragment.getPreImg()+mProduct.getImageList().get(0));
		demandItem.setType(1);// 主材费

		// 人工安装费
		DemandItem demandItem2 = new DemandItem();
		demandItem2.setBrandId(mProduct.getBrandId());

		String serviceType = mProperties.get服务类型();
		String materialld = mProduct.getMaterialId();
		Log.i("materialld======"+materialld);
		double price2 = 0;
		if ("1107".equals(materialld)) {
			price2 = 0;
		}else if("1102".equals(materialld)){
			price2 = 15;
		}else if("1104".equals(materialld) || "1105".equals(materialld)){
			price2 = 10;
		}else if("1103".equals(materialld) || "1106".equals(materialld)){
			price2 = 30;
		}else if("1101".equals(materialld)){
			price2 = 40;
		}
		
		demandItem2.setPrice(mFormat.format(price2));
		demandItem2.setProductCode(mProduct.getCode());
		demandItem2.setProductId(mProduct.getId());
		demandItem2.setQuantity((int)Math.ceil(mArea));
		demandItem2.setType(2);

		// 辅材费
		DemandItem demandItem3 = new DemandItem();
		demandItem3.setBrandId(mProduct.getBrandId());
		demandItem3.setProductId(mProduct.getId());
		demandItem3.setProductCode(mProduct.getCode());
		demandItem3.setQuantity(1);
		demandItem3.setPrice(String.valueOf(0));
		demandItem3.setType(3);

		// 搬运费
		DemandItem demandItem4 = new DemandItem();
		demandItem4.setBrandId(mProduct.getBrandId());
		String elevator = mProperties.get电梯信息();
		String storey = mProperties.get楼层信息();
		double price = 0;
		if (elevator.equals("无电梯")) {
			if (storey.equals("二层")) {
				price = 2;
			} else if (storey.equals("三层")) {
				price = 4;
			} else if (storey.equals("四层")) {
				price = 6;
			} else if (storey.equals("五层")) {
				price = 8;
			} else if (storey.equals("六层")) {
				price = 10;
			} else if (storey.equals("七层")) {
				price = 12;
			} else {
				price = 0;
			}
		}
		demandItem4.setPrice(mFormat.format(price));
		demandItem4.setProductCode(mProduct.getCode());
		demandItem4.setProductId(mProduct.getId());
		demandItem4.setQuantity((int)Math.ceil(mArea));
		demandItem4.setType(4);

		// 运输费
		DemandItem demandItem5 = new DemandItem();
		demandItem5.setBrandId(mProduct.getBrandId());
		demandItem5.setProductId(mProduct.getId());
		demandItem5.setProductCode(mProduct.getCode());
		demandItem5.setQuantity(1);
		demandItem5.setPrice(String.valueOf(0));
		demandItem5.setType(5);

		// 踢脚线费
		DemandItem demandItem6 = new DemandItem();
		demandItem6.setBrandId(String.valueOf(mSkirtingBoard.getId()));

		double unitLength = mSeriesProperty.getUnitLength();
		double skirtingBoardLength = mHouseInfoFragment.getSkirtingBoard();
		demandItem6.setPrice(String.valueOf(mSeriesProperty.getUnitPrice()));
		demandItem6.setName(mSeriesProperty.getName());
		demandItem6.setMemo(mSeriesProperty.getSpec());
		demandItem6.setProductCode(String.valueOf(mSeriesProperty.getId()));
		demandItem6.setProductId(mSeriesProperty.getId());
		demandItem6.setQuantity((int)Math.ceil(skirtingBoardLength / unitLength));
		demandItem6.setType(6);// 踢脚线费

		// 拆除费
		DemandItem demandItem8 = new DemandItem();
		demandItem8.setBrandId(mProduct.getBrandId());
		demandItem8.setProductId(mProduct.getId());
		demandItem8.setProductCode(mProduct.getCode());
		demandItem8.setQuantity((int)Math.ceil(mArea));
		double price8 = 0;
		if (serviceType.equals("更新地板")) {
			price8 = 20;
		} else {
			price8 = 0;
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
		bundle.putSerializable("demandItem6", demandItem6);
		bundle.putSerializable("demandItem8", demandItem8);
		bundle.putSerializable("demandItem9", demandItem9);
		bundle.putString("type", "floor");
		intent.putExtras(bundle);

		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.floor_left_img:// 上一步
			if (mCurrent > 1) {
				mCurrent--;
				step(mCurrent);
			}
			break;
		case R.id.floor_right_img:// 下一步
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
		mBrand = brandSelected;
	}

	@Override
	public void onUserSelected(Product productSelected) {
		mProduct = productSelected;
	}

	@Override
	public void onUserSelected(SkirtingBoard skirtingBoard) {
		mSkirtingBoard = skirtingBoard;
	}

	@Override
	public void onUserSelected(SeriesProperty seriesProperty) {
		mSeriesProperty = seriesProperty;
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
