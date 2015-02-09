package com.bangninjia.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.adapter.SchoolAdapter;
import com.bangninjia.app.model.School;
import com.bangninjia.app.model.SchoolData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.google.gson.reflect.TypeToken;

/**
 * 知识学堂
 * 
 */
public class SchoolActivity extends BaseNetActivity implements OnClickListener,
		OnRefreshListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private ViewPager mSchoolVp;

	private TextView mAllTv;
	private TextView mWallTv;
	private TextView mWallpaperTv;
	private TextView mFloorTv;

	private List<View> mViews;

	private ListView mAllLv;
	private ListView mWallLv;
	private ListView mWallpaperLv;
	private ListView mFloorLv;

	private List<School> mAllList;
	private List<School> mWallList;
	private List<School> mWallpaperList;
	private List<School> mFloorList;

	private SchoolAdapter mAllAdapter;
	private SchoolAdapter mWallAdapter;
	private SchoolAdapter mWallpaperAdapter;
	private SchoolAdapter mFloorAdapter;

	private SwipeRefreshLayout mSwipeRefreshLayoutAll;
	private SwipeRefreshLayout mSwipeRefreshLayoutWall;
	private SwipeRefreshLayout mSwipeRefreshLayoutWallpaper;
	private SwipeRefreshLayout mSwipeRefreshLayoutFloor;

	private int mCurrentPosition = 0;
	private int mPosition = 0;
	private MyProgressDialog mProgressDialog;

	private static final int SCHOOL_0_REQUEST_CODE = 100;// 全部
	private static final int SCHOOL_1_REQUEST_CODE = 101;// 墙面
	private static final int SCHOOL_2_REQUEST_CODE = 102;// 壁纸
	private static final int SCHOOL_3_REQUEST_CODE = 103;// 地板

	private int mAllCount = 0;
	private int mWallCount = 0;
	private int mWallpaperCount = 0;
	private int mFloorCount = 0;
	
	private int mAllStartRow = 0;
	private int mWallStartRow = 0;
	private int mWallpaperStartRow = 0;
	private int mFloorStartRow = 0;

	private View mFooter;
	private ProgressBar mBar;
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school);

		initView();

		mViews = new ArrayList<View>();
		mProgressDialog = new MyProgressDialog(this, "正在加载");
		mFooter = View.inflate(this, R.layout.list_footer, null);
		mBar = (ProgressBar) mFooter.findViewById(R.id.footer_progressBar);
		mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
		mTextView.setOnClickListener(this);
		
		mAllList = new ArrayList<School>();
		mWallList = new ArrayList<School>();
		mWallpaperList = new ArrayList<School>();
		mFloorList = new ArrayList<School>();
		
		// 全部
		View viewAll = View.inflate(this, R.layout.school_listview, null);
		mSwipeRefreshLayoutAll = (SwipeRefreshLayout) viewAll
				.findViewById(R.id.school_swipeRefreshLayout);
		mAllLv = (ListView) viewAll.findViewById(R.id.school_listview);
		 mAllLv.addFooterView(mFooter);
		mSwipeRefreshLayoutAll.setOnRefreshListener(this);
		mSwipeRefreshLayoutAll.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		

		mAllLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 滚动条停止的时候
					// 记录滚动条停止时看到的第一个元素的位置
					mPosition = view.getFirstVisiblePosition();
					Log.i("position:" + mPosition);
					// 在这里判断是否到达底部，如果到达就自动加载数据
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 先获取能看到的最下边的那个条目的位置,看是否等于list所有条目数
						// mTask = new MyTask();
						// mTask.execute();
						if (mAllCount >= mAllStartRow) {
							loadData(mCurrentPosition);
						}else{
							mTextView.setText("没有更多数据了");
						}
					}

					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 滚动条正在滚动

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸滚动

					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		
		// 设置Item点击监听时间
		mAllLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						SchoolDetailActivity.class);
				intent.putExtra("School", mAllList.get(position));
				Log.i("title======="+mAllList.get(position).getTitle());
				startActivity(intent);
			}
		});

		// 墙面
		View viewWall = View.inflate(this, R.layout.school_listview, null);
		mSwipeRefreshLayoutWall = (SwipeRefreshLayout) viewWall
				.findViewById(R.id.school_swipeRefreshLayout);
		mWallLv = (ListView) viewWall.findViewById(R.id.school_listview);
		mWallLv.addFooterView(mFooter);
		mSwipeRefreshLayoutWall.setOnRefreshListener(this);
		mSwipeRefreshLayoutWall.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mWallLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 滚动条停止的时候
					// 记录滚动条停止时看到的第一个元素的位置
					mPosition = view.getFirstVisiblePosition();
					Log.i("position:" + mPosition);
					// 在这里判断是否到达底部，如果到达就自动加载数据
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 先获取能看到的最下边的那个条目的位置,看是否等于list所有条目数
//						MyTask mTask = new MyTask();
//						mTask.execute();
						if (mWallCount >= mWallStartRow) {
							loadData(mCurrentPosition);
						}else{
							mTextView.setText("没有更多数据了");
						}
					}

					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 滚动条正在滚动

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸滚动

					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		// 设置Item点击监听时间
		mWallLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						SchoolDetailActivity.class);
				intent.putExtra("School", mWallList.get(position));
				Log.i("title======="+mWallList.get(position).getTitle());
				startActivity(intent);
			}
		});

		// 壁纸
		View viewWallpaper = View.inflate(this, R.layout.school_listview, null);
		mSwipeRefreshLayoutWallpaper = (SwipeRefreshLayout) viewWallpaper
				.findViewById(R.id.school_swipeRefreshLayout);
		mWallpaperLv = (ListView) viewWallpaper
				.findViewById(R.id.school_listview);
		mWallpaperLv.addFooterView(mFooter);
		mSwipeRefreshLayoutWallpaper.setOnRefreshListener(this);
		mSwipeRefreshLayoutWallpaper.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		
		mWallpaperLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 滚动条停止的时候
					// 记录滚动条停止时看到的第一个元素的位置
					mPosition = view.getFirstVisiblePosition();
					Log.i("position:" + mPosition);
					// 在这里判断是否到达底部，如果到达就自动加载数据
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 先获取能看到的最下边的那个条目的位置,看是否等于list所有条目数
//						MyTask mTask = new MyTask();
//						mTask.execute();
						if (mWallpaperCount >= mWallpaperStartRow) {
							loadData(mCurrentPosition);
						}else{
							mTextView.setText("没有更多数据了");
						}
					}

					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 滚动条正在滚动

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸滚动

					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		// 设置Item点击监听时间
		mWallpaperLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						SchoolDetailActivity.class);
				intent.putExtra("School", mWallpaperList.get(position));
				Log.i("title======="+mWallpaperList.get(position).getTitle());
				startActivity(intent);
			}
		});
		
		// 地板
		View viewFloor = View.inflate(this, R.layout.school_listview, null);
		mSwipeRefreshLayoutFloor = (SwipeRefreshLayout) viewFloor
				.findViewById(R.id.school_swipeRefreshLayout);
		mFloorLv = (ListView) viewFloor.findViewById(R.id.school_listview);
		mFloorLv.addFooterView(mFooter);
		mSwipeRefreshLayoutFloor.setOnRefreshListener(this);
		mSwipeRefreshLayoutFloor.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		mViews.add(viewAll);
		mViews.add(viewWall);
		mViews.add(viewWallpaper);
		mViews.add(viewFloor);
		
		mFloorLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 滚动条停止的时候
					// 记录滚动条停止时看到的第一个元素的位置
					mPosition = view.getFirstVisiblePosition();
					Log.i("position:" + mPosition);
					// 在这里判断是否到达底部，如果到达就自动加载数据
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 先获取能看到的最下边的那个条目的位置,看是否等于list所有条目数
//						MyTask mTask = new MyTask();
//						mTask.execute();
						if (mFloorCount >= mFloorStartRow) {
							loadData(mCurrentPosition);
						}else{
							mTextView.setText("没有更多数据了");
						}
					}

					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 滚动条正在滚动

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸滚动

					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		// 设置Item点击监听时间
		mFloorLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						SchoolDetailActivity.class);
				intent.putExtra("School", mFloorList.get(position));
				Log.i("title======="+mFloorList.get(position).getTitle());
				startActivity(intent);
			}
		});

		mSchoolVp.setAdapter(new MyPagerAdapter(mViews));
		mSchoolVp.setOnPageChangeListener(new MyPageChangeListener());
		//mSchoolVp.setCurrentItem(0);
		initData(0);

	}

	/**
	 * 初始化View
	 */
	private void initView() {

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.school);
		mTitleLeftBtn.setOnClickListener(this);
		mTitleRightBtn.setVisibility(View.GONE);

		mAllTv = (TextView) this.findViewById(R.id.school_all);
		mWallTv = (TextView) this.findViewById(R.id.school_wall);
		mWallpaperTv = (TextView) this.findViewById(R.id.school_wallpaper);
		mFloorTv = (TextView) this.findViewById(R.id.school_floor);

		mAllTv.setOnClickListener(this);
		mWallTv.setOnClickListener(this);
		mWallpaperTv.setOnClickListener(this);
		mFloorTv.setOnClickListener(this);

		mSchoolVp = (ViewPager) this.findViewById(R.id.school_vp);
	}

	/**
	 * 加载数据
	 */
	private void initData(int index) {
		switch (index) {
		case 0:
			Map<String, String> params0 = new HashMap<String, String>();

			SchoolData data0 = new SchoolData();
			data0.setIsPage("true");
			data0.setTypeId("");
			data0.setStartRow(mAllStartRow);
			data0.setRowCount(10);

			String dataString0 = MyApplication.getGson().toJson(data0);
			params0.put("data", dataString0);
			Log.i("MainActivity", "SchoolData==" + dataString0);

			post(SCHOOL_0_REQUEST_CODE, Constants.SCHOOL_URL, params0);

			break;
		case 1:// 墙面
			Map<String, String> params = new HashMap<String, String>();

			SchoolData data = new SchoolData();
			data.setIsPage("true");
			data.setTypeId("1");
			data.setStartRow(0);
			data.setRowCount(10);

			String dataString = MyApplication.getGson().toJson(data);
			params.put("data", dataString);
			Log.i("MainActivity", "SchoolData==" + dataString);

			post(SCHOOL_1_REQUEST_CODE, Constants.SCHOOL_URL, params);

			break;
		case 2:// 壁纸
			Map<String, String> params2 = new HashMap<String, String>();

			SchoolData data2 = new SchoolData();
			data2.setIsPage("true");
			data2.setTypeId("2");
			data2.setStartRow(0);
			data2.setRowCount(10);

			String dataString2 = MyApplication.getGson().toJson(data2);
			params2.put("data", dataString2);
			Log.i("MainActivity", "SchoolData==" + dataString2);

			post(SCHOOL_2_REQUEST_CODE, Constants.SCHOOL_URL, params2);

			break;
		case 3:// 地板

			Map<String, String> params3 = new HashMap<String, String>();

			SchoolData data3 = new SchoolData();
			data3.setIsPage("true");
			data3.setTypeId("3");
			data3.setStartRow(0);
			data3.setRowCount(10);

			String dataString3 = MyApplication.getGson().toJson(data3);
			params3.put("data", dataString3);
			Log.i("MainActivity", "SchoolData==" + dataString3);

			post(SCHOOL_3_REQUEST_CODE, Constants.SCHOOL_URL, params3);

			break;
		default:
			break;
		}
	}

	/**
	 * 加载数据
	 * @param type
	 */
	private void loadData(int type) {
		if (type == 0) {
			Map<String, String> params = new HashMap<String, String>();

			SchoolData data = new SchoolData();
			data.setIsPage("true");
			data.setTypeId("");
			data.setStartRow(mAllStartRow);
			data.setRowCount(10);

			String dataString = MyApplication.getGson().toJson(data);
			params.put("data", dataString);
			Log.i("MainActivity", "SchoolData==" + dataString);

			post(SCHOOL_0_REQUEST_CODE, Constants.SCHOOL_URL, params);
		}else if(type == 1){
			Map<String, String> params = new HashMap<String, String>();

			SchoolData data = new SchoolData();
			data.setIsPage("true");
			data.setTypeId("1");
			data.setStartRow(mWallStartRow);
			data.setRowCount(10);

			String dataString = MyApplication.getGson().toJson(data);
			params.put("data", dataString);
			Log.i("MainActivity", "SchoolData==" + dataString);

			post(SCHOOL_1_REQUEST_CODE, Constants.SCHOOL_URL, params);
		}else if(type == 2){
			Map<String, String> params = new HashMap<String, String>();

			SchoolData data = new SchoolData();
			data.setIsPage("true");
			data.setTypeId("2");
			data.setStartRow(mWallpaperStartRow);
			data.setRowCount(10);

			String dataString = MyApplication.getGson().toJson(data);
			params.put("data", dataString);
			Log.i("MainActivity", "SchoolData==" + dataString);

			post(SCHOOL_2_REQUEST_CODE, Constants.SCHOOL_URL, params);
		}else if(type == 3){
			Map<String, String> params = new HashMap<String, String>();

			SchoolData data = new SchoolData();
			data.setIsPage("true");
			data.setTypeId("3");
			data.setStartRow(mFloorStartRow);
			data.setRowCount(10);

			String dataString = MyApplication.getGson().toJson(data);
			params.put("data", dataString);
			Log.i("MainActivity", "SchoolData==" + dataString);

			post(SCHOOL_3_REQUEST_CODE, Constants.SCHOOL_URL, params);
		}
	}

	/**
	 * 自定义PagerAdapter
	 * 
	 */
	private class MyPagerAdapter extends PagerAdapter {

		private List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(mListViews.get(position));
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	/**
	 * ViewPager监听
	 * 
	 */
	public class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			Log.i("切换到Tab:" + position);
			switch (position) {
			case 0:
				mAllTv.setTextColor(getResources()
						.getColor(R.color.title_color));
				mWallTv.setTextColor(getResources().getColor(R.color.color_222));
				mWallpaperTv.setTextColor(getResources().getColor(
						R.color.color_222));
				mFloorTv.setTextColor(getResources()
						.getColor(R.color.color_222));

				break;
			case 1:
				mAllTv.setTextColor(getResources().getColor(R.color.color_222));
				mWallTv.setTextColor(getResources().getColor(
						R.color.title_color));
				mWallpaperTv.setTextColor(getResources().getColor(
						R.color.color_222));
				mFloorTv.setTextColor(getResources()
						.getColor(R.color.color_222));

				break;
			case 2:
				mAllTv.setTextColor(getResources().getColor(R.color.color_222));
				mWallTv.setTextColor(getResources().getColor(R.color.color_222));
				mWallpaperTv.setTextColor(getResources().getColor(
						R.color.title_color));
				mFloorTv.setTextColor(getResources()
						.getColor(R.color.color_222));
				break;
			case 3:
				mAllTv.setTextColor(getResources().getColor(R.color.color_222));
				mWallTv.setTextColor(getResources().getColor(R.color.color_222));
				mWallpaperTv.setTextColor(getResources().getColor(
						R.color.color_222));
				mFloorTv.setTextColor(getResources().getColor(
						R.color.title_color));
				break;
			default:
				break;
			}
			
			mTextView.setText("加载更多");
			mAllCount = 0;
			mAllStartRow = 0;
			mWallCount = 0;
			mWallStartRow = 0;
			mWallpaperCount = 0;
			mWallpaperStartRow = 0;
			mFloorCount = 0;
			mFloorStartRow = 0;

			mCurrentPosition = position;
			initData(position);
		}
		
		

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private void setCurrentItem(int index) {
		switch (index) {
		case 0:
			mAllTv.setTextColor(getResources().getColor(R.color.title_color));
			mWallTv.setTextColor(getResources().getColor(R.color.color_222));
			mWallpaperTv.setTextColor(getResources()
					.getColor(R.color.color_222));
			mFloorTv.setTextColor(getResources().getColor(R.color.color_222));
			break;
		case 1:
			mAllTv.setTextColor(getResources().getColor(R.color.color_222));
			mWallTv.setTextColor(getResources().getColor(R.color.title_color));
			mWallpaperTv.setTextColor(getResources()
					.getColor(R.color.color_222));
			mFloorTv.setTextColor(getResources().getColor(R.color.color_222));
			break;
		case 2:
			mAllTv.setTextColor(getResources().getColor(R.color.color_222));
			mWallTv.setTextColor(getResources().getColor(R.color.color_222));
			mWallpaperTv.setTextColor(getResources().getColor(
					R.color.title_color));
			mFloorTv.setTextColor(getResources().getColor(R.color.color_222));
			break;
		case 3:
			mAllTv.setTextColor(getResources().getColor(R.color.color_222));
			mWallTv.setTextColor(getResources().getColor(R.color.color_222));
			mWallpaperTv.setTextColor(getResources()
					.getColor(R.color.color_222));
			mFloorTv.setTextColor(getResources().getColor(R.color.title_color));
			
			break;
		default:
			break;
		}
		mSchoolVp.setCurrentItem(index);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:// 返回
			this.finish();
			break;
		case R.id.school_all:
			setCurrentItem(0);
			break;
		case R.id.school_wall:
			setCurrentItem(1);
			break;
		case R.id.school_wallpaper:
			setCurrentItem(2);
			break;
		case R.id.school_floor:
			setCurrentItem(3);
			break;
		case R.id.footer_text:
			
			if (mCurrentPosition == 0) {
				if (mAllCount >= mAllStartRow) {
					loadData(mCurrentPosition);
				}else{
					mTextView.setText("没有更多数据了");
				}
			}else  if (mCurrentPosition == 1) {
				if (mWallCount >= mWallStartRow) {
					loadData(mCurrentPosition);
				}else{
					mTextView.setText("没有更多数据了");
				}
			}else  if (mCurrentPosition == 2) {
				if (mWallpaperCount >= mWallpaperStartRow) {
					loadData(mCurrentPosition);
				}else{
					mTextView.setText("没有更多数据了");
				}
			}else  if (mCurrentPosition == 3) {
				if (mFloorCount >= mFloorStartRow) {
					loadData(mCurrentPosition);
				}else{
					mTextView.setText("没有更多数据了");
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		Log.i("刷新Tab:" + mCurrentPosition);
		mTextView.setText("加载更多");
		if (mCurrentPosition == 0) {
			mAllCount = 0;
			mAllStartRow = 0;
		}else if(mCurrentPosition == 1){
			mWallCount = 0;
			mWallStartRow = 0;
		}else if(mCurrentPosition == 2){
			mWallpaperCount = 0;
			mWallpaperStartRow = 0;
		}else if(mCurrentPosition == 3){
			mFloorCount = 0;
			mFloorStartRow = 0;
		}
		initData(mCurrentPosition);
	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		if (mCurrentPosition == 0) {
			if (mAllStartRow == 0) {
				mProgressDialog.show();
			}else{
				mBar.setVisibility(ProgressBar.VISIBLE);
			}
		}else if(mCurrentPosition == 1){
			if (mWallStartRow == 0) {
				mProgressDialog.show();
			}else{
				mBar.setVisibility(ProgressBar.VISIBLE);
			}
		}else if(mCurrentPosition == 2){
			if (mWallpaperStartRow == 0) {
				mProgressDialog.show();
			}else{
				mBar.setVisibility(ProgressBar.VISIBLE);
			}
		}else if(mCurrentPosition == 3){
			if (mFloorStartRow == 0) {
				mProgressDialog.show();
			}else{
				mBar.setVisibility(ProgressBar.VISIBLE);
			}
		}
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == SCHOOL_0_REQUEST_CODE) {// 全部
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.optString("msg");
				if (code == 1000) {
					JSONArray array = object.optJSONArray("list");
					TypeToken<List<School>> toTypeToken = new TypeToken<List<School>>() {
					};
					List<School> list = MyApplication.getGson().fromJson(
							array.toString(), toTypeToken.getType());
					if (mAllStartRow == 0) {
						mAllList.clear();
						mAllList.addAll(list);
						mAllAdapter = new SchoolAdapter(this, mAllList);
						mAllLv.setAdapter(mAllAdapter);
					}
					if (mAllStartRow > 0) {
						mAllList.addAll(list);
						mAllAdapter.notifyDataSetChanged();
					}
					mAllCount = object.optInt("pageCount");
					Log.i("总条数:" + mAllCount);
//					if (mAllStartRow == 0 && mAllCount > 5) {
//						mAllLv.addFooterView(mFooter);
//					}
					if (mAllCount < 10) {
						mAllLv.removeFooterView(mFooter);
					}
					mAllStartRow += 10;

				} else {
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == SCHOOL_1_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.optString("msg");
				if (code == 1000) {
					JSONArray array = object.optJSONArray("list");
					TypeToken<List<School>> toTypeToken = new TypeToken<List<School>>() {
					};
					List<School> list = MyApplication.getGson().fromJson(
							array.toString(), toTypeToken.getType());
					
					if (mWallStartRow == 0) {
						mWallList.clear();
						mWallList.addAll(list);
						mWallAdapter = new SchoolAdapter(this, mWallList);
						mWallLv.setAdapter(mWallAdapter);
					}
					
					if (mWallStartRow > 0) {
						mWallList.addAll(list);
						mWallAdapter.notifyDataSetChanged();
					}
					
					mWallCount = object.optInt("pageCount");
					Log.i("总条数：" + mWallCount);
//					if (mWallStartRow == 0 && mWallCount > 10) {
//						mWallLv.addFooterView(mFooter);
//					}
					if (mWallCount < 10) {
						mWallLv.removeFooterView(mFooter);
					}
					mWallStartRow += 10;
				} else {
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == SCHOOL_2_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.optString("msg");
				if (code == 1000) {
					JSONArray array = object.optJSONArray("list");
					TypeToken<List<School>> toTypeToken = new TypeToken<List<School>>() {
					};
					List<School> list = MyApplication.getGson().fromJson(
							array.toString(), toTypeToken.getType());
					if (mWallpaperStartRow == 0) {
						mWallpaperList.clear();
						mWallpaperList.addAll(list);
						mWallpaperAdapter = new SchoolAdapter(this, mWallpaperList);
						mWallpaperLv.setAdapter(mWallpaperAdapter);
					}
					if (mWallpaperStartRow > 0) {
						mWallpaperList.addAll(list);
						mWallpaperAdapter.notifyDataSetChanged();
					}
					mWallpaperCount = object.optInt("pageCount");
					Log.i("总条数：" + mWallpaperCount);
//					if (mWallpaperStartRow == 0 && mWallpaperCount > 10) {
//						mWallpaperLv.addFooterView(mFooter);
//					}
					if (mWallpaperCount < 10) {
						mWallpaperLv.removeFooterView(mFooter);
					}
					mWallpaperStartRow += 10;
				} else {
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == SCHOOL_3_REQUEST_CODE) {
			try {
				JSONObject object = new JSONObject(jsonData);
				int code = object.optInt("code");
				String msg = object.optString("msg");
				if (code == 1000) {
					JSONArray array = object.optJSONArray("list");
					TypeToken<List<School>> toTypeToken = new TypeToken<List<School>>() {
					};
					List<School> list = MyApplication.getGson().fromJson(
							array.toString(), toTypeToken.getType());
					if (mFloorStartRow == 0) {
						mFloorList.clear();
						mFloorList.addAll(list);
						mFloorAdapter = new SchoolAdapter(this, mFloorList);
						mFloorLv.setAdapter(mFloorAdapter);
					}
					if (mFloorStartRow > 0) {
						mFloorList.addAll(list);
						mFloorAdapter.notifyDataSetChanged();
					}
					
					mFloorCount = object.optInt("pageCount");
					Log.i("总条数：" + mFloorCount);
//					if (mFloorStartRow == 0 && mFloorCount > 10) {
//						mFloorLv.addFooterView(mFooter);
//					}
					if (mFloorCount < 10) {
						mFloorLv.removeFooterView(mFooter);
					}
					mFloorStartRow += 10;
				} else {
					Toast.show(this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNetworkFinish(int requestCode) {
		mProgressDialog.dismiss();
		mBar.setVisibility(ProgressBar.GONE);
		
		mSwipeRefreshLayoutAll.setRefreshing(false);
		mSwipeRefreshLayoutWall.setRefreshing(false);
		mSwipeRefreshLayoutWallpaper.setRefreshing(false);
		mSwipeRefreshLayoutFloor.setRefreshing(false);
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		Toast.show(this, "加载失败，请检查您的网络");
	}

//	private class MyTask extends AsyncTask<Void, Void, Void> {
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			// 任务开始前 显示进度条
//			mBar.setVisibility(ProgressBar.VISIBLE);
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//
//			// 模拟加载运行
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			loadData(mCurrentPosition);
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//			// 任务完成后 即数据加载完成后 通知更新UI 并隐藏进度条
//			mWallAdapter.notifyDataSetChanged();
//			mBar.setVisibility(ProgressBar.GONE);
//		}
//
//	}

}
