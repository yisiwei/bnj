package com.bangninjia.app.adapter;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.DemandProperties;
import com.bangninjia.app.model.Order;
import com.bangninjia.app.utils.Constants;
import com.bangninjia.app.utils.DateUtil;
import com.bangninjia.app.utils.Log;

public class OrderAdapter extends BaseAdapter {

	private List<Order> mOrders;
	private Context mContext;
	private LayoutInflater mInflater;
	private OnOrderListener mOrderListener;
	
	private static Format mFormat = new DecimalFormat("0.00");

	public interface OnOrderListener {

		/**
		 * 查看详情
		 * 
		 * @param position
		 */
		public void onItemClick(int position);

		/**
		 * 取消订单
		 * 
		 * @param position
		 */
		public void onCancelClick(int position);

		/**
		 * 修改订单
		 * 
		 * @param position
		 */
		public void onUpdateClick(int position);

		/**
		 * 支付
		 * 
		 * @param position
		 */
		public void onPayClick(int position);

		/**
		 * 删除
		 * 
		 * @param position
		 */
		public void onDeleteClick(int position);

		/**
		 * 评价
		 * 
		 * @param position
		 */
		public void onCommentClick(int position);

		/**
		 * 订单确认
		 * 
		 * @param position
		 */
		public void onConfirmClick(int position);

		/**
		 * 订单跟踪
		 * 
		 * @param position
		 */
		public void onTrackingClick(int position);

		/**
		 * 订单查看
		 * 
		 * @param position
		 */
		public void onLookClick(int position);

	}

