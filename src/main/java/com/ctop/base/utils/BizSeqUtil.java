package com.ctop.base.utils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctop.base.entity.BaseBizSeq;
import com.ctop.base.entity.BaseBizSeqDetail;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;

public class BizSeqUtil {

	

	/**
	 * 校验配置的业务单据规则, 如果解析不了，则抛出Exception
	 * @param bizSeqRule 业务单据号的配置规则 
	 * gongjj
	 */
	public static void checkBizSeqRule(BaseBizSeq baseBizSeq)   {
		RuleDefinition.buildDefinition(baseBizSeq.getFullDefinition());
		if(baseBizSeq.getHolderDefinition() != null) {
			RuleDefinition.buildDefinition(baseBizSeq.getHolderDefinition());
		}
	}

	/**
	 * 根据业务单据号规则生成
	 * @param bizSeqRule 业务单据号的配置规则
	 * @param bizData 用来生成业务单据号的业务数据
	 * @return
	 * gongjj
	 */
	public static String buildBizSeqHolder(BaseBizSeq baseBizSeq, Object bizData) {
		String hd = baseBizSeq.getHolderDefinition();
		if(hd == null) {
			hd = baseBizSeq.getFullDefinition();
		}
		RuleDefinition definition = RuleDefinition.buildDefinition(hd);
		Map<String, Object> predfinedVarMap = buildPredefinedVarMap();
		return definition.buildBizSeq(bizData, predfinedVarMap);
	}
	
	/**
	 * 根据业务单据号规则生成
	 * @param bizSeqRule 业务单据号的配置规则
	 * @param bizData 用来生成业务单据号的业务数据
	 * @param bizSeqDetail 数据库生成的序列号
	 * @return
	 * gongjj
	 */
	public static String buildBizSeq(BaseBizSeq baseBizSeq, Object bizData, BaseBizSeqDetail bizSeqDetail)  {
		RuleDefinition definition = RuleDefinition.buildDefinition(baseBizSeq.getFullDefinition());
		Map<String, Object> predfinedVarMap = buildPredefinedVarMap(baseBizSeq, bizSeqDetail.getSequence());
		String bizSeq = definition.buildBizSeq(bizData, predfinedVarMap);
		checkGeneratedBizSeq(bizSeq, baseBizSeq);
		return bizSeq;
	}
	 
	
	/**"根据业务单据号规则生成"*/
	public static String[] buildBizSeq(BaseBizSeq bizSeqRule, Object bizData, BaseBizSeqDetail bizSeqDetail, int seqNum) {
		RuleDefinition definition = RuleDefinition.buildDefinition(bizSeqRule.getFullDefinition());
		Map<String, Object> predfinedVarMap = buildPredefinedVarMap( );
		String[] seqs = buildSeqs(bizSeqRule, bizSeqDetail, seqNum);
		String[] bizSeqs = new String[seqNum];
		for(int i=0; i<seqNum; i++) {
			predfinedVarMap.put("$seq", seqs[i]);
			bizSeqs[i] = definition.buildBizSeq(bizData, predfinedVarMap);
			checkGeneratedBizSeq(bizSeqs[i], bizSeqRule);
		}
		return bizSeqs;
	}
	
	private static void checkGeneratedBizSeq(String bizSeq, BaseBizSeq bizSeqRule) { 
		if(bizSeq.indexOf("{") != -1 || bizSeq.indexOf("}") != -1) {
			//生成的业务单据号{0}无效，存在未解析的变量
			throw new RuntimeException("生成的业务单据号" + bizSeq + "无效，存在未解析的变量");
		}
		Integer maxLength = bizSeqRule.getMaxLength();
		if(maxLength != null) {
			if(bizSeq.length() > maxLength) {
				//生成的业务单据号{0}超长，单据号规则定义最大单据长度{1}
				throw new RuntimeException("生成的业务单据号" + bizSeq + "超长，单据号规则定义最大单据长度" + maxLength);
			}
		}
	}
	 
