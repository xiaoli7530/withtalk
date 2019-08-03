package com.ctop.core.pdf.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.entity.SysExcelImportColumn;
import com.ctop.fw.sys.excelImport.support.ExcelImportRow;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy.TextChunk;

/**
 * 解析PDF中的表格数据，简单的通过列的X坐标的范围来取数据；
 * @author 龚建军
 *
 */
public class TableExtractor {
	private PdfTextChunkExtractor processor;
	private float scale = 595f/210f;
	private List<TableColumn> columns = new ArrayList<TableColumn>();
	private Predicate<Map<String, List<TextChunk>>> predicate;
	
	public TableExtractor(PdfTextChunkExtractor processor) {
		this.processor = processor;
	}
	
	public TableExtractor addColumn(String field, float startX, float endX, String[] titles) {
		startX = this.scale * startX;
		endX = this.scale * endX;
		this.columns.add(new TableColumn(field, startX, endX, titles));
		return this;
	}
	
	public TableExtractor addColumn(String field, float startX, float endX, String[] titles, TextConverter converter) {
		startX = this.scale * startX;
		endX = this.scale * endX;
		this.columns.add(new TableColumn(field, startX, endX, titles, converter));
		return this;
	}
	
	public TableExtractor endListDataWithPredicate(Predicate<Map<String, List<TextChunk>>> predicate) {
		this.predicate = predicate;
		return this;
	}
	
	public List<Map<String, List<TextChunk>>> parse() {
		TableColumn.logColumns(this.columns);
		List<Map<String, List<TextChunk>>> allList = new ArrayList<Map<String, List<TextChunk>>>();
		for(List<TextChunk> textChunkList : this.processor) {
			List<Map<String, List<TextChunk>>> list = this.parseInner(textChunkList);
			allList.addAll(list);
		}
		return allList;
	}
	
	private List<Map<String, List<TextChunk>>> parseInner(List<TextChunk> textChunks) {
		TableColumn.logColumns(this.columns);
		// 复制一份；
		textChunks = new ArrayList<TextChunk>(textChunks);
		if(PdfTextChunkUtil.DEBUG) {
			System.out.println(" ================== this.textChunkList ===============");
			System.out.println(PdfTextChunkUtil.getText(textChunks));
		}
		// 转换成每行一个List
		List<List<TextChunk>> lines = this.convertToLines(textChunks);
		if(PdfTextChunkUtil.DEBUG) {
			System.out.println(" ================== this.convertToLines(textChunks) ===============");
			this.logLines(lines);
		}
		// 初步过滤不是表格数据的行；
		List<Map<String, List<TextChunk>>> subLines = this.fitlerAndConvertByColumns(lines, this.columns);
		if(PdfTextChunkUtil.DEBUG) {
			System.out.println(" ================== this.fitlerByColumns(lines, this.columns) ===============");
			this.logMapLines(subLines);
		}
		// 根据column的标题找到匹配的第一条记录，根据predicate定位最后的记录；
		subLines = this.filterWithTitleAndPredicate(subLines, columns);
		if(PdfTextChunkUtil.DEBUG) {
			System.out.println(" ================== this.filterWithTitleAndPredicate(subLines, columns) ===============");
			this.logMapLines(subLines);
		}
		return subLines;
	}
	
