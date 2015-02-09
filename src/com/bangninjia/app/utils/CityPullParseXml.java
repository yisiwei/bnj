package com.bangninjia.app.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.bangninjia.app.model.City;
import com.bangninjia.app.model.Road;

public class CityPullParseXml {

	public CityPullParseXml() {

	}

	public static List<City> getCities(Context context, String province)
			throws Exception {
		InputStream cityXml = null;

		if ("北京市".equals(province)) {
			cityXml = context.getResources().getAssets()
					.open("city_beijing.xml");
		}

		List<City> cities = null;
		List<String> areaList = null;
		City city = null;

		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(cityXml, "UTF-8");
		int event = pullParser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				cities = new ArrayList<City>();
				break;
			case XmlPullParser.START_TAG:
				if ("city".equals(pullParser.getName())) {
					String cityName = pullParser.getAttributeValue(0);
					city = new City();
					city.setCityName(cityName);
					areaList = new ArrayList<String>();
				}
				if ("area".equals(pullParser.getName())) {
					// String id = pullParser.getAttributeValue(0);
					String county = pullParser.nextText();
					areaList.add(county);
				}
				break;
			case XmlPullParser.END_TAG:
				if ("city".equals(pullParser.getName())) {
					city.setAreaList(areaList);
					cities.add(city);
					city = null;
					areaList = null;
				}
				break;
			}
			event = pullParser.next();
		}

		return cities;
	}

	public static List<Road> getRoads(Context context, String city)
			throws Exception {
		InputStream cityXml = null;
		if ("北京市".equals(city)) {
			cityXml = context.getResources().getAssets()
					.open("road_beijing.xml");
		}

		List<Road> roads = null;
		List<String> roadList = null;
		Road road = null;

		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(cityXml, "UTF-8");
		int event = pullParser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				roads = new ArrayList<Road>();
				break;
			case XmlPullParser.START_TAG:
				if ("area".equals(pullParser.getName())) {
					String areaName = pullParser.getAttributeValue(0);
					// String areaName = pullParser.nextText();
					road = new Road();
					road.setAreaName(areaName);
					roadList = new ArrayList<String>();
				}
				if ("road".equals(pullParser.getName())) {
					String roadName = pullParser.nextText();
					roadList.add(roadName);
				}
				break;
			case XmlPullParser.END_TAG:
				if ("area".equals(pullParser.getName())) {
					road.setRoadList(roadList);
					roads.add(road);
					road = null;
					roadList = null;
				}
				break;
			}
			event = pullParser.next();
		}

		return roads;
	}
}
