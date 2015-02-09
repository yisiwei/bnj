package com.bangninjia.app.activity;

import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.School;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.DensityUtil;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 知识学堂 详情页
 * 
 */
public class SchoolDetailActivity extends Activity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private TextView mTitleTv;
	private TextView mDateTv;
	private TextView mContentTv;

	private School mSchool;
	private String mContent;

	private MyProgressDialog mProgressDialog;

	private int mWidth;

	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constants.DESCRIPTOR);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_detail);

		Intent intent = getIntent();
		mSchool = (School) intent.getSerializableExtra("School");
		Log.i("School===" + mSchool.toString());
		initView();

		mProgressDialog = new MyProgressDialog(this, "正在加载");

		initData();

		shareConfig();
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
		mTitleRightBtn.setText("分享");
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				share();
			}
		});

		mTitleTv = (TextView) this.findViewById(R.id.school_detail_title);
		mDateTv = (TextView) this.findViewById(R.id.school_detail_date);
		mContentTv = (TextView) this.findViewById(R.id.school_detail_content);

		// mContentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	/**
	 * 显示内容
	 */
	private void initData() {
		mTitleTv.setText(mSchool.getTitle());
		mDateTv.setText(mSchool.getCreateDate().substring(0,
				mSchool.getCreateDate().length() - 2));

		mContent = mSchool.getAppContent();
		Log.i("content=" + mContent);

		mWidth = DensityUtil.getWidth(this);
		new mAsyncTask().execute();
	}

	private class mAsyncTask extends AsyncTask<Void, Void, Spanned> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected Spanned doInBackground(Void... params) {
			Spanned spanned = null;
			if (mContent != null) {
				spanned = Html.fromHtml(mContent, imageGetter, null);
			} else {
				spanned = Html.fromHtml("无", imageGetter, null);
			}
			return spanned;
		}

		@Override
		protected void onPostExecute(Spanned result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			mContentTv.setText(result);
		}

	}

	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		@Override
		public Drawable getDrawable(String source) {
			Log.i("source=======" + source);
			Drawable drawable = null;
			try {
				URL url = new URL(source);
				drawable = Drawable.createFromStream(url.openStream(), "");
			} catch (Exception e) {
				e.printStackTrace();
				drawable = getResources().getDrawable(R.drawable.ic_launcher);
			}
			int height = (int) Math.ceil(mWidth / 1.4);
			Log.i("图片高度：" + height);
			drawable.setBounds(0, 0, mWidth, height);
			return drawable;
		};
	};

	/**
	 * 分享配置
	 */
	private void shareConfig() {
		// 配置需要分享的相关平台
		configPlatforms();
		// 设置分享的内容
		setShareContent();
	}

	/**
	 * 根据不同的平台设置不同的分享内容
	 */
	private void setShareContent() {
		// QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
		// "1103963999", "gB155ERC1RkCOQoO");
		// qZoneSsoHandler.addToSocialSDK();

		String title = mSchool.getTitle();
		String content = null;
		if (StringUtils.isNullOrEmpty(mSchool.getContentAbstract())) {
			content = "邦您家分享";
		} else {
			content = mSchool.getContentAbstract().replace("\n", "")
					.replace("\t", "").replace(" ", "");
			if (content.length() > 25) {
				content = content.substring(0, 25) + "...";
			}
		}
		String url = mSchool.getAppUrl();
		if (StringUtils.isNullOrEmpty(url)) {
			url = "http://www.bangninjia.com";
		}

		UMImage urlImage = new UMImage(this, mSchool.getMainImage());
		// UMImage urlImage = new UMImage(this, R.drawable.ic_launcher);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(content);
		qzone.setTargetUrl(url);
		qzone.setTitle(title);
		qzone.setShareMedia(urlImage);
		mController.setShareMedia(qzone);

		// 设置微信分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(content);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(url);
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle(title);
		circleMedia.setShareMedia(urlImage);
		circleMedia.setTargetUrl(url);
		mController.setShareMedia(circleMedia);

		// 新浪微博分享的内容
		SinaShareContent sinaShareContent = new SinaShareContent();
		sinaShareContent.setShareContent(content + url);
		sinaShareContent.setTitle(title);
		sinaShareContent.setShareMedia(urlImage);
		sinaShareContent.setTargetUrl(url);
		mController.setShareMedia(sinaShareContent);
	}

	/**
	 * 分享
	 */
	private void share() {
		// mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
		// SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
		// SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN,
		// SHARE_MEDIA.RENREN);

		mController.getConfig()
				.setPlatforms(SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
						SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);

		mController.openShare(this, false);
	}

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO授权
		// mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// 添加人人网SSO授权
		// RenrenSsoHandler renrenSsoHandler = new
		// RenrenSsoHandler(getActivity(),
		// "201874", "28401c0964f04a72a14c812d6132fcef",
		// "3bf66e42db1e4fa9829b955cc300b737");
		// mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1103963999";
		String appKey = "gB155ERC1RkCOQoO";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		// UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
		// appId, appKey);
		// qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		// qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		String appId = "wx53f73e1da673cc35";
		String appSecret = "72667bb62b18427d0530d8cbb69730f0";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		mController.registerListener(mSnsPostListener);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}

	SnsPostListener mSnsPostListener = new SnsPostListener() {

		@Override
		public void onStart() {

		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				Toast.show(getApplicationContext(), "分享成功");
			} else {
				Toast.show(getApplicationContext(), "分享失败 : error code : "
						+ stCode);
			}
		}
	};

	// private void textAndPicShare() {
	// mController.postShare(this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {
	// @Override
	// public void onStart() {
	// Toast.show(getApplicationContext(), "开始分享.");
	// }
	//
	// @Override
	// public void onComplete(SHARE_MEDIA platform, int eCode,
	// SocializeEntity entity) {
	// if (eCode == 200) {
	// Toast.show(getApplicationContext(), "分享成功.");
	// } else {
	// String eMsg = "";
	// if (eCode == -101) {
	// eMsg = "没有授权";
	// }
	// Toast.show(getApplicationContext(), "分享失败[" + eCode + "] "
	// + eMsg);
	// }
	// }
	// });
	//
	// mController.postShare(this, SHARE_MEDIA.WEIXIN_CIRCLE,
	// new SnsPostListener() {
	// @Override
	// public void onStart() {
	// Toast.show(getApplicationContext(), "开始分享.");
	// }
	//
	// @Override
	// public void onComplete(SHARE_MEDIA platform, int eCode,
	// SocializeEntity entity) {
	// if (eCode == 200) {
	// Toast.show(getApplicationContext(), "分享成功.");
	// } else {
	// String eMsg = "";
	// if (eCode == -101) {
	// eMsg = "没有授权";
	// }
	// Toast.show(getApplicationContext(), "分享失败[" + eCode
	// + "] " + eMsg);
	// }
	// }
	// });
	// }

}
