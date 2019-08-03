package com.ctop.fw.common.excelexport;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.ReflectionUtils;

import com.ctop.base.dto.BaseDictDetailDto;
import com.ctop.base.dto.BaseDictDto;
import com.ctop.base.service.BaseDictService;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CellRendererBuilder {
	public static CellRendererBuilder INSTANCE = new CellRendererBuilder();
	public static CellRenderer NOOP = INSTANCE.buildNoopRenderer();

	/** 注册的解析renderer的方法 */
	private Map<String, Method> rendererRegister = new HashMap<String, Method>();

	private CellRendererBuilder() {
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Renderer.class)) {
				rendererRegister.put(method.getAnnotation(Renderer.class)
						.value(), method);
			}
		}

	}

	/**
	 * 根据配置的规则，生成校验器 规则： vtype1:validatorParam1;vtype2:validatorParam2;
	 * */
	public CellRenderer buidlRenderer(GridColumn column) {
		try {
			if(column == null) {
				return NOOP;
			}
			String type = column.getExcelRenderer();
			Method method = rendererRegister.get(type);
			if (method != null) {
				return (CellRenderer) method.invoke(this, column);
			}
		} catch (Exception e) {
			// 解析不了，不用renderer, 原样输出
		}
		return NOOP;
	}
	 
	public CellRenderer buildNoopRenderer()  {
		return new NoopCellRenderer();
	}
	
	public static class NoopCellRenderer implements CellRenderer {
		public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) { 
			if(input instanceof String) {
				cell.setCellValue((String) input);
			} else if(input instanceof Integer) {
				cell.setCellValue(((Integer) input).intValue());
			} else if(input instanceof Number) {  
				cell.setCellValue( ( (Number) input).doubleValue()); 
			} else if(input instanceof Date) {
				String datePattern = "yyyy-MM-dd HH:mm";
				if(gridColumn != null 
						&& StringUtils.isNotBlank(gridColumn.getField())
						&& !(rowData instanceof Map)){
					Field dataField = ReflectionUtils.findField(rowData.getClass(), gridColumn.getField());
					if(dataField.isAnnotationPresent(JsonFormat.class)){
						JsonFormat annotation = dataField.getAnnotation(JsonFormat.class);
						datePattern = annotation.pattern();
					}
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
				String text = sdf.format(input);
				text = text.replace(" 00:00", "");
				cell.setCellValue(text);
			}
		}
		
	}
	
	@Renderer("dict")
	public CellRenderer buildDictRenderer(GridColumn column)  {
		return new DictCellRenderer(column.getDict());
	}
	
	@Renderer("date")
	public CellRenderer buildDateRenderer(GridColumn column)  {
		return new DateCellRenderer(column.getDateFormat());
	}  
	 
	@Renderer("notEmptyRenderer")
	public CellRenderer buildNotEmptyRenderer(GridColumn column)  {
		return new NotEmptyRenderer();
	}
	
	public static class NotEmptyRenderer implements CellRenderer {
		public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) {
			String text = input != null && !"".equals(input) ? "是" : "否";
			cell.setCellValue(text);
		}
	}
	

	public static class EmptyColumnRenderer implements CellRenderer {
		public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) {
			
		}
	}
	
	
	@Renderer("textFieldRenderer")
	public CellRenderer buildTextFieldRenderer(GridColumn column)  {
		return new TextFieldRenderer(column.getTextField());
	}
	
	public static class TextFieldRenderer implements CellRenderer {
		
		private String textField;
		public TextFieldRenderer(String textField) {
			this.textField = textField;
		}

		@SuppressWarnings("unchecked")
		public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) {
			if(rowData instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) rowData;
				String text = (String) map.get(textField);
				cell.setCellValue(text);
			} else {
				Class<?> clz = rowData.getClass();
				Field field = ReflectionUtils.findField(clz, textField);
				field.setAccessible(true);
				Object text = ReflectionUtils.getField(field, rowData);
				if(text instanceof String) {
					cell.setCellValue((String) text);
				} else if(text != null) {
					cell.setCellValue(text.toString());
				} 
			}
		}
		
	};
	
	@Renderer("decimalRenderer")
	public CellRenderer buildDecimalRenderer(GridColumn column)  {
		return new CellRenderer() {
			
			private CellStyle style;

			public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) { 
				
				if(this.style == null) {
					Workbook wb = cell.getSheet().getWorkbook();

					this.style = wb.createCellStyle();
					DataFormat df = wb.createDataFormat(); 
					String format = "#,##0";
					if(column.getDecimals() != null) {
						int decimals = column.getDecimals();
						if(decimals > 0) {
							String text = StringUtil.repeat("0", decimals);
							format = format + "." + text;
						}
					}
					style.setDataFormat(df.getFormat(format));
				}
				
				cell.setCellStyle(style);
				if (input==null || "".equals(input)) {
					cell.setCellValue(0D);
				} else if(input instanceof Number) {
					cell.setCellValue(((Number) input).doubleValue());
				} else {
					cell.setCellValue(input.toString());
				}
			}
				
		};
	}  
	
	@Renderer("rateRenderer")
	public CellRenderer buildRateRenderer(GridColumn column)  {
		
		return new CellRenderer() {
			CellStyle style;

			public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) { 
				if(this.style == null) {
					Workbook wb = cell.getSheet().getWorkbook();

					this.style = wb.createCellStyle();
					DataFormat df = wb.createDataFormat(); 
					String format = "#,##0.00";
					if(StringUtil.isNotEmpty(column.getRateText())) {
						String rateText = column.getRateText();
						if("%".equals(rateText)) {
							format = "0.00%";
						}
					}
					style.setDataFormat(df.getFormat(format));
				}
				
				if (input==null || "".equals(input)) {
					cell.setCellValue(0D);
				} else if(input instanceof Number) {
					cell.setCellValue(((Number) input).doubleValue());
				} else {
					cell.setCellValue(input.toString());
				}
				cell.setCellStyle(style);
			}
				
		};
	}  
	
	@Renderer("asnCodeWithStatus")
	public CellRenderer buildAsnCodeWithStatusRenderer(GridColumn column)  {
		BaseDictService baseDictService = AppContextUtil.getBean(BaseDictService.class);
		BaseDictDto dict = baseDictService.findBaseDictWithDetails("ASN_STATUS"); 
		final Map<String, String> map = new HashMap<String, String>();
		if(dict.getDetails() != null) {
			List<BaseDictDetailDto> details = dict.getDetails();
			for(BaseDictDetailDto detail : details) {
				map.put(detail.getCode(), detail.getName());
			}
		}
		
		return new CellRenderer() {

			@SuppressWarnings("unchecked")
			public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) { 
				List<AsnCodeWithStatus> list = (List<AsnCodeWithStatus>) input; 
				StringBuilder builder = new StringBuilder();
				for(AsnCodeWithStatus temp : list) {
					builder.append(temp.getAsnCode());
					builder.append("(");
					builder.append(map.get(temp.getAsnStatus()));
					builder.append(") ");
				}
				cell.setCellValue(builder.toString());
			}
				
		};
	}  
	
	@Renderer("entityRevision")
	public CellRenderer buildEntityRevisionRenderer(GridColumn column) {
		return new EntityRevisionCellRenderer(column);
	}
	
	@Renderer("rowNumber") 
	public CellRenderer buildRowNumberRenderer(GridColumn column){
		return new RowNumberCellRenderer();
	}
	
	public static class AsnCodeWithStatus implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7509419144274069038L;
		String asnCode;
		String asnStatus;
		public AsnCodeWithStatus(String asnCode, String asnStatus) {
			this.asnCode = asnCode;
			this.asnStatus = asnStatus;
		}
		public String getAsnCode() {
			return asnCode;
		}
		public void setAsnCode(String asnCode) {
			this.asnCode = asnCode;
		}
		public String getAsnStatus() {
			return asnStatus;
		}
		public void setAsnStatus(String asnStatus) {
			this.asnStatus = asnStatus;
		}
	}
}
