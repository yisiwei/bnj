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
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.OnSkirtingBoardListener;
import com.bangninjia.app.model.SeriesProperty;
import com.bangninjia.app.model.SkirtingBoard;
import com.bangninjia.app.utils.Log;
import com.bangninjia.app.view.MyGridView;

public class SkirtingBoardAdapter extends BaseExpandableListAdapter {

	private List<SkirtingBoard> mSkirtingBoardList;
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private SkirtingBoard mSkirtingBoard;
	private SeriesProperty seriesProperty;
	
	private OnSkirtingBoardListener mOnSkirtingBoardListener;
	
	private String preImg;// 图片地址前缀

	public SkirtingBoardAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mSkirtingBoardList = new ArrayList<SkirtingBoard>();
		this.mOnSkirtingBoardListener = (OnSkirtingBoardListener) mContext;
	}

	public SeriesProperty getSeriesProperty() {
		return seriesProperty;
	}

	public void setPreImg(String preImg) {
		this.preImg = preImg;
	}

	@Override
	public int getGroupCount() {
		return mSkirtingBoardList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return mSkirtingBoardList.get(groupPosition).getSeriesProperties().size();
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mSkirtingBoardList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		//return mSkirtingBoardList.get(groupPosition).getSeriesProperties().get(childPosition);
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

	public void update(List<SkirtingBoard> data) {
		this.mSkirtingBoardList.clear();
		this.mSkirtingBoardList.addAll(data);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.floor_skirting_borad_item, parent,
					false);
			holder = new ViewHolder();
			holder.mSkirtingBoardNameText = (TextView) convertView
					.findViewById(R.id.skirting_board_name_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SkirtingBoard skirtingBoard = mSkirtingBoardList.get(groupPosition);
		holder.mSkirtingBoardNameText.setText(skirtingBoard.getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder = null;
		if (convertView == null) {
			childViewHolder = new ChildViewHolder();
			convertView = mInflater.inflate(R.layout.wall_brand_product,
					parent, false);
			childViewHolder.mProductsView = (MyGridView) convertView
					.findViewById(R.id.wall_brand_product_gridView);
			convertView.setTag(childViewHolder);
			childViewHolder.mProductsView.setTag(groupPosition);

		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		childViewHolder.mProductsView.setAdapter(new SkirtingBoardGridViewAdapter(
				mContext, mSkirtingBoardList.get(groupPosition).getSeriesProperties(), preImg));
		childViewHolder.mProductsView.setTag(groupPosition);
		childViewHolder.mProductsView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						int pos = (Integer) parent.getTag();
						mSkirtingBoard = mSkirtingBoardList.get(pos);
						Object item = parent.getAdapter().getItem(position);
						if (item != null) {
							seriesProperty = (SeriesProperty) item;
							//seriesProperty.setId(mSkirtingBoard.getId());
							mOnSkirtingBoardListener.onUserSelected(seriesProperty);
							mOnSkirtingBoardListener.onUserSelected(mSkirtingBoard);
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
		public TextView mSkirtingBoardNameText;
	}

	private class ChildViewHolder {
		public MyGridView mProductsView;
	}

}
