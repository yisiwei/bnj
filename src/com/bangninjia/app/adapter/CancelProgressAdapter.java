package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.OrderLog;
import com.bangninjia.app.utils.DateUtil;

public class CancelProgressAdapter extends BaseAdapter {

	private List<OrderLog> mOrderLogs;
	private OrderLog mOrderLog;
	private Context mContext;

	public CancelProgressAdapter(Context context, List<OrderLog> orderLogs) {
		this.mContext = context;
		this.mOrderLogs = orderLogs;
	}

	@Override
	public int getCount() {
		return mOrderLogs.size();
	}

	@Override
	public Object getItem(int position) {
		return mOrderLogs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.cancel_progress_item,
					null);
			holder = new ViewHolder();
			holder.mDateTime = (TextView) convertView
					.findViewById(R.id.datetime);
			holder.mContent = (TextView) convertView.findViewById(R.id.content);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		mOrderLog = mOrderLogs.get(position);
		
		holder.mDateTime.setText(DateUtil.parseLongDate(mOrderLog.getCreateDate()));
		holder.mContent.setText(mOrderLog.getOperRemark());

		return convertView;
	}

	class ViewHolder {
		public TextView mDateTime;
		public TextView mContent;
	}

}
