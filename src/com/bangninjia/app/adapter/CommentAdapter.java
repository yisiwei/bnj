package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.Comment;
import com.bangninjia.app.model.CommentResult;

public class CommentAdapter extends BaseAdapter {

	private List<CommentResult> mComments;
	private Context mContext;
	private LayoutInflater mInflater;
	private Comment mComment;

	public CommentAdapter(Context context, List<CommentResult> comments) {
		this.mContext = context;
		this.mComments = comments;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		return mComments.get(position);
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
			convertView = mInflater.inflate(R.layout.comment_item, parent,
					false);
			holder.mOrderNo = (TextView) convertView
					.findViewById(R.id.comment_order_no);
			holder.mServiceItems = (TextView) convertView
					.findViewById(R.id.comment_service_items);
			holder.mSatisfaction = (TextView) convertView
					.findViewById(R.id.comment_satisfaction);
			holder.mCommentContent = (TextView) convertView
					.findViewById(R.id.comment_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		mComment = mComments.get(position).getCommentList().get(0);
		holder.mOrderNo.setText(mComment.getOrderId().toString());
		Integer type = mComment.getType();
		String serviceItems = null;
		if (type == 0) {
			serviceItems = "墙面更新";
		} else if (type == 1) {
			serviceItems = "地板更新";
		} else {
			serviceItems = "壁纸更新";
		}
		holder.mServiceItems.setText(serviceItems);
		Integer grade = mComment.getCommentGrade();
		String satisfaction = null;
		if (grade == 0) {
			satisfaction = "非常满意";
		} else if (grade == 1) {
			satisfaction = "满意";
		} else {
			satisfaction = "不满意";
		}
		holder.mSatisfaction.setText(satisfaction);
		holder.mCommentContent.setText(mComment.getCommentContent());

		return convertView;
	}

	private class ViewHolder {
		public TextView mOrderNo;
		public TextView mServiceItems;
		public TextView mSatisfaction;
		public TextView mCommentContent;
	}

}
