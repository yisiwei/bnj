package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bangninjia.app.MyApplication;
import com.bangninjia.app.activity.SpecialActivity;
import com.bangninjia.app.utils.ListUtils;

/**
 * ImagePagerAdapter
 * 
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

	private Context context;
	// private List<Integer> imageIdList;
	private List<String> imageIdList;

	private int size;
	private boolean isInfiniteLoop;

	public ImagePagerAdapter(Context context, List<String> imageIdList) {
		this.context = context;
		this.imageIdList = imageIdList;
		this.size = ListUtils.getSize(imageIdList);
		isInfiniteLoop = false;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils
				.getSize(imageIdList);
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			ImageView imageView = new ImageView(context);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// view = holder.imageView = new ImageView(context);
			view = imageView;
			holder.imageView = (ImageView) view;
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// holder.imageView.setImageResource(imageIdList.get(getPosition(position)));

		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getPosition(position) == 0) {
					context.startActivity(new Intent(context,
							SpecialActivity.class));
				}
			}
		});

		MyApplication.getImageLoader().displayImage(
				imageIdList.get(getPosition(position)), holder.imageView,
				MyApplication.getDisplayImageOptions());

		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
