package com.bangninjia.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.Brand;
import com.bangninjia.app.model.OnSelectedListener;
import com.bangninjia.app.model.Product;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.view.MyGridView;

public class FloorListAdapter extends BaseExpandableListAdapter {

	private List<Brand> mBrandList;
	private Context mContext;
	private LayoutInflater mInflater;
	private Brand mBrand;
	private Product mProduct;
	private OnSelectedListener mOnSelectedListener;
	private String preImg;// 图片地址前缀

	public FloorListAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mBrandList = new ArrayList<Brand>();
		this.mOnSelectedListener = (OnSelectedListener) mContext;
	}

	public Product getmProduct() {
		return mProduct;
	}

	public void setPreImg(String preImg) {
		this.preImg = preImg;
	}

	@Override
	public int getGroupCount() {
		return mBrandList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mBrandList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public void update(List<Brand> data) {
		this.mBrandList.clear();
		this.mBrandList.addAll(data);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.wall_brand_item, parent,
					false);
			holder = new ViewHolder();
			holder.mBrandImg = (ImageView) convertView
					.findViewById(R.id.wall_brand_item_img);
			holder.mBrandText = (TextView) convertView
					.findViewById(R.id.wall_brand_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Brand brand = mBrandList.get(groupPosition);
		holder.mBrandText.setText(brand.getChName());
		MyApplication.getImageLoader().displayImage(brand.getPicLogo(),
				holder.mBrandImg, MyApplication.getDisplayImageOptions());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder = null;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.wall_brand_product,
					parent, false);
			childViewHolder = new ChildViewHolder();
			childViewHolder.mProductsView = (MyGridView) convertView
					.findViewById(R.id.wall_brand_product_gridView);
			convertView.setTag(childViewHolder);
			childViewHolder.mProductsView.setTag(groupPosition);

		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		childViewHolder.mProductsView.setAdapter(new FloorGridViewAdapter(
				mContext, mBrandList.get(groupPosition).getProducts(), preImg));
		childViewHolder.mProductsView.setTag(groupPosition);
		childViewHolder.mProductsView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						int pos = (Integer) parent.getTag();
						mBrand = mBrandList.get(pos);
						Object item = parent.getAdapter().getItem(position);
						if (item != null) {
							mProduct = (Product) item;
							mProduct.setBrandId(mBrand.getId());
							mOnSelectedListener.onUserSelected(mProduct);
							mOnSelectedListener.onUserSelected(mBrand);
							Log.i("MainActivity", "--" + item + position
									+ "___" + id);
						}
					}
				});

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private class ViewHolder {
		public ImageView mBrandImg;
		public TextView mBrandText;
	}

	private class ChildViewHolder {
		public MyGridView mProductsView;
	}

}
