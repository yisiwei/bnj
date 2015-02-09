package com.bangninjia.app.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangninjia.R;

public class OrderTrackingAdapter extends BaseAdapter {

	private List<Map<String, String>> mList;
	private Context mContext;

	public OrderTrackingAdapter(Context context, List<Map<String, String>> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.order_tracking_item,
					null);
			holder = new ViewHolder();
			holder.mContent = (TextView) convertView
					.findViewById(R.id.order_tracks_item_cotent);
			holder.mDateTime = (TextView) convertView
					.findViewById(R.id.order_tracks_item_datetime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mContent.setText(mList.get(position).get("content"));
		holder.mDateTime.setText(mList.get(position).get("datetime"));

		return convertView;
	}

	private class ViewHolder {
		public TextView mContent;
		public TextView mDateTime;
	}

}
