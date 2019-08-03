package com.ctop.fw.common.excelexport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import com.ctop.base.dto.BaseDictDetailDto;
import com.ctop.base.dto.BaseDictDto;
import com.ctop.base.service.BaseDictService;
import com.ctop.fw.common.utils.AppContextUtil;

public class DictCellRenderer implements CellRenderer {
	private Map<String, String> map;
 
	public DictCellRenderer(String dictNo) { 
		BaseDictService baseDictService = AppContextUtil.getBean(BaseDictService.class);
		BaseDictDto dict = baseDictService.findBaseDictWithDetails(dictNo); 
		if(dict != null && dict.getDetails() != null) {
			List<BaseDictDetailDto> details = dict.getDetails();
			map = new HashMap<String, String>(details.size());
			for(BaseDictDetailDto detail : details) {
				map.put(detail.getCode(), detail.getName());
			}
		}
	}
	
	public void render(Cell cell, Object input, Object rowData,GridColumn gridColumn) {
		if(input != null && map != null) {
			String name = (String) map.get(input); 
			if(name == null) {
				if(input != null) {
					name = input.toString();
				}
			}
			cell.setCellValue(name);
			/*String name = (String) map.get(input); 
			name = name == null ? (String) input : name;
			cell.setCellValue(name);*/
		} else if (input != null){
			cell.setCellValue(input.toString());
		}
	}

}