	public OrderAdapter(Context context, List<Order> orders,
			OnOrderListener onOrderListener) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mOrders = orders;
		this.mOrderListener = onOrderListener;
	}

	@Override
	public int getCount() {
		return mOrders.size();
	}

	@Override
	public Object getItem(int position) {
		return mOrders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.order_item, parent, false);

			holder.mIcon = (ImageView) convertView
					.findViewById(R.id.order_type_icon);

			holder.mServiceNameView = (TextView) convertView
					.findViewById(R.id.order_service_name);
			holder.mAppointmentDateView = (TextView) convertView
					.findViewById(R.id.order_appointmentDate);
			holder.mOrderIdView = (TextView) convertView
					.findViewById(R.id.order_id);

			holder.mOrderStatusView = (TextView) convertView
					.findViewById(R.id.order_status);
			holder.mOrderImg = (ImageView) convertView
					.findViewById(R.id.order_img);
			holder.mServiceTypeView = (TextView) convertView
					.findViewById(R.id.order_service_type);

			holder.mPropertiesView = (TextView) convertView
					.findViewById(R.id.order_properties);
			holder.mPriceView = (TextView) convertView
					.findViewById(R.id.order_price);
			holder.mOrderCancelBtn = (Button) convertView
					.findViewById(R.id.order_cancel_btn);

			holder.mOrderUpdateBtn = (Button) convertView
					.findViewById(R.id.order_update_btn);
			holder.mOrderPaymentBtn = (Button) convertView
					.findViewById(R.id.order_payment_btn);
			holder.mOrderDeleteBtn = (Button) convertView
					.findViewById(R.id.order_delete_btn);

			holder.mOrderCommentBtn = (Button) convertView
					.findViewById(R.id.order_comment_btn);
			holder.mOrderTrackingBtn = (Button) convertView
					.findViewById(R.id.order_tracking_btn);
			holder.mOrderConfirmBtn = (Button) convertView
					.findViewById(R.id.order_confirm_btn);
			holder.mOrderLookBtn = (Button) convertView
					.findViewById(R.id.order_look_btn);
			holder.mItemLayout = (LinearLayout) convertView
					.findViewById(R.id.order_item_layout);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Order order = mOrders.get(position);

		String serviceName = order.getServiceName();
		holder.mServiceNameView.setText(serviceName);
		holder.mAppointmentDateView.setText(DateUtil.parseLongDate(Long
				.valueOf(order.getCreateDate())));
		holder.mOrderIdView.setText(order.getOrderId().toString());

		holder.mOrderStatusView.setText(Constants.orderStatus(order
				.getOrderStatus()));// 订单状态

		holder.mOrderCancelBtn.setVisibility(View.GONE);
		holder.mOrderUpdateBtn.setVisibility(View.GONE);
		holder.mOrderPaymentBtn.setVisibility(View.GONE);
		holder.mOrderDeleteBtn.setVisibility(View.GONE);
		holder.mOrderCommentBtn.setVisibility(View.GONE);
		holder.mOrderTrackingBtn.setVisibility(View.GONE);
		holder.mOrderLookBtn.setVisibility(View.GONE);
		holder.mOrderConfirmBtn.setVisibility(View.GONE);

		int comeFrom = Integer.valueOf(MyApplication.getComeFrom());
		switch (order.getOrderStatus()) {
		case 100:// 待付款
			switch (comeFrom) {
			case 1:
				holder.mOrderCancelBtn.setVisibility(View.VISIBLE);
				holder.mOrderPaymentBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 101:// 待派单
			switch (comeFrom) {
			case 1:
				holder.mOrderCancelBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 102:// 已派单
			switch (comeFrom) {
			case 1:
				holder.mOrderCancelBtn.setVisibility(View.VISIBLE);
				holder.mOrderUpdateBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 103:// 用户已确认
			switch (comeFrom) {
			case 1:
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderConfirmBtn.setVisibility(View.VISIBLE);
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 104:// 作业人员已确认
			switch (comeFrom) {
			case 1:
				holder.mOrderConfirmBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 105:// 施工完成
			switch (comeFrom) {
			case 1:
				holder.mOrderCommentBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 106:// 已评价
			switch (comeFrom) {
			case 1:
				holder.mOrderDeleteBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 200:// 申请取消
			switch (comeFrom) {
			case 1:
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 201:// 已取消
			switch (comeFrom) {
			case 1:
				holder.mOrderDeleteBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 202:// 取消失败
			switch (comeFrom) {
			case 1:
				holder.mOrderCancelBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			
			break;
		case 203:// 派单失败
			switch (comeFrom) {
			case 1:
				holder.mOrderCancelBtn.setVisibility(View.VISIBLE);
				holder.mOrderTrackingBtn.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.mOrderLookBtn.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		// if (order.getOrderItems() != null && order.getOrderItems().size() >
		// 0) {
		//
		// for (int i = 0; i < order.getOrderItems().size(); i++) {
		// if (order.getOrderItems().get(i).getType() == 1) {
		// if (order.getOrderItems().get(i).getSmallPic() != null) {
		// MyApplication.getImageLoader().displayImage(
		// order.getOrderItems().get(i).getSmallPic(),
		// holder.mOrderImg,
		// MyApplication.getDisplayImageOptions());
		// // Log.i("图片URL："+
		// // order.getOrderItems().get(i).getSmallPic());
		// } else {
		// holder.mOrderImg
		// .setImageResource(R.drawable.ic_launcher);
		// }
		//
		// break;
		// }
		// }
		// }

		MyApplication.getImageLoader().displayImage(order.getProductImages(),
				holder.mOrderImg, MyApplication.getDisplayImageOptions());

		String propertites = order.getOrderProperties();
		DemandProperties demandProperties = MyApplication.getGson().fromJson(
				propertites, DemandProperties.class);
		Log.i("MainActivity", "demandProperties==" + demandProperties);

		String serviceType = demandProperties.get服务类型();
		if ("地板更新".equals(serviceName)) {
			holder.mIcon.setImageResource(R.drawable.floor_icon);
		} else if ("墙面更新".equals(serviceName)) {
			holder.mIcon.setImageResource(R.drawable.wall_icon);
			serviceType = serviceType + "【" + demandProperties.get现状信息() + "】";
		} else {
			holder.mIcon.setImageResource(R.drawable.wallpaper_icon);
			serviceType = serviceType + "【" + demandProperties.get现状信息() + "】";
		}
		holder.mServiceTypeView.setText(serviceType);

		String property = "";
		if ("有电梯".equals(demandProperties.get电梯信息())) {
			property = "【" + order.getDemandSpace() + "平方米+"
					+ demandProperties.get电梯信息() + "】";
		} else {
			property = "【" + order.getDemandSpace() + "平方米+"
					+ demandProperties.get电梯信息() + "+"
					+ demandProperties.get楼层信息() + "】";
		}
		holder.mPropertiesView.setText(property);
		holder.mPriceView.setText("￥" + mFormat.format(order.getRealPay()));

		final int mPosition = position;

		// 查看详情
		holder.mItemLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onItemClick(mPosition);
			}
		});

		// 取消
		holder.mOrderCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onCancelClick(mPosition);
			}
		});

		// 付款
		holder.mOrderPaymentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onPayClick(mPosition);
			}
		});
		// 删除
		holder.mOrderDeleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onDeleteClick(mPosition);
			}
		});
		// 修改
		holder.mOrderUpdateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onUpdateClick(mPosition);
			}
		});
		// 评价
		holder.mOrderCommentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onCommentClick(mPosition);
			}
		});

		// 订单查看
		holder.mOrderLookBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onLookClick(mPosition);
			}
		});

		// 订单跟踪
		holder.mOrderTrackingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onTrackingClick(mPosition);
			}
		});

		// 订单确认
		holder.mOrderConfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOrderListener.onConfirmClick(mPosition);
			}
		});

		return convertView;
	}

	private class ViewHolder {
		private ImageView mIcon;

		public TextView mServiceNameView;
		public TextView mAppointmentDateView;
		public TextView mOrderIdView;

		public TextView mOrderStatusView;
		public ImageView mOrderImg;
		public TextView mServiceTypeView;

		public TextView mPropertiesView;
		public TextView mPriceView;
		public Button mOrderCancelBtn;

		public Button mOrderUpdateBtn;
		public Button mOrderPaymentBtn;
		public Button mOrderDeleteBtn;

		public Button mOrderCommentBtn;
		public Button mOrderTrackingBtn;
		public Button mOrderConfirmBtn;

		public Button mOrderLookBtn;
		public LinearLayout mItemLayout;
	}

}
