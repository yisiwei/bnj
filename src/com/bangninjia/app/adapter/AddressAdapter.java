package com.bangninjia.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangninjia.R;
import com.bangninjia.app.model.Address;

public class AddressAdapter extends BaseAdapter {

	private List<Address> mAddresses;
	private Context mContext;
	private Address mAddress;

	public interface OnAddressEditListener {
		public void onEditClick(Address address);
	}

	public AddressAdapter(Context context, List<Address> addresses) {
		this.mContext = context;
		this.mAddresses = addresses;
	}

	@Override
	public int getCount() {
		return mAddresses.size();
	}

	@Override
	public Object getItem(int position) {
		return mAddresses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.address_item, null);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		mAddress = mAddresses.get(position);

		holder.mConsigneeTv.setText(mAddress.getName());
		holder.mPhoneTv.setText(mAddress.getMobile());
		String detailAddress = mAddress.getProvince() + mAddress.getCity()
				+ mAddress.getArea() + mAddress.getRoad()
				+ mAddress.getAddress();
		holder.mAddressDetailTv.setText(String.format(mContext.getResources()
				.getString(R.string.shipping_address), detailAddress, 1));

		return convertView;
	}

	private class ViewHolder {
		public TextView mConsigneeTv;
		public TextView mPhoneTv;
		public TextView mAddressDetailTv;

		ViewHolder(View view) {
			mConsigneeTv = (TextView) view.findViewById(R.id.address_consignee);
			mPhoneTv = (TextView) view.findViewById(R.id.address_phone);
			mAddressDetailTv = (TextView) view
					.findViewById(R.id.address_detail);
		}
	}

}
