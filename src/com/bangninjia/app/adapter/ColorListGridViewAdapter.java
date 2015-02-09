package com.bangninjia.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.ColorValue;

public class ColorListGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<ColorValue> mData;

	public ColorListGridViewAdapter(Context context, List<ColorValue> data) {
		this.mContext = context;
		this.mData = data;
		if (data == null) {
			this.mData = new ArrayList<ColorValue>();
		}
	}

	public void setData(List<ColorValue> data) {
		if (data == null) {
			this.mData = new ArrayList<ColorValue>();
		} else {
			this.mData = data;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.wall_color_gridview_item, parent, false);
			holder = new ViewHolder();
			holder.colorName = (TextView) convertView
					.findViewById(R.id.wall_color_colorName);
			holder.modelNum = (TextView) convertView
					.findViewById(R.id.wall_color_modelNum);
			holder.itemLayout = (LinearLayout) convertView
					.findViewById(R.id.wall_color_item_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.colorName.setText(mData.get(position).getColorName());
		holder.modelNum.setText(mData.get(position).getModelNum());
		holder.itemLayout.setBackgroundColor(Color.parseColor(mData.get(
				position).getColorNum()));

		return convertView;
	}

	class ViewHolder {
		TextView colorName;
		TextView modelNum;
		LinearLayout itemLayout;
	}

}
