package com.bangninjia.app.utils;

import android.content.Context;
import android.content.res.Resources;

public class Toast {

	public static void show(Context context, String msg) {
		android.widget.Toast.makeText(context, msg,
				android.widget.Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, int resId)
			throws Resources.NotFoundException {
		android.widget.Toast.makeText(context,
				context.getResources().getText(resId),
				android.widget.Toast.LENGTH_SHORT).show();
	}
}
