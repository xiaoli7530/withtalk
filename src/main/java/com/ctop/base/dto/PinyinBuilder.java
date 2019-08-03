package com.ctop.base.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ctop.fw.common.utils.StringUtil;

public class PinyinBuilder {

	private String[] pinyins;
	private int[] chsPyIndexArr;
	private char minChar;
	private char maxChar;
	
	/**
	 * 生成拼音：生成的拼音中包含全拼的拼音及缩写的拼音(只含头字母)
	 * @param text
	 * @return
	 */
	public String generatePinyin(String text) {
		if(StringUtil.isEmpty(text)) {
			return "";
		}
		if(chsPyIndexArr != null && chsPyIndexArr.length > 0) {
			char[] chars = text.toCharArray();
			StringBuilder b1 = new StringBuilder();
			StringBuilder b2 = new StringBuilder();
			for(char c : chars) {
				//c在不在汉字拼音表中
				if(c < minChar || c > maxChar) {
					b1.append(c);
					b2.append(c);
				} else {
					int index = (int)(c - minChar);
					int chsPyIndex = chsPyIndexArr[index];
					if(chsPyIndex == -1) {
						//字典库中没这个字
						b1.append(c);
						b2.append(c);
					} else {
						String pinyin = pinyins[chsPyIndex];
						b1.append(pinyin);
						b2.append(pinyin != null && pinyin.length() > 0 ? pinyin.charAt(0) : '\0');
					}
				}
			}
			return b2.append(' ').append(b1).toString();
		} else {
			return text;
		}
	}
	
	/**
	 * 将拼音库缓存起来，所有pinyin去重后存在一个数组中；
	 * 汉字及其拼音的索引存在一个数组中，按汉字的升序存放
	 * @param list
	 */
	public void initWithPinyinList(List<BasePinyinDto> list) {
		//唯一的，排序了的拼音
		List<String> pyList = list.parallelStream().map(pinyin -> pinyin.getPy()).distinct().sorted().collect(Collectors.toList());
		this.pinyins = pyList.toArray(new String[pyList.size()]);
		this.minChar = list.get(0).getFirstChar();
		this.maxChar = list.get(list.size() - 1).getFirstChar();
		this.chsPyIndexArr = new int[maxChar - minChar + 1];
		for(int i = 0; i < this.chsPyIndexArr.length; i++) {
			this.chsPyIndexArr[i] = -1;
		}
		for(BasePinyinDto pinyin : list ) {
			int index = pinyin.getFirstChar() - minChar;
			chsPyIndexArr[index] = pyList.indexOf(pinyin.getPy());
		}
	}
	
	
	public String[] getPinyins() {
		return pinyins;
	}
	public void setPinyins(String[] pinyins) {
		this.pinyins = pinyins;
	}
	public int[] getChsPyIndexArr() {
		return chsPyIndexArr;
	}
	public void setChsPyIndexArr(int[] chsPyIndexArr) {
		this.chsPyIndexArr = chsPyIndexArr;
	}
	public char getMinChar() {
		return minChar;
	}
	public void setMinChar(char minChar) {
		this.minChar = minChar;
	}
	public char getMaxChar() {
		return maxChar;
	}
	public void setMaxChar(char maxChar) {
		this.maxChar = maxChar;
	}
}
