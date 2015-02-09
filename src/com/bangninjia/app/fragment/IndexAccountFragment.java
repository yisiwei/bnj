package com.bangninjia.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.activity.AddressManageActivity;
import com.bangninjia.app.activity.CommentListActivity;
import com.bangninjia.app.activity.LoginActivity;
import com.bangninjia.app.activity.MainActivity;
import com.bangninjia.app.activity.MoreSettingActivity;
import com.bangninjia.app.activity.RegisterActivity;
import com.bangninjia.app.activity.UpdatePwdActivity;
import com.bangninjia.app.model.User;
import com.bangninjia.app.utils.PreferencesUtils;
import com.bangninjia.app.utils.Toast;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的
 * 
 */
public class IndexAccountFragment extends Fragment implements OnClickListener {

	// title
	private ImageButton mTitleLeftBtn;
	private TextView mTitleText;
	private Button mTitleRightBtn;

	private ImageButton mLoginBtn, mRegisterBtn;

	private TextView mUsernameView;
	private TextView mOrderCountView;
	private TextView mExitView;// 退出登录

	private TextView mMyOrderView, mServiceCommentView, mServiceAddressView,
			mUpdatePasswordView, mMoreSettingView;

	private LinearLayout mUnloginLayout;// 未登录布局
	private RelativeLayout mLoginLayout;// 已登录布局

	private User mUser;
	private static final int LOGIN_REQUEST_CODE = 1001;
	private static final int GO_ORDER_REQUEST_CODE = 1002;
	private static final int GO_COMMENT_REQUEST_CODE = 1003;
	private static final int GO_MODIFY_PWD_REQUEST_CODE = 1004;
	private static final int GO_ADDRESS_REQUEST_CODE = 1005;

	private static final int BACK_ADDRESS_REQUEST_CODE = 3000;// 修改密码回传requestCode

