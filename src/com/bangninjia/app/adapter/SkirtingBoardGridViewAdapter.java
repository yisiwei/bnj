package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.SeriesProperty;

public class SkirtingBoardGridViewAdapter extends BaseAdapter {

	private List<SeriesProperty> mSeriesProperties;
	private LayoutInflater mInflater;
	private String mPreImg;

	public SkirtingBoardGridViewAdapter(Context context,
			List<SeriesProperty> seriesProperties, String preImg) {
		super();
		this.mSeriesProperties = seriesProperties;
		this.mPreImg = preImg;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mSeriesProperties.size();
	}

	@Override
	public Object getItem(int position) {
		return mSeriesProperties.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.floor_brand_product_item,
					parent, false);
			holder = new ViewHolder();
			holder.mSkirtingBoardImg = (ImageView) convertView
					.findViewById(R.id.floor_brand_product_item_img);
			holder.mSkirtingBoardText = (TextView) convertView
					.findViewById(R.id.floor_brand_product_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mSkirtingBoardText.setText(mSeriesProperties.get(position)
				.getName());
		MyApplication.getImageLoader().displayImage(
				mPreImg + mSeriesProperties.get(position).getImages(),
				holder.mSkirtingBoardImg);
		return convertView;
	}

	private class ViewHolder {
		ImageView mSkirtingBoardImg;
		TextView mSkirtingBoardText;
	}

}
