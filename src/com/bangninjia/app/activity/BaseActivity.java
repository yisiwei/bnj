package com.bangninjia.app.activity;

import java.util.Map;

import org.loopj.android.http.AsyncHttpResponseHandler;
import org.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;

import com.bangninjia.app.MyApplication;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.view.MyProgressDialog;


public abstract class BaseActivity extends Activity {

	MyProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgressDialog = new MyProgressDialog(this, "正在加载");
	}

	/**
	 * 发送GET请求
	 * 
	 * @param requestCode
	 * @param url
	 */
	public void get(int requestCode, String url) {
		if (!url.contains("http://")) {
			MyApplication.getAsyncHttpClient().get("http://" + url,
					new NetworkResponse(requestCode));
		} else {
			MyApplication.getAsyncHttpClient().get(url,
					new NetworkResponse(requestCode));
		}
	}

	/**
	 * 发送POST请求
	 * 
	 * @param requestCode
	 * @param url
	 */
	public void post(int requestCode, String url) {
		if (!url.contains("http://")) {
			MyApplication.getAsyncHttpClient().post("http://" + url,
					new NetworkResponse(requestCode));
		} else {
			MyApplication.getAsyncHttpClient().post(url,
					new NetworkResponse(requestCode));
		}
	}

	/**
	 * 发送带参数的POST请求
	 * 
	 * @param requestCode
	 * @param url
	 * @param params
	 */
	public void post(int requestCode, String url, Map<String, String> params) {
		if (!url.contains("http://")) {
			MyApplication.getAsyncHttpClient()
					.post("http://" + url, new RequestParams(params),
							new NetworkResponse(requestCode));
		} else {
			MyApplication.getAsyncHttpClient()
					.post(url, new RequestParams(params),
							new NetworkResponse(requestCode));
		}
	}
	
	/**
	 * 网络请求开始
	 */
	public void onNetworkStart(){
		
	}

	/**
	 * 网络请求成功
	 * 
	 * @param requestCode
	 * @param jsonData
	 */
	public abstract void onNetworkSuccess(int requestCode, String jsonData);

	/**
	 * 网络请求完成
	 * 
	 * @param requestCode
	 */
	public abstract void onNetworkFinish(int requestCode);

	/**
	 * 网络请求失败
	 * 
	 * @param requestCode
	 * @param reason
	 */
	public void onNetworkFail(int requestCode, String reason) {
		Log.i("网络连接失败，请检查您的网络"+reason);
	}


	/**
	 * 
	 * @author Administrator
	 * 
	 */
	class NetworkResponse extends AsyncHttpResponseHandler {

		private int requestCode;

		/**
		 * 
		 * @param requestCode
		 */
		public NetworkResponse(int requestCode) {
			this.requestCode = requestCode;
		}

		@Override
		public void onFinish() {
			onNetworkFinish(requestCode);
			mProgressDialog.dismiss();
		}

		@Override
		public void onSuccess(String content) {
			onNetworkSuccess(requestCode, content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			onNetworkFail(requestCode, content);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			mProgressDialog.show();
			onNetworkStart();
		}

	}
}