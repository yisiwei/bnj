package com.bangninjia.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.loopj.android.http.AsyncHttpClient;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.bangninjia.R;
import com.bangninjia.app.model.User;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.PreferencesUtils;
import com.bangninjia.app.utils.StringUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApplication extends Application {

	private static String comeFrom = "1"; // 记录登录人员的身份 1 ：用户 2：商家 3： 作业人员

	private static ImageLoader imageLoader; // /图片加载器
	private static Context context;

	private static User user;
	private static AsyncHttpClient asyncHttpClient; // 异步访问网络数据客户端
	private static Gson gson; // 使用GSon解析数据

	/**
	 * DisplayImageOptions是用于设置图片显示的类
	 */
	private static DisplayImageOptions options;

	private List<Activity> activities = new ArrayList<Activity>();
	private static MyApplication instance;

	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		gson = new Gson();
		asyncHttpClient = new AsyncHttpClient();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(getConfiguration());
		initShowImageLoader();

		getAccount();
	}

	/**
	 * 获取保存的登录信息
	 */
	public void getAccount() {
		String userId = PreferencesUtils.getString(this, "userId");
		String userName = PreferencesUtils.getString(this, "userName");
		String comeFrom = PreferencesUtils.getString(this, "comeFrom");
		if (!StringUtils.isNullOrEmpty(userId)) {
			MyApplication.setUser(new User(userId, userName));
			MyApplication.setComeFrom(comeFrom);
		}

	}

	/**
	 * 存放Activity到list中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	/**
	 * 移除activity
	 */
	public void removeActivities() {
		for (Activity activity : activities) {
			activity.finish();
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i("程序退出");
	}
	
	public static Context getContext() {
		return context;
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static AsyncHttpClient getAsyncHttpClient() {
		return asyncHttpClient;
	}

	public static Gson getGson() {
		return gson;
	}

	public static void setComeFrom(String comeFrom) {
		MyApplication.comeFrom = comeFrom;
	}

	public static String getComeFrom() {
		return comeFrom;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		MyApplication.user = user;
	}

	private ImageLoaderConfiguration getConfiguration() {

		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "bangninjia/Cache");

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.diskCacheExtraOptions(480, 800, null)
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// 设置优先级
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)

				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 自定义缓存路径
				.diskCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				//.imageDecoder(new BaseImageDecoder(true))
				// connectTimeout(5s),
				// readTimeout(30s)超时时间
				//.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		return configuration;
	}

	private void initShowImageLoader() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中

				.build();
	}

	public static DisplayImageOptions getDisplayImageOptions() {
		return options;
	}

}
