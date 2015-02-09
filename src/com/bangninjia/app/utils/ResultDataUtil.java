package com.bangninjia.app.utils;

import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.ResultData;
import com.google.gson.reflect.TypeToken;

public class ResultDataUtil {

	public static ResultData parse(String json) {

		TypeToken<ResultData> typeToken = new TypeToken<ResultData>() {
		};
		ResultData data = MyApplication.getGson().fromJson(json,
				typeToken.getType());
		return data;

	}
}
