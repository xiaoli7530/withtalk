package com.ctop.fw.sys.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.sys.entity.SysExcelImportColumn;


/**
 * <pre>
 * 功能说明：
 * 示例程序如下：
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@Service
@Transactional
public class SysExcelImportColumnService {


	@Autowired
	private EntityManager entityManager; 
	 
	
	@Transactional(readOnly=true)
	public List<SysExcelImportColumn> buildDraftColumns(String table) {
		SessionImplementor session = this.entityManager.unwrap(SessionImplementor.class);
		String user = "";
		try {
			user = session.connection().getMetaData().getUserName();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String columnNameSql = "(select COMMENTS from all_col_comments where owner = :user and table_name = :table and column_name = a.column_name)";
		String pkIndSql = "(select 'Y' from user_cons_columns cu, user_constraints au " +
				"where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' " + 
				"and cu.owner = :user and au.table_name = upper(:table) and cu.column_name = upper(a.column_name) ) ";
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField(columnNameSql, "columnName", ",");
		sql.appendField("table_name", "targetTable", ",");
		sql.appendField("column_name", "targetColumn", ",");
		sql.appendField("data_type", "targetType", ",");
		sql.appendField("data_length", "targetLength", ",");
		sql.appendField("data_precision", "targetPrecision", ",");
		sql.appendField("data_scale", "targetScale", ",");
		sql.appendField(pkIndSql, "targetColumnPkInd", ",");
		sql.appendField("column_name", "tempColumn");
		sql.append(" from all_tab_cols a ");
		sql.append("where table_name = :table and owner = :user");
		sql.addParameter("user", user);
		sql.addParameter("table", table);
		List<SysExcelImportColumn> list = sql.query(entityManager, SysExcelImportColumn.class);
		return list;
	}
}

