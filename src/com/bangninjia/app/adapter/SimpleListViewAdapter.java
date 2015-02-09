package com.bangninjia.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.TimePickerText;

public class SimpleListViewAdapter extends BaseAdapter {

	private Context context;
	private List<String> data;
	//private int currrentItem = 0;
	//private boolean[] checked;
	private List<TimePickerText> beans;

	public SimpleListViewAdapter(Context context, List<String> data) {
		this.context = context;
		this.data = data;

	}

	public void setData(List<String> mdata, int position) {
		this.data = mdata;

		beans = new ArrayList<TimePickerText>();

		for (int i = 0; i < mdata.size(); i++) {
			TimePickerText pickerText = new TimePickerText();
			if (i == position) {
				pickerText.setChecked(true);
			} else {
				pickerText.setChecked(false);
			}
			pickerText.setText(mdata.get(i));
			beans.add(pickerText);
		}

	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_simple_listview, parent, false);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView
					.findViewById(R.id.simple_listview_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(data.get(position));

		TextPaint tp = holder.tv.getPaint();
		if (beans != null && beans.get(position).isChecked()) {
			tp.setFakeBoldText(true);
		} else {
			tp.setFakeBoldText(false);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

}
