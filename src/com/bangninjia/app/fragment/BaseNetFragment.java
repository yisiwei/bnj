package com.bangninjia.app.fragment;

import java.util.Map;

import org.loopj.android.http.AsyncHttpResponseHandler;
import org.loopj.android.http.RequestParams;

import android.app.Fragment;
import android.app.ProgressDialog;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;

public abstract class BaseNetFragment extends Fragment {
	
	ProgressDialog progresDlg = null;
	
	public ProgressDialog onLoadProgressDlg(String message) {
		ProgressDialog progresDlg = new ProgressDialog(getActivity());
		progresDlg.setIcon(R.drawable.ic_launcher);
		progresDlg.setTitle("帮您家");
		progresDlg.setMessage(message);
		progresDlg.show();
		
		return progresDlg;
	}
	
	public void dismissDialog(){
		progresDlg.dismiss();
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
		public void onStart() {
			super.onStart();
			onNetworkStart();
		}

		@Override
		public void onFinish() {
			onNetworkFinish(requestCode);
		}

		@Override
		public void onSuccess(String content) {
			onNetworkSuccess(requestCode, content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			onNetworkFail(requestCode, content);
		}
	}
}
