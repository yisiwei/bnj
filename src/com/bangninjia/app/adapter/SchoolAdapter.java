package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.School;

public class SchoolAdapter extends BaseAdapter {

	private Context mContext;
	private List<School> mList;

	public SchoolAdapter(Context context, List<School> list) {
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
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.school_item, null);
			holder.mImage = (ImageView) convertView
					.findViewById(R.id.school_item_image);
			holder.mTitle = (TextView) convertView
					.findViewById(R.id.school_item_title);
			holder.mContext = (TextView) convertView
					.findViewById(R.id.school_item_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		School school = mList.get(position);
		holder.mTitle.setText(school.getTitle());
		String content = school.getContentAbstract().replace("\n", "").replace("\t", "").replace(" ", "");
		if (content.length() > 25) {
			content = content.substring(0, 25) + "...";
		}
		holder.mContext.setText(content);
		
		MyApplication.getImageLoader().displayImage(school.getMainImage(),
				holder.mImage, MyApplication.getDisplayImageOptions());

		return convertView;
	}

	class ViewHolder {
		public ImageView mImage;
		public TextView mTitle;
		public TextView mContext;
	}

}
