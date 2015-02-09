package com.bangninjia.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.utils.Log;

/**
 * 专题
 *
 */
public class SpecialActivity extends Activity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private WebView mWebView;
	private ProgressBar mBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special);

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.special);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mWebView = (WebView) this.findViewById(R.id.activity_webView);
		mBar = (ProgressBar) this.findViewById(R.id.activity_pb);

		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setWebViewClient(new android.webkit.WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 页面下载完毕,却不代表页面渲染完毕显示出来
				Log.i("onPageFinished");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 自身加载新链接,不做外部跳转
				view.loadUrl(url);
				return true;
			}

		});
		mWebView.setWebChromeClient(new WebViewClient());
		mWebView.loadUrl("http://wap.bangninjia.com/app_activity.shtml");

	}

	private class WebViewClient extends WebChromeClient {
		
		public WebViewClient() {
			
		}
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.i("newProgress:" + newProgress);
			mBar.setProgress(newProgress);

			if (newProgress == 0) {
				mBar.setVisibility(View.VISIBLE);
			}
			if (newProgress == 100) {
				mBar.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

}
