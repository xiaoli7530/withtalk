package com.ctop.fw.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;


public class StringUtil {
	public static String leftPad(String text, int size, char pad) {
		if (text == null) {
			text = "";
		}
		int length = text.length();
		if (length > size) {
			throw new RuntimeException("序列号已大于指定的长度!");
		} else if (length == size) {
			return text;
		} else {
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < size - length; i++) {
				b.append(pad);
			}
			b.append(text);
			return b.toString();
		}

	}

	public static boolean isEmpty(String text) {
		return text == null || "".equals(text);
	}

	public static boolean isNotEmpty(String text) {
		return text != null && !"".equals(text);
	}

	public static boolean equals(String text1, String text2) {
		return text1 != null && text1.equals(text2);
	}
	
	public static boolean equals(String text1, String... texts) {
		if(text1==null) {
			return false;
		}
		for(String s :texts) {
			if(text1.equals(s)){
				return true;
			}
		}
		return false;
	}
	
	public static String repeat(String text, int times) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < times; i++) {
			b.append(text);
		}
		return b.toString();
	}

	/** MD5编码 */
	public static String encodeMd5(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = encode(md.digest(resultString.getBytes()));
			else
				resultString = encode(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final char[] hexadecimal = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * Encodes the 128 bit (16 bytes) MD5 into a 32 character String.
	 *
	 * @param binaryData
	 *            Array containing the digest
	 *
	 * @return Encoded MD5, or null if encoding failed
	 */
	public static String encode(byte[] binaryData) {

		if (binaryData.length != 16)
			return null;

		char[] buffer = new char[32];

		for (int i = 0; i < 16; i++) {
			int low = binaryData[i] & 0x0f;
			int high = (binaryData[i] & 0xf0) >> 4;
			buffer[i * 2] = hexadecimal[high];
			buffer[i * 2 + 1] = hexadecimal[low];
		}

		return new String(buffer);
	}

	public static String convertUnderscoreNameToPropertyName(String name) {
		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (name != null && name.length() > 0) {
			if (name.length() > 1 && name.substring(1, 2).equals("_")) {
				result.append(name.substring(0, 1).toUpperCase());
			} else {
				result.append(name.substring(0, 1).toLowerCase());
			}
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals("_")) {
					nextIsUpper = true;
				} else {
					if (nextIsUpper) {
						result.append(s.toUpperCase());
						nextIsUpper = false;
					} else {
						result.append(s.toLowerCase());
					}
				}
			}
		}
		return result.toString();
	}

	private static char[] text36Chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z' };

	/** 36进制字符串转10进制 */
	public static long text36ToNum(String text36) {
		if (text36 == null) {
			return 0;
		}
		long num = 0;
		long unit = 1;
		int singleNum = 1;
		for (int i = text36.length() - 1; i >= 0; i--, unit = unit * 36) {
			char c = text36.charAt(i);
			if ((singleNum = Arrays.binarySearch(text36Chars, c)) == -1) {
				throw new RuntimeException("36进制中不支制的字符:" + c);
			}
			num += (singleNum * unit);
		}
		return num;
	}

	public static String text36Increment(String text36, int increment) {
		long num = text36ToNum(text36);
		num += increment;
		return numToText36(num);
	}

	/** 10进制转36进制字符串 */
	private static String numToText36(long num) {
		StringBuilder buff = new StringBuilder();
		do {
			long mod = num % 36;
			buff.insert(0, text36Chars[(int) mod]);
			num = num / 36;
			mod = num % 36;
		} while (num > 0);
		return buff.toString();
	}

	/**
	 * 将A1, A2这样的的位置转换成 [1, 2], [1, 2]这样的地址
	 * 
	 * @param location
	 * @return
	 */
	public static int[] convertExcelLocation(String location) {
		int baseNum = (int) ('Z' - 'A') + 1, x = 0, y = 0;
		char[] chars = location.toCharArray();
		boolean yDone = false;
		for(char c : chars) {
			if(c >= 'a' && c <= 'z') {
				if(yDone) {
					throw new IllegalArgumentException("无效的Excel单元格地址[" + location + "]");
				}
				y = y * baseNum + (c - 'a');
			} else if (c >= 'A' && c <= 'Z') {
				if(yDone) {
					throw new IllegalArgumentException("无效的Excel单元格地址[" + location + "]");
				}
				y = y * baseNum + (c - 'A');
			} else if (c >= '0' && c <= '9') {
				yDone = true;
				// rowIndex 从0开始，但是传入的是1开始，即A1 ＝ （0， 0）；
				x = x * 10 + (c - '0');
			} else {
				throw new IllegalArgumentException("无效的Excel单元格地址[" + location + "]");
			}
		}
		return new int[] { x - 1, y };
	}
	
	public static int indexOf(String text, String sub) {
		return text != null && sub != null ? text.indexOf(sub) : -1;
	}
	/***
	 * 构建in查询条件
	 * @param ids
	 * @return
	 */
	public static String buildQueryInWhere(Collection<String> ids){
		if(!ListUtil.isEmpty(ids)){
			StringBuilder sb = new StringBuilder();
			Set<String> unRepIds = new HashSet<>(ids);
			for(String id : unRepIds){
				if(StringUtils.isNotBlank(id)){
					sb.append("'").append(id).append("'").append(",");
				}
			}
			if(sb.length() > 0){
				return sb.substring(0, sb.length() -1);
			}
		}
		return "";
	}
	
	/**
	 * 根据浏览器版本编码现在文件名称；
	 * @param name
	 * @param userAgent
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeDownloadName(String name, String userAgent) throws UnsupportedEncodingException {
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			name = java.net.URLEncoder.encode(name, "UTF-8");    
            name = name.replace("+", "%20");//替换空格
		} else {
			byte[] bytes = name.getBytes("UTF-8"); // name.getBytes("UTF-8")处理safari的乱码问题
			name = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
		}
		return name;
	}
	
	public static boolean validateLicensePlate(String licensePlateNum) {
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}");
		Matcher matcher = pattern.matcher(licensePlateNum);
		return matcher.matches();
	}
	
	 /**
	   * 将第一个字母转换为大写
	   * @param s
	   * @return
	   */
	  public static String setFirstCharUpcase(String s){
	    if(s==null||s.length()<1) return s;
	    char[] c= s.toCharArray();
	    if(c.length>0){
	      if(c[0]>='a'&&c[0]<='z')c[0]=(char)((short)(c[0])-32);
	    }
	    return String.valueOf(c);
	  }
	  
	/**
	 * 首字母小写
	 * @param s
	 * @return
	 */
	public static String setFirstCharLowcase(String s){
	    if(s==null||s.length()<1) return s;
	    char[] c= s.toCharArray();
	    if(c.length>0){
	      if(c[0]>='A'&&c[0]<='Z')c[0]=(char)((short)(c[0])+32);
	    }
	    return String.valueOf(c);
	  }
	
	/**
	 * 根据列名创建java方法的field名称
	 * @param columnName
	 * @return
	 */
	public static String buildFieldName(String columnName){
		if(StringUtil.isEmpty(columnName)){
			return "";
		}
		columnName=columnName.toLowerCase();
		String fieldName="";				
		String[] words = columnName.split("[_\\.-]");
		for(int i=0;i<words.length;i++){
			if(i==0){
				fieldName=words[i];
			}
			else{
				fieldName+=setFirstCharUpcase(words[i]);
			}
		}
		return fieldName;
	}
	
	/**
	 * 得到唯一的32位的uuid
	 * @return
	 */
	public static String getJavaUuid(){
		return UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
	}
	
	
	/**
	 * 将类当中的属性名称转换为字段名称
	 * 例如：carType->car_type
	 * @param fieldName
	 * @return
	 */
	public static String convertToDbColumn(String fieldName ){
		if(isEmpty(fieldName)){
			return "";
		}
		StringBuilder strBuilder = new StringBuilder(fieldName.length()+10);
		char[] cs = fieldName.toCharArray();
		for(char c:cs) {
			if (c >= 'A' && c <= 'Z'){
				strBuilder.append("_");
			}
			strBuilder.append(c);
		}
		return  strBuilder.toString().toLowerCase();
	}
	
	/**
	 * 返回长度为【strLength】的随机字符串，在前面补0 
	 * 
	 * @param strLength
	 * @return
	 */
	public static String getFixLenthString(int strLength) {  
	      
	    String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < strLength; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();  
	} 
	
	public static String getUuid() {
		UUID uuid=UUID.randomUUID();
        String str = uuid.toString(); 
        String uuidStr=str.replace("-", "");
        return uuidStr;
	}
	
	/**
	 * 把字符串按字节长度截取
	 * @param str
	 * @param length
	 * @return
	 */
	public static String sbuByteString(String str, int length) {
		StringBuilder result = new StringBuilder();
		try {
			int bytelength = str.getBytes("UTF-8").length;
			int counter = 0;
			if(bytelength > length) {
				for(int i = 0; i < str.length(); i++) {
					String s = "" + str.charAt(i);
					counter += s.getBytes("UTF-8").length;
					if(counter > length) {
						break;
					}
					result.append(s);
				}
				
			} else {
				result.append(str);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	/**
	 * 若字符串为空则返回N/A
	 * @param text
	 * @return
	 */
	public static String getWithDefault(String text) {
		return StringUtil.getWithDefault(text, "N/A");
	}
	
	/**
	 * 若字符串为空则返一个默认值
	 * @param text			字符串
	 * @param defaultVal	默认值
	 * @return
	 */
	public static String getWithDefault(String text, String defaultVal) {
		if(StringUtil.isEmpty(text)) {
			return defaultVal;
		}
		return text;
	}
	
	/**
	 * 判断两个字符串的值是否相等，若两个字符串同时为空也认为相等
	 * @param text1
	 * @param text2
	 * @return
	 */
	public static boolean equalsIgnoreNull(String text1, String text2) {
		text1 = StringUtil.isEmpty(text1) ? "" : text1;
		text2 = StringUtil.isEmpty(text2) ? "" : text2;
		return text1.equals(text2);
	}
	
	public static String trim(String str) {
		if(StringUtil.isEmpty(str)) {
			return "";
		} else {
			return str.trim();
		}
	}
	
	/**
	 * 替换${param}
	 * @param resource
	 * @param params
	 * @return
	 */
	public static String formatByParam(String resource,Map<String, Object> params){
		String result = "";
		if(StringUtil.isNotEmpty(resource)){
			String regExp = "\\$\\{\\w+\\}";
			Matcher m = Pattern.compile(regExp).matcher(resource);			
			if(m.find()){				
				org.apache.commons.lang.text.StrSubstitutor strSubstitutor = new org.apache.commons.lang.text.StrSubstitutor(params);
				result = strSubstitutor.replace(resource);
				Matcher m2 = Pattern.compile(regExp).matcher(result);
				while(m2.find()) {
					String temp = m2.group();
					System.out.println(temp);
					//result = result.replace(temp, "");
				}
			}else{
				result = resource;
			}
		}		
		return result;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	
	/**
         * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("123","2000613");
		map.put("abc","2003223");
		
		Map<String, String> resultMap = sortMapByKey(map);
		StringBuilder sb = new StringBuilder(1000);
        for(String pname:resultMap.keySet()){
        	sb.append(pname).append("=").append(resultMap.get(pname)).append("&");
        }
        sb = sb.deleteCharAt(sb.length()-1);
		String sign = new String(sb);
		System.out.println(sign);
		System.out.println(encodeMd5(sign,"UTF-8"));
		
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("name","2000613");
		map2.put("title","2003223");
		
		//PPO Build-${name}退回审批任务，${title}
		String resouce = "PPO Build-${name}退回审批任务，${title}";
		System.out.println(StringUtil.formatByParam(resouce,map2));
	}
	
	
}

//比较器类

class MapKeyComparator implements Comparator<String>{
    @Override
    public int compare(String str1, String str2) {        
        return str1.compareTo(str2);
    }
}