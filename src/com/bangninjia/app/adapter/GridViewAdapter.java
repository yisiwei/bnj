package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.Product;

/**
 * 此类是 ExpandableListView 中 Child 内部 GridView 的适配器
 * 
 * 
 */
public class GridViewAdapter extends BaseAdapter {

	private List<Product> mProducts;
	private LayoutInflater mInflater;
	private ViewHolder holder;

	public GridViewAdapter(Context context, List<Product> products) {
		super();
		this.mProducts = products;
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.wall_brand_product_item,
					parent, false);
			holder = new ViewHolder();
			holder.mProductText = (TextView) convertView
					.findViewById(R.id.wall_brand_product_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mProductText.setText(mProducts.get(position).getName());
		return convertView;
	}

	private class ViewHolder {
		TextView mProductText;
	}

}
