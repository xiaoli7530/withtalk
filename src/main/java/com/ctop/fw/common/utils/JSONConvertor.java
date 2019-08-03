package com.ctop.fw.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONConvertor {
	
	/**
	 * 对象转成JSON字符串
	 * @param object
	 * @return
	 * @throws Exception
	 */
	static public String toJson(Object object) throws Exception {
		return new ObjectMapper().writeValueAsString(object);
	}

	/**
	 * JSON字符串转成对象
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	static public <T> T toObject(String json, Class<T> clazz) throws Exception {
		return new ObjectMapper().readValue(json, clazz);
	}
	
	/**
	 * JSON字符串转成对象
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	static public <T> List<T> toObjectList(String json, Class<T> clazz) throws Exception {
		List<T> resultList = new ArrayList<T>();
		ObjectMapper mapper = new ObjectMapper();
		List list = mapper.readValue(json, List.class);
		if(list != null && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {
				resultList.add(toObject(toJson(list.get(i)), clazz));
			}
		}
		
		return resultList;
	}

}
