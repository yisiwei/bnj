package com.bangninjia.app.activity;

import java.net.URL;

import com.bangninjia.R;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.view.MyProgressDialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class TestActivity extends Activity {

	private MyProgressDialog mProgressDialog;
	private TextView textView;
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		mProgressDialog = new MyProgressDialog(this, "正在加载");

		textView = (TextView) this.findViewById(R.id.test);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		text = "<p style='text-align:center;'><img src='http://pic.bangninjia.com/push/15/1/124ad46396ef47e190e829579a370263.jpg' title='5188751_141158999178_2.jpg' /></p><p><br /></p><p>　　有关壁纸变黄的处理问题，我们必须先搞清楚壁纸变黄的原因，一下3点原因有：<br /><br />　　第一：是因为在铺贴壁纸的时候，墙灰尚未干，所以，它和墙纸发生了反应导致而成。<br /><br />　　第二：是因为它纸质常年的受阳光的照射还有与空气接触而导致老化而致。<br /><br />　　第三，还有可能是因为烟熏。<br /><br />　　那么，我们下面就根据以上三种原因的分析来对它进行改变和处理。<br /><br />　　上面的3种原因介绍，对应的措施，有关第一和第二种原因介绍，我们都可以采取的办法就是用毛巾蘸漂白水擦拭，半小时后再用清水擦拭一遍即可。对于第三种原因呢?建议用户最好用软布蘸着稀释的烧碱擦洗干净。<br /><br /></p><p style='text-align:center;'><img src='http://pic.bangninjia.com/push/15/1/124ad46396ef47e190e829579a370263.jpg' title='5188751_141158999178_2.jpg' /></p>";

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
			Spanned spanned = Html.fromHtml(text, imageGetter, null);
			return spanned;
		}

		@Override
		protected void onPostExecute(Spanned result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			textView.setText(result);
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
			drawable.setBounds(0, 0, 600, 500);
			return drawable;
		};
	};
}
