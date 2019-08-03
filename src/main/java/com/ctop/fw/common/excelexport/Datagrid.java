package com.ctop.fw.common.excelexport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;

public class Datagrid implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private List<List<GridColumn>> columns;
	private String url;
	private NuiPageRequestData queryParams;
	private String method;
	//列表数据
	private List<Object> data;

	/**
	 * 列定义行中增加一列
	 * @param column
	 */
	public void appendColumn(GridColumn column) {
		if(this.columns == null) {
			this.columns = new ArrayList<List<GridColumn>>();
			this.columns.add(new ArrayList<GridColumn>());
		}
		List<GridColumn> curColumns = this.columns.get(this.columns.size() - 1);
		curColumns.add(column);
	}
	
	/**
	 * 复合表头时，列定义中 新起一行
	 */
	public void startNewColumnRow() {
		if(this.columns == null) {
			this.columns = new ArrayList<List<GridColumn>>();
		}
		this.columns.add(new ArrayList<GridColumn>());
	}
	
	/**
	 * 计算每个ExcelColumn的cell的columnIndex, 合并单元格的是左上角的columnIndex;
	 */
	public void populateColumnIndex() {
		//用来标志单元格是否占用, true=单元格已占用， false=未占用
		List<BitSet> bitSetList = new ArrayList<BitSet>(columns.size());
		for(int i=0; i<columns.size(); i++) {
			bitSetList.add(new BitSet());
		}
		int rowIndex = 0;
		for(List<GridColumn> list : columns) {
			BitSet bitRow = bitSetList.get(rowIndex);
			int bitColIndex = 0;
			for(GridColumn column : list) {
				//忽略导出的字段，不计算；
				if(column.isExportIgnored()) {
					continue;
				}
				//如果已占用，则用下一列
				while(bitRow.get(bitColIndex)) {
					bitColIndex++;
				}
				//columnIndex记录单元格区域的左上角的columnIndex;
				column.setColumnIndex(bitColIndex);
				//将当前标题跨越的所有单元格设置成已占用(true)
				int colspan = column.getColspan() <= 0 ? 1 : column.getColspan();
				int rowspan = column.getRowspan() <= 0 ? 1 : column.getRowspan();
				for(int i=0; i<rowspan; i++) {
					if(rowIndex + i >= bitSetList.size()) {
						throw new BusinessException("字段" + column.getTitle() + "的rowspan超出标题行数!");
					}
					BitSet tempRow = bitSetList.get(rowIndex + i);
					for(int j=0; j < colspan; j++) {
						//设置成已占用；
						tempRow.set(bitColIndex + j);
					}
				}
				bitColIndex += colspan;
			}
			rowIndex++;
		}
	}
	
	/**
	 * 取得有数据取值的字段定义；
	 * @return
	 */
	public List<GridColumn> buildFlatColumns() {
		List<GridColumn> flatList = new ArrayList<GridColumn>();
		for(List<GridColumn> list : columns) {
			for(GridColumn column : list) {
				if(StringUtil.isNotEmpty(column.getField()) && !column.isExportIgnored()) {
					flatList.add(column);
				}
			}
		}
		return flatList;
	}
	
	/**
	 * 根据一批数据处理一下列的renderer, 有的renderer需要批量取数据；
	 * @param rowDatas
	 */
	public void processColumnRenderer(List<?> rowDatas) {
		for(List<GridColumn> columnList : this.columns) {
			for(GridColumn column : columnList) {
				column.buildRenderer();
				if(column.getValueGetter() instanceof RowDatasAware) {
					RowDatasAware aware = (RowDatasAware) column.getValueGetter();
					aware.setRowDatas(rowDatas);
				}
				CellRenderer renderer = column.getRenderer();
				if(renderer instanceof RowDatasAware) {
					RowDatasAware aware = (RowDatasAware) renderer;
					aware.setRowDatas(rowDatas);
				}
				
			}
		}
	}
	
	public DatagridRenderer getRenderer(Sheet sheet) {
		return new DatagridRenderer(this, sheet);
	}
	 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<List<GridColumn>> getColumns() {
		return columns;
	}
	public void setColumns(List<List<GridColumn>> columns) {
		this.columns = columns;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	} 
	public NuiPageRequestData getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(NuiPageRequestData queryParams) {
		this.queryParams = queryParams;
	}

	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}
}
