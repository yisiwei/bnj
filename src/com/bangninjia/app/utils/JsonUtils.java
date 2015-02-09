package com.bangninjia.app.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bangninjia.app.MyApplication;
import com.bangninjia.app.model.Brand;
import com.bangninjia.app.model.SkirtingBoard;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {

	public static List<Brand> parseJsonOfBrand(String json) {
		Log.i("MainActivity", "--jsonstr--" + json);
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray jsonArray = obj.getJSONArray("listBrand");
			List<Brand> list = MyApplication.getGson().fromJson(
					jsonArray.toString(), new TypeToken<List<Brand>>() {
					}.getType());
			Log.i("MainActivity", "--list--" + list);
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getPreImg(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String preImg = obj.getString("prefix");
			return preImg;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<SkirtingBoard> parseJsonOfSkirtingBoard(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray jsonArray = obj.getJSONArray("listSeries");
			List<SkirtingBoard> list = MyApplication.getGson().fromJson(
					jsonArray.toString(), new TypeToken<List<SkirtingBoard>>() {
					}.getType());
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
