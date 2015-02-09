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
import com.bangninjia.app.model.Product;

public class FloorGridViewAdapter extends BaseAdapter {

	private List<Product> mProducts;
	private LayoutInflater mInflater;
	private String mPreImg;

	public FloorGridViewAdapter(Context context, List<Product> products,
			String preImg) {
		super();
		this.mProducts = products;
		this.mPreImg = preImg;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mProducts.size();
	}

	@Override
	public Object getItem(int position) {
		return mProducts.get(position);
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
			holder.mProductImg = (ImageView) convertView
					.findViewById(R.id.floor_brand_product_item_img);
			holder.mProductText = (TextView) convertView
					.findViewById(R.id.floor_brand_product_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mProductText.setText(mProducts.get(position).getName());
		MyApplication.getImageLoader().displayImage(
				mPreImg + mProducts.get(position).getImageList().get(0),
				holder.mProductImg);
		return convertView;
	}

	private class ViewHolder {
		ImageView mProductImg;
		TextView mProductText;
	}

}
