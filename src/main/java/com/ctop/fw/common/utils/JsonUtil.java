package com.ctop.fw.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtil {
	
	private static Gson gson = new Gson();
	/**
	 * @title: 对象转换为json
	 * @author: YangHongyuan
	 * @date: 2015年3月7日
	 * @param obj
	 * @return
	 */
	public static JsonObject toJson(String jsonStr) {
        JsonParser parser = new JsonParser();  
        JsonObject json = parser.parse(jsonStr).getAsJsonObject();  

		return json;
	}

	/**
	 * @title: 转换对象为json字符串
	 * @author: YangHongyuan
	 * @date: 2015年3月7日
	 * @param obj
	 * @return
	 */
	public static String toJsonStr(Object obj) {
		String jsonStr = gson.toJson(obj);
		return jsonStr;
	}
	
	/**
	 * @title: 转换对象为json字符串
	 * @author: YangHongyuan
	 * @date: 2015年3月7日
	 * @param obj
	 * @return
	 */
	public static JsonObject toJson(Object obj) {
		String jsonStr = gson.toJson(obj);
		return toJson(jsonStr);
	}
	
	/**
	 * @title: 转换对象为json字符串
	 * @author: YangHongyuan
	 * @date: 2015年3月7日
	 * @param obj
	 * @return
	 */
	public static JsonArray toJsonArray(String arrayStr) {
        JsonParser parser = new JsonParser();  
        JsonArray array = parser.parse(arrayStr).getAsJsonArray();  
        return array;
	}

	/**
	 * @title: json串转对象
	 * @author: YangHongyuan
	 * @date: 2015年3月7日
	 * @param jsonStr
	 * @param 类
	 * @return 
	 * @description 
	 */
	public static <T> T toBean(String jsonStr, Class<T> cls) {
		JsonObject json = toJson(jsonStr);
		return toBean(json, cls);
	}

	/**
	 * @title:json转对象
	 * @author: YangHongyuan
	 * @date: 2015年3月18日
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T toBean(JsonObject json, Class<T> cls) {
        Gson gson = new Gson();  
        T obj = gson.fromJson(json, cls);
		return  obj;
	}

	/**
	 * @title: 获取list（默认取data节点）
	 * @author: YangHongyuan
	 * @date: 2015年3月7日
	 * @param jsonStr
	 * @param classNm
	 * @return
	 */
	public static  <T> List<T> getList(String jsonStr, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		JsonArray jsonArray = toJsonArray(jsonStr);  
		for(JsonElement el:jsonArray){
			T obj =  gson.fromJson(el, cls);  
			list.add(obj);
		}
		return list;
	}
	
	/**
	 * @title: 获取list
	 * @author: YangHongyuan
	 * @date: 2015年3月7日
	 * @param jsonStr 
	 * @param nodeNm 节点名
	 * @param class 类
	 * @return
	 */
	public static <T> List<T> getList(String jsonStr,String nodeNm,Class<T> cls) {
		List<T> list = new ArrayList<T>();
		JsonObject json = toJson(jsonStr);
		JsonArray jsonArray = json.getAsJsonArray(nodeNm);  
		for(JsonElement el:jsonArray){
			T obj =  gson.fromJson(el, cls);  
			list.add(obj);
		}
		return list;
	}

}
