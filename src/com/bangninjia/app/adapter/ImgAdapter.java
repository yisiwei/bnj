package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bangninjia.app.MyApplication;
import com.bangninjia.app.activity.SpecialActivity;

public class ImgAdapter extends BaseAdapter {

	private Context _context;
//	private List<Integer> imgList;
	private List<String> imgList;

	public ImgAdapter(Context context, List<String> imgList) {
		_context = context;
		this.imgList = imgList;
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(_context);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.imageView.setImageResource(imgList.get(position
//				% imgList.size()));
		
		if (position % imgList.size()==0) {
			//Log.i("进来了："+position % imgList.size());
			viewHolder.imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Toast.show(_context, "点击了");
					_context.startActivity(new Intent(_context, SpecialActivity.class));
				}
			});
		}
		

		MyApplication.getImageLoader().displayImage(imgList.get(position % imgList.size()),
				viewHolder.imageView, MyApplication.getDisplayImageOptions());
		
		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}
}
