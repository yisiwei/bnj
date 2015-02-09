package com.bangninjia.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bangninjia.R;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;

public class DownLoadService extends Service {

	public static final int APP_VERSION_LATEST = 0;
	public static final int APP_VERSION_OLDER = 1;
	public static final int mNotificationId = 100;

	private String mDownloadUrl = null;// 下载apk文件的路径
	//private float serverVerCode = -1;
	private NotificationManager mNotificationManager = null;// 消息管理器
	private Notification mNotification = null;// 消息
	private PendingIntent mPendingIntent = null;//

	private File destDir = null;// 目标文件路径
	private File destFile = null;// 目标文件

	private static final int DOWNLOAD_FAIL = -1;// 下载失败
	private static final int DOWNLOAD_SUCCESS = 0;// 下载成功
	private static final int NO_DOWNLOAD = 1;//
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				Toast.makeText(getApplicationContext(), "下载成功!",
						Toast.LENGTH_LONG).show();
				install(destFile);
				break;
			case DOWNLOAD_FAIL:
				Toast.makeText(getApplicationContext(), "下载失败!",
						Toast.LENGTH_LONG).show();
				mNotificationManager.cancel(mNotificationId);
				break;
			case NO_DOWNLOAD:
				Toast.makeText(getApplicationContext(), "检测不到SD卡无法下载更新",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 文件下载进度的监听器
	 */
	private DownloadListener downloadListener = new DownloadListener() {

		/**
		 * 下载中
		 */
		@Override
		public void downloading(int progress) {
			Log.i("MainActivity", "downloading");
			mNotification.contentView.setProgressBar(
					R.id.app_upgrade_progressbar, 100, progress, false);
			mNotification.contentView.setTextViewText(
					R.id.app_upgrade_progresstext, progress + "%");
			mNotificationManager.notify(mNotificationId, mNotification);
		}

		/**
		 * 下载完成
		 */
		@Override
		public void downloaded() {
			Log.i("MainActivity", "-----downloaded");
			mNotification.contentView.setViewVisibility(
					R.id.app_upgrade_progressblock, View.GONE);
			mNotification.defaults = Notification.DEFAULT_SOUND;
			mNotification.contentIntent = mPendingIntent;
			mNotification.contentView.setTextViewText(
					R.id.app_upgrade_progresstext, "下载完成。");
			mNotificationManager.notify(mNotificationId, mNotification);
			if (destFile.exists() && destFile.isFile()
					&& checkApkFile(destFile.getPath())) {
				Message msg = mHandler.obtainMessage();
				msg.what = DOWNLOAD_SUCCESS;
				mHandler.sendMessage(msg);
			}
			mNotificationManager.cancel(mNotificationId);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("升级的服务启动了-------------");
		mDownloadUrl = intent.getStringExtra("downloadUrl");
		Log.i("MainActivity", mDownloadUrl);
		//serverVerCode = intent.getFloatExtra("serverVerCode", -1);
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			destDir = new File(Constants.UPGRADE_DOWNLOAD_PATH);
			if (destDir.exists()) {
				@SuppressWarnings("deprecation")
				File destFile = new File(destDir.getPath() + "/"
						+ URLEncoder.encode(mDownloadUrl));
				if (destFile.exists() && destFile.isFile()
						&& checkApkFile(destFile.getPath())) {
					install(destFile);
					stopSelf();
					return super.onStartCommand(intent, flags, startId);
				}
			}
		} else {
			Message msg = new Message();
			msg.what = NO_DOWNLOAD;
			mHandler.sendMessage(msg);
			return super.onStartCommand(intent, flags, startId);
		}
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotification = new Notification();
		mNotification.contentView = new RemoteViews(getApplication()
				.getPackageName(), R.layout.download_notification_view);
		Intent completingIntent = new Intent();
		completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		completingIntent.setClass(getApplication().getApplicationContext(),
				DownLoadService.class);

		mPendingIntent = PendingIntent.getActivity(DownLoadService.this,
				R.string.app_name, completingIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mNotification.icon = R.drawable.ic_launcher;
		mNotification.tickerText = "正在下载";
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		mNotification.contentIntent = mPendingIntent;
		mNotification.contentView.setProgressBar(R.id.app_upgrade_progressbar,
				100, 0, false);
		mNotification.contentView.setTextViewText(
				R.id.app_upgrade_progresstext, "0%");
		// mNotificationManager.cancel(mNotificationId);
		mNotificationManager.notify(mNotificationId, mNotification);
		new AppUpgradeThread().start();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
     * 
     */
	class AppUpgradeThread extends Thread {

		@Override
		public void run() {

			if (Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				if (destDir == null) {
					destDir = new File(Constants.UPGRADE_DOWNLOAD_PATH);
				}
				if (destDir.exists() || destDir.mkdirs()) {
					Log.i("MainActivity", "destDir  exists");
					destFile = new File(Constants.UPGRADE_DOWNLOAD_PATH
							+ Constants.APK_NAME);
					// 如果文件已存在直接安装
					if (destFile.exists() && destFile.isFile()
							&& checkApkFile(destFile.getPath())) {
//						int localVerCode = APKUtils.getVersionCode(destFile
//								.getAbsolutePath());
//						if (serverVerCode > localVerCode) {
//							// 如果服务端版本号大于本地，重新下载
//							try {
//								DownloadUtils.download(mDownloadUrl, destFile,
//										false, downloadListener);
//							} catch (Exception e) {
//								Message msg = mHandler.obtainMessage();
//								msg.what = DOWNLOAD_FAIL;
//								mHandler.sendMessage(msg);
//								e.printStackTrace();
//							}
//						} else {
//							// 否则直接安装
//							install(destFile);
//						}
						try {
							DownloadUtils.download(mDownloadUrl, destFile,
									false, downloadListener);
						} catch (Exception e) {
							Message msg = mHandler.obtainMessage();
							msg.what = DOWNLOAD_FAIL;
							mHandler.sendMessage(msg);
							e.printStackTrace();
						}
					} else {
						try {
							DownloadUtils.download(mDownloadUrl, destFile,
									false, downloadListener);
						} catch (Exception e) {
							Message msg = mHandler.obtainMessage();
							msg.what = DOWNLOAD_FAIL;
							mHandler.sendMessage(msg);
							e.printStackTrace();
						}
					}
				}
			} else {
				Message msg = mHandler.obtainMessage();
				msg.what = NO_DOWNLOAD;
				mHandler.sendMessage(msg);
			}
			// stopSelf();// 停止服务
		}
	}

	public boolean checkApkFile(String apkFilePath) {
		boolean result = false;
		try {
			PackageManager pManager = getPackageManager();
			PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath,
					PackageManager.GET_ACTIVITIES);
			if (pInfo == null) {
				result = false;
			} else {
				result = true;
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 安装APK文件
	 * 
	 * @param apkFile
	 */
	public void install(File apkFile) {
		mNotificationManager.cancel(mNotificationId);
		stopSelf();// 停止服务
		Uri uri = Uri.fromFile(apkFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);
	}

	// =============================================================================
	/**
	 * 下载进度的监听器
	 */
	public interface DownloadListener {

		/**
		 * 下载中
		 * 
		 * @param progress
		 */
		public void downloading(int progress);

		/**
		 * 下载完成
		 */
		public void downloaded();
	}

	static class DownloadUtils {
		/**
		 * 网络连接的超时时间
		 */
		private static final int CONNECT_TIMEOUT = 10000;

		/**
		 * 数据的超时时间
		 */
		private static final int DATA_TIMEOUT = 40000;

		/**
		 * 下载数据的缓冲区大小
		 */
		private final static int DATA_BUFFER = 8192;

		private static int downloadProgress = 0;

		/**
		 * 下载文件，并返回下载文件的大小
		 * 
		 * @param urlStr
		 * @param dest
		 * @param append
		 * @param downloadListener
		 * @return
		 * @throws Exception
		 */
		public static long download(String urlStr, File dest, boolean append,
				final DownloadListener downloadListener) throws Exception {
			Log.i("MainActivity", "-------net   download");
			downloadProgress = 0;
			long remoteSize = 0;
			int currentSize = 0;
			long totalSize = -1;
			if (!append && dest.exists() && dest.isFile()) {
				dest.delete();
			}

			if (append && dest.exists() && dest.exists()) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(dest);
					currentSize = fis.available();
				} catch (IOException e) {
					throw e;
				} finally {
					if (fis != null) {
						fis.close();
					}
				}
			}
			HttpGet request = new HttpGet(urlStr);
			if (currentSize > 0) {
				request.addHeader("RANGE", "bytes=" + currentSize + "-");
			}
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, DATA_TIMEOUT);
			HttpClient httpClient = new DefaultHttpClient(params);

			InputStream is = null;
			FileOutputStream os = null;
			try {
				HttpResponse response = httpClient.execute(request);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					is = response.getEntity().getContent();
					remoteSize = response.getEntity().getContentLength();
					Header contentEncoding = response
							.getFirstHeader("Content-Encoding");
					if (contentEncoding != null
							&& contentEncoding.getValue().equalsIgnoreCase(
									"gzip")) {
						is = new GZIPInputStream(is);
					}
					os = new FileOutputStream(dest, append);
					byte buffer[] = new byte[DATA_BUFFER];
					int readSize = 0;
					// 监听进度的线程
					Thread MonitorThread = new Thread(new Runnable() {

						@Override
						public void run() {
							if (downloadListener == null) {
								return;
							}
							while (downloadProgress < 99) {
								downloadListener.downloading(downloadProgress);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					});
					MonitorThread.start();
					while ((readSize = is.read(buffer)) > 0) {
						os.write(buffer, 0, readSize);
						os.flush();
						totalSize += readSize;
						downloadProgress = (int) (totalSize * 100 / remoteSize);
					}
					if (totalSize < 0) {
						totalSize = 0;
					}
					// if(MonitorThread.isAlive()){
					// MonitorThread.stop();//结束监听进度的线程
					// }
				}
			} finally {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			}
			if (totalSize < 0) {
				throw new Exception("Download file fail: " + urlStr);
			}

			if (downloadListener != null) {
				downloadListener.downloaded();
			}
			return totalSize;
		}
	}

}