	/**创建生成业务单据号需要的预定义参数MAP, 除了$seq*/
	private static Map<String, Object> buildPredefinedVarMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("$sysdate", new Date());
		return map;
	}
	
	/**创建生成业务单据号需要的预定义参数MAP*/
	private static  Map<String, Object> buildPredefinedVarMap(BaseBizSeq bizSeqRule, Long seq)  { 
		String str = seq.toString();
		int size = bizSeqRule.getSeqLength();
		String seqText = StringUtil.leftPad(str, size, '0');
		Map<String, Object> map = buildPredefinedVarMap();
		map.put("$seq", seqText);
		return map;
	}
	
	private static String[] buildSeqs(BaseBizSeq baseBizSeq, BaseBizSeqDetail bizSeqDetail, int num) {
		Long seq = bizSeqDetail.getSequence();
		int size = baseBizSeq.getSeqLength();
		String[] arr = new String[num];
		for(int i = arr.length - 1; i>=0; i--) {
			String str = String.valueOf(seq.longValue() - (arr.length - 1 - i));
			double pow = Math.pow(10, size);
			if(num >= pow) {
				throw new BusinessException("单据号生成失败，序号{0}超过单据号定义的序号长度{1}.", new Object[]{num, size});
			}
			arr[i] = StringUtil.leftPad(str, size, '0');
			
		}
		return arr;
	}
	
	
	
	/**解析配置的业务单据号规则，根据提供的业务参数，预定义参数生成业务单据号*/
	private static class RuleDefinition {
		private static final String VAR = "VAR";
		private static final String CONST = "CONST";
		
		private List<RuleSegment> segments = new ArrayList<RuleSegment>();
		
		public static RuleDefinition buildDefinition(String str) {
			if(null == str) {
				return null;
			}
			str = str.trim();
			if(str.isEmpty()) {
				return null;
			}
			RuleDefinition definition = new RuleDefinition();
			int length = str.length();
			int start = 0;
			
			String type = null;
			for(int i=0; i<length; i++) {
				char c = str.charAt(i);
				if(type == null)  {
					//开始处理一个新段时，根据起始的字符
					if(c == '{') {
						type = VAR;
					} else if(c == '}') {
						//无效的业务单据号规则定义。
						throw new RuntimeException("无效的业务单据号规则定义。");
					} else {
						type = CONST;
					}
					start = i;
					continue;
				}
				if(VAR == type) {
					if(c == '}') {
						String varDef = str.substring(start+1, i);
						definition.segments.add(RuleSegment.buildVarSegment(varDef));
						type = null;
						continue;
					} else if (c == '{') {
						//无效的业务单据号规则定义。
						throw new RuntimeException("无效的业务单据号规则定义。");
					}
					//正常处理，走下一个字节
				}
				if(CONST == type) {
					if(c == '}') {
						//无效的业务单据号规则定义。
						throw new RuntimeException("无效的业务单据号规则定义。");
					}
					if(c == '{') {
						String constDef = str.substring(start, i);
						definition.segments.add(RuleSegment.buildConstSegment(constDef));
						type = null;
						//需要回退一位， 用来判断下一段是什么类型
						i--;
						continue;
					} else if ( i == length - 1) {
						String constDef = str.substring(start, i + 1);
						definition.segments.add(RuleSegment.buildConstSegment(constDef));
						type = null;
					}
					//正常处理，走下一个字节
				}
				
			}
			return definition;
		}
		
		public String buildBizSeq(Object sdo) {
			StringBuilder builder = new StringBuilder();
			for(RuleSegment segment : this.segments) {
				builder.append(segment.getValue(sdo));
			}
			return builder.toString();
		}
		
		public String buildBizSeq(Object bizData, Map<String, Object> predfinedVarMap) {
			StringBuilder builder = new StringBuilder();
			for(RuleSegment segment : this.segments) {
				if(segment.isPredefinedVar()) {
					builder.append(segment.getValue(predfinedVarMap));	
				} else {
					builder.append(segment.getValue(bizData));	
				}
			}
			return builder.toString();
		}
	}
	
	
	public static abstract class RuleSegment {
		public abstract String getValue(Object obj);
		
		public boolean isPredefinedVar() {
			return false;
		}
		
		public static RuleSegment buildConstSegment(String str) {
			return new RuleConstSegment(str);
		}
		
		public static RuleSegment buildVarSegment(String str) {
			RuleVarSegment segment = new RuleVarSegment();
			String[] arr = str.split("\\s*,\\s*");
			segment.varName = arr[0].trim();
			if(arr.length > 1) {
				segment.formatType = arr[1].trim();
			}
			if(arr.length > 2) {
				segment.formatStyle = arr[2].trim();
			}
			if(arr.length > 3) {
				segment.varText = arr[3].trim();
			}
			return segment;
		}
	}
	
	public static class RuleConstSegment extends RuleSegment {
		private String value;
		public RuleConstSegment(String value){
			this.value = value;
		}
		public String getValue(Object obj) {
			return value;
		}
	}
	
	public static class RuleVarSegment extends RuleSegment {
		private String varName;
		private String formatType;
		private String formatStyle;
		private String varText;
		
		@Override
		public boolean isPredefinedVar() {
			return varName.startsWith("$");
		}
		
		public String getValue(Object obj) {
			String str = null;
			if("date".equalsIgnoreCase(this.formatType)) {
				Date date = getDate(obj);
				if(formatStyle == null) {
					formatStyle = "yyyyMMdd";
				}
				if(date != null) { 
					DateFormat format = new SimpleDateFormat(formatStyle);
					str = format.format(date);
				}
			} else {
				str = (String) getString(obj);
			}
			if(str == null) {
				str = "{" + varName + "}";
			}
			return str;
		}
		
		@SuppressWarnings("rawtypes")
		private Date getDate(Object obj) {
			if(obj instanceof Map) {
				return (Date) ((Map)obj).get(varName);
			} else {
				if(obj == null) {
					return null;
				}
				try {
					Field field = obj.getClass().getDeclaredField(varName);
					return (Date) field.get(obj);
				} catch(Exception ex) {
					throw new BusinessException("生成序列号时，读取变量" + varName + "值失败!");
				}
			}
		}
		
		@SuppressWarnings("rawtypes")
		private Object getString(Object obj) {
			if(obj instanceof Map) {
				Object temp = ((Map)obj).get(varName);
				if(temp != null){
					temp = temp.toString();
				}
				return (String) temp;
			} else {
				if(obj == null) {
					return null;
				}
				try {
					Field field = obj.getClass().getDeclaredField(varName);
					return field.get(obj);
				} catch(Exception ex) {
					throw new BusinessException("生成序列号时，读取变量" + varName + "值失败!");
				}
			}
		}

		public String getVarName() {
			return varName;
		}

		public void setVarName(String varName) {
			this.varName = varName;
		}

		public String getFormatType() {
			return formatType;
		}

		public void setFormatType(String formatType) {
			this.formatType = formatType;
		}

		public String getFormatStyle() {
			return formatStyle;
		}

		public void setFormatStyle(String formatStyle) {
			this.formatStyle = formatStyle;
		}

		public String getVarText() {
			return varText;
		}

		public void setVarText(String varText) {
			this.varText = varText;
		}
	}

}