	private static final int COMMENT_REQUEST_CODE = 2001;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index_account,
				container, false);

		initView(view);

		return view;
	}

	/**
	 * 初始化
	 * 
	 * @param view
	 */
	private void initView(View view) {

		// title
		mTitleLeftBtn = (ImageButton) view.findViewById(R.id.title_left_btn);
		mTitleText = (TextView) view.findViewById(R.id.title_text);
		mTitleRightBtn = (Button) view.findViewById(R.id.title_right_btn);

		mTitleLeftBtn.setVisibility(View.GONE);
		mTitleText.setText(R.string.account);
		mTitleRightBtn.setVisibility(View.GONE);

		mUnloginLayout = (LinearLayout) view
				.findViewById(R.id.acc_unloginLayout);
		mLoginLayout = (RelativeLayout) view.findViewById(R.id.acc_loginLayout);

		mLoginBtn = (ImageButton) view.findViewById(R.id.acc_login_btn);
		mRegisterBtn = (ImageButton) view.findViewById(R.id.acc_register_btn);

		mUsernameView = (TextView) view.findViewById(R.id.acc_username);
		mOrderCountView = (TextView) view.findViewById(R.id.acc_order_count);
		mExitView = (TextView) view.findViewById(R.id.acc_exit);

		mMyOrderView = (TextView) view.findViewById(R.id.acc_myOrder);
		mServiceCommentView = (TextView) view
				.findViewById(R.id.acc_service_comment);
		mServiceAddressView = (TextView) view
				.findViewById(R.id.acc_service_address);
		mUpdatePasswordView = (TextView) view
				.findViewById(R.id.acc_update_password);
		mMoreSettingView = (TextView) view.findViewById(R.id.acc_more_setting);

		mLoginBtn.setOnClickListener(this);
		mRegisterBtn.setOnClickListener(this);

		mExitView.setOnClickListener(this);

		mMyOrderView.setOnClickListener(this);
		mServiceCommentView.setOnClickListener(this);
		mServiceAddressView.setOnClickListener(this);
		mUpdatePasswordView.setOnClickListener(this);
		mMoreSettingView.setOnClickListener(this);

	}

	/**
	 * 判断是否已登录，显示不同界面
	 */
	private void isLoginShowView() {
		mUser = MyApplication.getUser();
		if (mUser == null) {
			mLoginLayout.setVisibility(View.GONE);
			mUnloginLayout.setVisibility(View.VISIBLE);
			mServiceCommentView.setVisibility(View.VISIBLE);
			mServiceAddressView.setVisibility(View.VISIBLE);
		} else {// 已登录
			mLoginLayout.setVisibility(View.VISIBLE);
			mUnloginLayout.setVisibility(View.GONE);
			mUsernameView.setText(mUser.getUserName());
			if ("1".equals(MyApplication.getComeFrom())) {
				mOrderCountView.setText(String.format(
						getResources()
								.getString(R.string.no_sumbit_order_count), 0,
						1));
			} else if ("2".equals(MyApplication.getComeFrom())) {// 商家
				mServiceCommentView.setVisibility(View.GONE);
				mServiceAddressView.setVisibility(View.GONE);
				mOrderCountView.setText("您有0个未查看订单");
			} else {// 作业人员
				mServiceCommentView.setVisibility(View.GONE);
				mServiceAddressView.setVisibility(View.GONE);
				mOrderCountView.setText("您有0个未确认订单");
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		isLoginShowView();
		if (requestCode == GO_ORDER_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				goOrder();
			}
		} else if (requestCode == GO_COMMENT_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				goComment();
			}
		} else if (requestCode == GO_MODIFY_PWD_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				goUpdatePwd();
			}
		} else if (requestCode == GO_ADDRESS_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				goServiceAddress();
			}
		} else if (requestCode == COMMENT_REQUEST_CODE) {// 服务评价回传
			if (resultCode == Activity.RESULT_OK) {
				((MainActivity) getActivity()).setTab(2);
			}
		} else if (requestCode == BACK_ADDRESS_REQUEST_CODE) {// 修改密码回传
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(intent, LOGIN_REQUEST_CODE);
			}
		}
	}

	/**
	 * 跳转到我的订单
	 */
	private void goOrder() {
		((MainActivity) getActivity()).setTab(2);
	}

	/**
	 * 跳转到服务评价
	 */
	private void goComment() {
		startActivityForResult(new Intent(getActivity(),
				CommentListActivity.class), COMMENT_REQUEST_CODE);
	}

	/**
	 * 跳转到修改密码
	 */
	private void goUpdatePwd() {
		startActivityForResult(new Intent(getActivity(),
				UpdatePwdActivity.class), BACK_ADDRESS_REQUEST_CODE);
	}

	/**
	 * 跳转到服务地址
	 */
	private void goServiceAddress() {
		startActivity(new Intent(getActivity(), AddressManageActivity.class));
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.acc_login_btn:// 登录
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(intent, LOGIN_REQUEST_CODE);
			break;
		case R.id.acc_register_btn:// 注册
			intent = new Intent(getActivity(), RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.acc_exit:// 退出
			MyApplication.setUser(null);
			PreferencesUtils.putString(getActivity(), "userId", null);
			PreferencesUtils.putString(getActivity(), "userName", null);
			PreferencesUtils.putString(getActivity(), "comeFrom", null);
			isLoginShowView();
			break;
		case R.id.acc_myOrder:// 我的订单
			if (mUser == null) {
				Toast.show(getActivity(), "请先登录");
				startActivityForResult(new Intent(getActivity(),
						LoginActivity.class), GO_ORDER_REQUEST_CODE);
				return;
			}
			goOrder();
			break;
		case R.id.acc_service_comment:// 服务评价
			if (mUser == null) {
				Toast.show(getActivity(), "请先登录");
				startActivityForResult(new Intent(getActivity(),
						LoginActivity.class), GO_COMMENT_REQUEST_CODE);
				return;
			}
			goComment();
			break;
		case R.id.acc_service_address:// 服务地址
			if (mUser == null) {
				Toast.show(getActivity(), "请先登录");
				startActivityForResult(new Intent(getActivity(),
						LoginActivity.class), GO_ADDRESS_REQUEST_CODE);
				return;
			}
			goServiceAddress();
			break;
		case R.id.acc_update_password:// 修改密码
			if (mUser == null) {
				Toast.show(getActivity(), "请先登录");
				startActivityForResult(new Intent(getActivity(),
						LoginActivity.class), GO_MODIFY_PWD_REQUEST_CODE);
				return;
			}
			goUpdatePwd();
			break;
		case R.id.acc_more_setting:
			startActivity(new Intent(getActivity(), MoreSettingActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("IndexAccountFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		isLoginShowView();
		MobclickAgent.onPageStart("IndexAccountFragment");
	}
}