	public List<Map<String, String>> extractMapList() {
		List<Map<String, List<TextChunk>>> mapLines = this.parse(); 
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(Map<String, List<TextChunk>> mapLine : mapLines) {
			Map<String, String> map = new HashMap<String, String>();
			for(Map.Entry<String, List<TextChunk>> entry : mapLine.entrySet()) {
				map.put(entry.getKey(), PdfTextChunkUtil.getText(entry.getValue()));
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 将列表转换成指定类型的bean;
	 * @param cls
	 * @return
	 */
	public <T> List<T> extractBeanList(Class<T> cls) {
		List<Map<String, List<TextChunk>>> mapLines = this.parse();
		List<T> list = new ArrayList<T>();
		for(Map<String, List<TextChunk>> mapLine : mapLines) {
			T bean = BeanUtils.instantiate(cls);
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
			for(TableColumn tc : this.columns) {
				List<TextChunk> textChunks = mapLine.get(tc.getField());
				String text = PdfTextChunkUtil.getText(textChunks);
				Object obj = tc.getConverter().convert(text);
				bw.setPropertyValue(tc.getField(), obj);
			} 
			list.add(bean);
		}
		return list;
	}
	
	private void logLines(List<List<TextChunk>> lines) {
		System.out.println(" ================== logLines ===============");
		for(List<TextChunk> list : lines) {
			System.out.println(PdfTextChunkUtil.getText(list));
		}
	}
	
	public void logMapLines(List<Map<String, List<TextChunk>>> mapLines) {
		System.out.println(" ================== logLines ===============");
		
		for(Map<String, List<TextChunk>> mapLine : mapLines) {
			for(TableColumn column : this.columns) {
				List<TextChunk> chunks = mapLine.get(column.getField());
				System.out.print("[" + column.getField() + ": " +PdfTextChunkUtil.getText(chunks) + " ],  ");
			}
			System.out.println("");
		}
	}
	
	public void logMapList(List<Map<String, String>> mapLines) {
		System.out.println(" ================== logLines ===============");
		
		for(Map<String, String> mapLine : mapLines) {
			for(TableColumn column : this.columns) {
				Object value = mapLine.get(column.getField());
				System.out.print("[" + column.getField() + ": " + value + " ],  ");
			}
			System.out.println("");
		}
	}
	
	/** 转换成每行一个list的方式 */
	private List<List<TextChunk>> convertToLines(List<TextChunk> textChunks) {
		List<List<TextChunk>> lines = new ArrayList<List<TextChunk>>();
		List<TextChunk> line = new ArrayList<TextChunk>();
		TextChunk lastChunk = null;
		for (TextChunk chunk : textChunks) {
			if (lastChunk == null) {  
				line.add(chunk);
			} else {
				if (sameLine(chunk, lastChunk)) {
					line.add(chunk);
				} else {
					lines.add(line);
					line = new ArrayList<TextChunk>();
					line.add(chunk);
				}
			}
			lastChunk = chunk;
		}
		lines.add(line);
		return lines;
	}
	
	boolean sameLine(TextChunk chunk, TextChunk lastChunk) {
        return chunk.getLocation().sameLine(lastChunk.getLocation());
    }
	
	
	/**
	 * 根据列字段的X范围过滤掉 含textChunk不在列的宽度范围内的记录
	 * @param lines
	 * @param columns
	 * @return
	 */
	private List<Map<String, List<TextChunk>>> fitlerAndConvertByColumns(List<List<TextChunk>> lines, List<TableColumn> columns) {
		List<Map<String, List<TextChunk>>> list = new ArrayList<Map<String, List<TextChunk>>>();
		outer:
		for(List<TextChunk> line : lines) {
			//同一行，如个是表格的数据的话，所列的数据都在列的范围内；
			for(TextChunk textChunk : line) {
				if(!this.isInColumnsRange(textChunk, columns)) {
					continue outer;
				}
			}
			Map<String, List<TextChunk>> map = new HashMap<String, List<TextChunk>>();
			list.add(map);
			for(TableColumn column : columns) {
				List<TextChunk> fieldChunks = line.stream()
						.filter(chunk -> column.isTextChunkInColumnRange(chunk))
						.collect(Collectors.toList());
				map.put(column.getField(), fieldChunks);
			} 
		}
		return list;
	}
	
	/**
	 * 
	 * @param mapLines
	 * @param columns
	 * @return
	 */
	private List<Map<String, List<TextChunk>>> filterWithTitleAndPredicate(List<Map<String, List<TextChunk>>> mapLines, List<TableColumn> columns) { 
		Map<String, List<String>> fieldTitles = new HashMap<String, List<String>>();
		int index = 0;
		for(Map<String, List<TextChunk>> mapLine : mapLines) {
			// 一行一行匹配标题
			boolean allTitleMatched = true;
			for(TableColumn column : columns) {
				List<TextChunk> chunks= mapLine.get(column.getField());
				String text = PdfTextChunkUtil.getText(chunks);
				List<String> titles = fieldTitles.get(column.getField());
				if(titles == null) {
					titles = new ArrayList<String>();
					fieldTitles.put(column.getField(), titles);
				}
				if(!StringUtil.isEmpty(text)) {
					titles.add(text);
				}
				// 是否所有的列标题都匹配；
				allTitleMatched = allTitleMatched && column.matchTitlesWithListTails(titles);
			}
			index++;
			if(allTitleMatched) {
				break;
			}
		}
		if(index >= mapLines.size()) {
			return Collections.emptyList();
		}
		
		mapLines = mapLines.subList(index, mapLines.size());
		// 取到截至的记录前为止；
		if(this.predicate != null) {
			int endIndex = ListUtil.findIndex(mapLines, this.predicate);
			if (endIndex != -1) {
				mapLines = mapLines.subList(0, endIndex);
			}
		}
		return mapLines;
	}
	
	/**
	 * 判断textChunk是否在任意一个列的范围内
	 * @param textChunk
	 * @param columns
	 * @return
	 */
	private boolean isInColumnsRange(TextChunk textChunk, List<TableColumn> columns) {
		for(TableColumn column : columns) {
			if(column.isTextChunkInColumnRange(textChunk)) {
				return true;
			}
		}
		return false;
	} 
	
	public List<ExcelImportRow> readAccordingToSysExcelColumns(List<SysExcelImportColumn> columns) {
		List<Map<String, List<TextChunk>>> mapLines = this.parse();
		List<ExcelImportRow> list = new ArrayList<ExcelImportRow>();
		int i=0;
		for(Map<String, List<TextChunk>> mapLine : mapLines) {
			ExcelImportRow rowData = new ExcelImportRow();
			rowData.setRowNum(i);
			int index = -1; 
			String[] values = new String[columns.size()]; 
			for (SysExcelImportColumn column : columns) {
				index++;
				// 根据计算出来的列号来取值
				if (column.getExcelColumnIndexInt() != null) {
					TableColumn tc = this.columns.get(column.getExcelColumnIndexInt());
					List<TextChunk> textChunks = mapLine.get(tc.getField()); 
					String text = PdfTextChunkUtil.getText(textChunks); 
					if("partCodePdf".equals(tc.getField())) {
						text = text.replace(" ", " ");
					}
					Object obj = tc.getConverter().convert(text);
					if(obj instanceof Date) {
						text = column.formateDate((Date) obj, "yyyy-MM-dd HH:mm:ss");
					} else if(obj instanceof Number) {
						text = ((Number) obj).toString();
					}
					values[index] = text;
				} else if ("Y".equalsIgnoreCase(column.getTargetColumnPkInd())) {
					// 配置是目标表的主键的，自动生成主键
					values[index] = (UUID.randomUUID().toString()).replace("-", ""); 
				}
			}
			rowData.setFields(values);
			list.add(rowData);
			i++;
		}
		return list; 
	}

	public List<TableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}
	
	
}