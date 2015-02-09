package com.bangninjia.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.ExpectData;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.utils.StringUtils;
import com.bangninjia.app.utils.Toast;
import com.bangninjia.app.view.MyProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 敬请期待
 * 
 */
public class ExpectActivity extends BaseNetActivity {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5,
			checkBox6;

	private EditText mContent;

	private Button mSubmitBtn;

	private List<String> mList;

	private MyProgressDialog myProgressDialog;
	private static final int EXPECT_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expect);

		// title
		mTitleLeftBtn = (ImageButton) this.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) this.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) this.findViewById(R.id.title_right_btn);

		mTitleText.setText(R.string.expect);
		mTitleLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleRightBtn.setVisibility(View.GONE);

		checkBox1 = (CheckBox) this.findViewById(R.id.expect_1);
		checkBox2 = (CheckBox) this.findViewById(R.id.expect_2);
		checkBox3 = (CheckBox) this.findViewById(R.id.expect_3);

		checkBox4 = (CheckBox) this.findViewById(R.id.expect_4);
		checkBox5 = (CheckBox) this.findViewById(R.id.expect_5);
		checkBox6 = (CheckBox) this.findViewById(R.id.expect_6);

		mContent = (EditText) this.findViewById(R.id.expect_content);

		mSubmitBtn = (Button) this.findViewById(R.id.expect_submit_btn);

		mList = new ArrayList<String>();
		myProgressDialog = new MyProgressDialog(this, "正在提交");

		mSubmitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});

		checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mList.add("0");
				} else {
					mList.remove("0");
				}
			}
		});

		checkBox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mList.add("1");
				} else {
					mList.remove("1");
				}
			}
		});

		checkBox3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mList.add("2");
				} else {
					mList.remove("2");
				}
			}
		});

		checkBox4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mList.add("3");
				} else {
					mList.remove("3");
				}
			}
		});

		checkBox5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mList.add("4");
				} else {
					mList.remove("4");
				}
			}
		});

		checkBox6.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mList.add("5");
				} else {
					mList.remove("5");
				}
			}
		});

	}

	private void submit() {
		String content = mContent.getText().toString();
		if (StringUtils.isNullOrEmpty(content)) {
			Toast.show(this, "请填写您的意见");
			return;
		}

		if (mList.size() <= 0) {
			Toast.show(this, "请勾选您期待的功能");
			return;
		}
		String str = null;
		for (int i = 0; i < mList.size(); i++) {
			if (str == null) {
				str = mList.get(i);
			} else {
				str = str + "," + mList.get(i);
			}
		}

		ExpectData data = new ExpectData();
		data.setModular(str);
		data.setCentent(content);
		data.setRegsource("2");
		String dataString = MyApplication.getGson().toJson(data);
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", dataString);
		post(EXPECT_REQUEST_CODE, Constants.PLEASE_WAIT_URL, params);

	}

	@Override
	public void onNetworkStart() {
		super.onNetworkStart();
		myProgressDialog.show();
	}

	@Override
	public void onNetworkSuccess(int requestCode, String jsonData) {
		Log.i(jsonData);
		if (requestCode == EXPECT_REQUEST_CODE) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				String code = jsonObject.getString("code");
				String msg = jsonObject.getString("msg");
				Toast.show(this, msg);
				if (code.equals("1000")) {
					this.finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNetworkFinish(int requestCode) {
		myProgressDialog.dismiss();
	}

	@Override
	public void onNetworkFail(int requestCode, String reason) {
		super.onNetworkFail(requestCode, reason);
		Toast.show(this, "请检查您的网络");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ExpectActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ExpectActivity");
		MobclickAgent.onPause(this);
	}

}
