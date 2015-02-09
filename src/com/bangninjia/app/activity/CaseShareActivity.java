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

public class CaseShareActivity extends Activity implements OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private Button mCaseWallBtn;
	private Button mCaseWallpaperBtn;
	private Button mCaseFloorBtn;

	private WebView mWebView;
	private ProgressBar mBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_case_share);

		initView();

		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setWebViewClient(new android.webkit.WebViewClient(){
			
			@Override  
            public void onPageFinished(WebView view, String url) {  
                    super.onPageFinished(view, url);  
                    //页面下载完毕,却不代表页面渲染完毕显示出来  
                    Log.i("onPageFinished");
            }  
            @Override  
            public boolean shouldOverrideUrlLoading(WebView view, String url) {  
                    //自身加载新链接,不做外部跳转  
                    view.loadUrl(url);  
                    return true;  
            }  
			
		});
		mWebView.setWebChromeClient(new WebViewClient());
		mWebView.loadUrl("http://wap.bangninjia.com/appCase_paint.shtml");
		
	}

	private class WebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.i("newProgress:"+newProgress);
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

	/**
	 * 初始化View
	 */
	private void initView() {

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.case_share);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		mCaseWallBtn = (Button) this.findViewById(R.id.case_wall_btn);
		mCaseWallpaperBtn = (Button) this.findViewById(R.id.case_wallpaper_btn);
		mCaseFloorBtn = (Button) this.findViewById(R.id.case_floor_btn);

		mCaseWallBtn.setOnClickListener(this);
		mCaseWallpaperBtn.setOnClickListener(this);
		mCaseFloorBtn.setOnClickListener(this);

		mWebView = (WebView) this.findViewById(R.id.case_share_webView);
		mBar = (ProgressBar) this.findViewById(R.id.case_share_pb);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.case_wall_btn:
			mCaseWallBtn.setTextColor(getResources().getColor(
					R.color.title_color));
			mCaseWallpaperBtn.setTextColor(getResources()
					.getColor(R.color.gray));
			mCaseFloorBtn.setTextColor(getResources().getColor(R.color.gray));
			
			mWebView.loadUrl("http://wap.bangninjia.com/appCase_paint.shtml");
			
			break;
		case R.id.case_wallpaper_btn:

			mCaseWallBtn.setTextColor(getResources().getColor(R.color.gray));
			mCaseWallpaperBtn.setTextColor(getResources().getColor(
					R.color.title_color));
			mCaseFloorBtn.setTextColor(getResources().getColor(R.color.gray));
			
			mWebView.loadUrl("http://wap.bangninjia.com/appCase_wallpaper.shtml");
			
			break;
		case R.id.case_floor_btn:

			mCaseWallBtn.setTextColor(getResources().getColor(R.color.gray));
			mCaseWallpaperBtn.setTextColor(getResources()
					.getColor(R.color.gray));
			mCaseFloorBtn.setTextColor(getResources().getColor(
					R.color.title_color));
			mWebView.loadUrl("http://wap.bangninjia.com/appCase_floor.shtml");
			
			break;
		default:
			break;
		}
	}

}
