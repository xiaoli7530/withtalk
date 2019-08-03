package com.ctop.fw.sys.service;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ctop.fw.common.constants.Constants.ExcelImportStage;
import com.ctop.fw.common.excelexport.ExcelUtil;
import com.ctop.fw.common.model.FileDto;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.entity.SysExcelImport;
import com.ctop.fw.sys.entity.SysExcelImportInstance;
import com.ctop.fw.sys.entity.SysExcelImportInstance.DataIterable;
import com.ctop.fw.sys.event.TempTableDataTransferedEvent;
import com.ctop.fw.sys.excelImport.support.ExcelImportRow;
import com.ctop.fw.sys.repository.SysExcelImportInstanceRepository;


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
public class SysExcelImportInstanceService {
	private static Logger logger = LoggerFactory.getLogger(SysExcelImportInstanceService.class);


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysExcelImportInstanceRepository sysExcelImportInstanceRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SysExcelImportService sysExcelImportService;
	@Autowired 
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private SysExcelImportInstanceService self;
	
	
	public SysExcelImportInstance getById(String id) {
		return this.sysExcelImportInstanceRepository.findOne(id);
	} 

	@Transactional
	public SysExcelImportInstance saveSysExcelImportInstance(SysExcelImportInstance instance) {
		instance = this.sysExcelImportInstanceRepository.save(instance);
		return instance;
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysExcelImportInstance> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysExcelImportInstance.class, "t");
		sql.append(" from SYS_EXCEL_IMPORT_INSTANCE t where 1=1 ");
		return sql.pageQuery(entityManager, request, SysExcelImportInstance.class);
	}
	
	/**
	 * 将数据导入中间表，并校验；
	 * @param instance
	 * @param it
	 * @return
	 */
	public SysExcelImportInstance importExcel2TempTable(SysExcelImportInstance instance, DataIterable it) {
		return this.importExcel2TempTable(instance, it, new HashMap<String, Object>()); 
	}
	
	/**
	 * 将数据导入中间表，并校验；
	 * 不加事务，里面调用两个方法，拆分成两个事务；保证，第二步SQL校验抛SQL异常时，数据已在中间表；
	 * 可以方便调试SQL；
	 * @param instance
	 * @param iterable
	 * @param params
	 * @return
	 */
	public SysExcelImportInstance importExcel2TempTable(SysExcelImportInstance instance, DataIterable iterable, Map<String, Object> params) {
		instance = self.importDataIntoTempTable(instance, iterable, params);
		instance = self.validateDataInTempTable(instance, params); 
		return instance; 
	}
	
	/**
	 * 将数据插入中间表；
	 * @param instance
	 * @param iterable
	 * @param params
	 * @return
	 */
	@Transactional
	public SysExcelImportInstance importDataIntoTempTable(SysExcelImportInstance instance, DataIterable iterable, Map<String, Object> params) {
		SysExcelImport excelImport = sysExcelImportService.getById(instance.getImportUuid());
		instance.setInstanceUuid(null);
		instance.setMessage(null);
		instance = this.sysExcelImportInstanceRepository.save(instance);
		instance.appendMessage("开始导入中间表!");
		instance.setExcelImport(excelImport);
		//删除临时表数据； 
		this.jdbcTemplate.execute("delete from " + instance.getTempTable() + " where import_instance_uuid = '" + instance.getInstanceUuid() + "'");  
		BatchSqlUpdate sql = excelImport.buildBatchSql4Insert(jdbcTemplate.getDataSource());
		Iterator<List<ExcelImportRow>> it = iterable.iterate(instance);
		while(it.hasNext()) {
			List<ExcelImportRow> rows = it.next();
			for(ExcelImportRow row : rows) {
				sql.update(row.getInsertParams());
			}
			sql.flush();
		}
		instance.appendMessage("上载文件导入中间表完成，记录数" + instance.getImportedNum() + ", 实际导入记录数:" + instance.getImportedRealNum()); 
		this.updateBaseEntityFields(instance, excelImport);
		instance.setExcelImport(excelImport);
		instance.setParamsMap(params);
		return instance; 
	}
	
	/**
	 * 校验在中间表的数据；
	 * @param excelImport
	 * @param instance
	 * @param params
	 * @return
	 */
	@Transactional
	public SysExcelImportInstance validateDataInTempTable(SysExcelImportInstance instance, Map<String, Object> params) {
		if("N".equals(instance.getBasicValid())) {
			instance.setStage(ExcelImportStage.ENTER_TEMP_TABLE);
			instance.setInvalidNum(this.populateInvalidNumInTempTable(instance));
			instance.appendMessage("数据基本校验未通过, " + instance.getInvalidNum() + "条无效数据."); 
			instance = this.sysExcelImportInstanceRepository.save(instance); 
		} else {
			instance.setStage(ExcelImportStage.BASIC_VALID);
			instance.appendMessage("数据基本校验通过。");
			instance = this.processTempTable(instance, params);
		}
		SysExcelImport excelImport = sysExcelImportService.getById(instance.getImportUuid());
		instance.setExcelImport(excelImport);
		instance.setParamsMap(params);
		return instance; 
	}
	
	private void updateBaseEntityFields(SysExcelImportInstance instance, SysExcelImport excelImport) {
		String sql = excelImport.buildUpdateBaseEntityFieldsSql();
		if(StringUtil.isEmpty(sql)) {
			return;
		}
		Query query = this.entityManager.createNativeQuery(sql);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		query.setParameter("createdBy", UserContextUtil.getAccountUuid());
		query.setParameter("createdDate", format.format(now));
		query.setParameter("updatedBy", UserContextUtil.getAccountUuid());
		query.setParameter("updatedDate", format.format(now));
		query.setParameter("isActive", "Y");
		query.setParameter("version", 0L);
		query.setParameter("instanceUuid", instance.getInstanceUuid());
		query.executeUpdate();
	}
	
	public void invokeSqlBlock(String block, Map<String, Object> paramMap) {
		if(StringUtil.isEmpty(block)) {
			return;
		}
		System.out.println(paramMap);
		block = block.trim();
		String sql = NamedParameterUtils.parseSqlStatementIntoString(block);
		final Object[] arr = NamedParameterUtils.buildValueArray(block, paramMap); 
		try {
			logger.debug("sql:\n" + sql);
			jdbcTemplate.execute(sql, new CallableStatementCallback<Object>() {

				public Object doInCallableStatement(CallableStatement arg0)
						throws SQLException, DataAccessException {
					int i = 1;
					for (Object item : arr) {
						if (item instanceof String) {
							arg0.setString(i++, (String) item);
						}
					}

					arg0.execute();
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex.getCause() != null && ex.getCause() instanceof SQLException) {
				SQLException cause = (SQLException) ex.getCause();
				int index = cause.getMessage().indexOf("\n");
				String temp = index != -1 ? cause.getMessage().substring(0, index) : cause.getMessage();				 
				throw BusinessException.template(temp);
			}
		}

	}
	
	/**临时表分页*/
	@SuppressWarnings("rawtypes")
	public PageResponseData<Map> queryImportTempTable(String instanceUuid, PageRequestData request) {
		SysExcelImportInstance instance = this.sysExcelImportInstanceRepository.findOne(instanceUuid);
		SysExcelImport excelImport = sysExcelImportService.getById(instance.getImportUuid());
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append(excelImport.buildSelectTempTableSql()); 
		sql.addParameter("instanceUuid", instanceUuid);
		return sql.pageQuery(entityManager, request, Map.class);
	}
	
	@Transactional(readOnly=true)
	public Map<String, Object> queryColumnList(String instanceUuid,String tempName) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select VEHICLE_CONFIGURATION1,VEHICLE_CONFIGURATION2,VEHICLE_CONFIGURATION3,VEHICLE_CONFIGURATION4, VEHICLE_CONFIGURATION5,VEHICLE_CONFIGURATION6,VEHICLE_CONFIGURATION7,VEHICLE_CONFIGURATION8,VEHICLE_CONFIGURATION9,VEHICLE_CONFIGURATION10,VEHICLE_CONFIGURATION11,VEHICLE_CONFIGURATION12,VEHICLE_CONFIGURATION13,VEHICLE_CONFIGURATION14,VEHICLE_CONFIGURATION15,VEHICLE_CONFIGURATION16,VEHICLE_CONFIGURATION17,VEHICLE_CONFIGURATION18,VEHICLE_CONFIGURATION19,VEHICLE_CONFIGURATION20 ");
		sql.append(" from "+tempName+" t ");
		sql.append(" where t.import_instance_uuid = :instanceUuid ");
		sql.append(" and t.import_row_num = 1 ");

		Session session = this.entityManager.unwrap(Session.class);
		SQLQuery query = session.createSQLQuery(sql.toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceUuid", instanceUuid);
		query.setProperties(params);
		
		List<Map> data = new ArrayList<Map>();
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		for (int j = 0; j < list.size(); j++) {
			Map<String, Object> m = list.get(j);
			data.add(j, m);
		}
		Map<String, Object> result = new HashMap<>();
		result.put("total", data.size());
		result.put("rows", data);
		return result;
	}
	
	/**数据导入中间表后，对中间表的数据做处理*/
	@Transactional
	public SysExcelImportInstance processTempTable(SysExcelImportInstance instance, Map<String, Object> params) {
//		SysExcelImportInstance instance = getById(instanceDto.getInstanceUuid());
		getById(instance.getInstanceUuid());
		if(!ExcelImportStage.BASIC_VALID.equals(instance.getStage())
				&& !ExcelImportStage.ADVANCE_VALID.equals(instance.getStage())) {
			throw BusinessException.template("临时表数据必须基本校验通过，才能进行业务逻辑校验!");
		}
		instance.appendMessage("开始中间表业务逻辑校验.");
		SysExcelImport excelImport = this.sysExcelImportService.getById(instance.getImportUuid());
		instance.setExcelImport(excelImport);
		this.defaultProcess(instance, params);
		instance.setInvalidNum(this.populateInvalidNumInTempTable(instance)); 
		instance.setImportedRealNum(this.populateImportedRealNumInTempTable(instance)); 
		instance.appendMessage("中间表业务逻辑校验完成，" + instance.getInvalidNum() + "条无效数据,"+(instance.getImportedNum()-instance.getInvalidNum())+"条有效数据.");
		if(0L == instance.getInvalidNum().longValue()) {
			instance.setStage(ExcelImportStage.ADVANCE_VALID);
		}
		instance = this.sysExcelImportInstanceRepository.save(instance);
		instance.setParamsMap(params);
		return instance;
	}
	
	@Transactional
	public SysExcelImportInstance transferTempTable(SysExcelImportInstance instance) {
		return this.transferTempTable(instance, new HashMap<String, Object>());
	}
	
	/**对中间表数据处理完成后，导入正式表*/
	@Transactional
	public SysExcelImportInstance transferTempTable(SysExcelImportInstance instance, Map<String, Object> params) {
		logger.debug("开时导入临时表数据到正式表, instanceUuid:{}", instance.getInstanceUuid());
		getById(instance.getInstanceUuid());
		if(!ExcelImportStage.ADVANCE_VALID.equals(instance.getStage())) {
			throw BusinessException.template("临时表数据必须完成校验，才能导入正式表!");
		}
		instance.appendMessage("开始导入中间表数据到正式表.");
		SysExcelImport excelImport = this.sysExcelImportService.getById(instance.getImportUuid());
		instance.setExcelImport(excelImport);
		this.defaultTransfer(instance, params); 
		instance.setStage(ExcelImportStage.DONE);
		instance.appendMessage("中间表数据导入正式表完成.");
		//在事务提交后 清理中间表数据.
		this.publisher.publishEvent(new TempTableDataTransferedEvent(this, excelImport, instance));
		return instance;
	}
		
	
	@Transactional
	public SysExcelImportInstance defaultProcess(SysExcelImportInstance instance, Map<String, Object> paramMap) {
		if(paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		SysExcelImport excelImport = instance.getExcelImport();
		paramMap.put("accountUuid", instance.getAccountUuid());
		paramMap.put("companyUuid", instance.getCompanyUuid());
		//paramMap.put("warehouseUuid", UserContextUtil.getWarehouseUuid());
		paramMap.put("instanceUuid", instance.getInstanceUuid());
		paramMap.put("importUuid", instance.getImportUuid());
		//根据配置的数据，自动运行的校验逻辑
		this.invokeSqlBlock(excelImport.buildBaseProcessSqlBlock(), paramMap);
		//自定义的校验脚本
		this.invokeSqlBlock(excelImport.getProcessScript(), paramMap);
		return instance;
	}
	
	@Transactional
	public SysExcelImportInstance defaultTransfer(SysExcelImportInstance instance, Map<String, Object> paramMap) { 
		if(paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		paramMap.put("accountUuid", instance.getAccountUuid());
		paramMap.put("companyUuid", instance.getCompanyUuid());
		//paramMap.put("warehouseUuid", UserContextUtil.getWarehouseUuid());
		paramMap.put("instanceUuid", instance.getInstanceUuid());
		paramMap.put("importUuid", instance.getImportUuid());
		SysExcelImport excelImport = instance.getExcelImport();
		//根据配置的数据，执行的数据转移前的SQL BLOCK
//		this.invokeSqlBlock(excelImport.buildBeforeTransferSqlBlock(), paramMap);
		//有自定义转移脚本，则用自定义的，否则用默认的SQL转移数据
		if(!StringUtil.isEmpty(excelImport.getTransferScript() )) {
			this.invokeSqlBlock(excelImport.getTransferScript(), paramMap);
		} else {
			this.invokeSqlBlock(excelImport.buildDefaultTransferSqlBlock(instance), paramMap);
		}
		return instance;
	}
	
	/**
	 * 删除临时表中无效的行
	 * @param instance
	 * @return
	 */
	@Transactional
	public SysExcelImportInstance deleteInvalidRow(SysExcelImportInstance instance, Map<String, Object> params) {
		SysExcelImport excelImport = sysExcelImportService.getById(instance.getImportUuid());
		String sql = "delete from " + instance.getTempTable() + " where import_instance_uuid = :instanceUuid  and import_message is not null";
		Query query = this.entityManager.createNativeQuery(sql);
		query.setParameter("instanceUuid", instance.getInstanceUuid());
		query.executeUpdate();
		 

		instance.appendMessage("删除无效数据完成，重新校验...");
		instance = this.processTempTable(instance, params);
		this.sysExcelImportInstanceRepository.flush();
		instance.setExcelImport(excelImport);
		return instance;
	}
	
	/**
	 * 删除临时表中无效的行
	 * @param instance
	 * @return
	 */
	@Transactional
	public SysExcelImportInstance deleteInstanceData(SysExcelImportInstance instance) {
		String sql = "delete from " + instance.getTempTable() + " where import_instance_uuid = :instanceUuid";
		Query query = this.entityManager.createNativeQuery(sql);
		query.setParameter("instanceUuid", instance.getInstanceUuid());
		query.executeUpdate();
		return instance;
	}
	 
	/**计算无效数据数量*/
	public Long populateInvalidNumInTempTable(SysExcelImportInstance instance) {
		String sql = "select count(1) from " + instance.getTempTable() + " where import_instance_uuid = ? and import_message is not null "; 
		Long num =  this.jdbcTemplate.queryForObject(sql, new Object[]{instance.getInstanceUuid()}, Long.class);
		logger.debug("计算临时表中无效数据.({}:{}): num:{}", instance.getTempTable(), instance.getInstanceUuid(), num);
		return num;
	} 
	
	/**计算临时表中实际导入数据数量*/
	public Long populateImportedRealNumInTempTable(SysExcelImportInstance instance) {
		String sql = "select count(1) from " + instance.getTempTable() + " where import_instance_uuid = ? "; 
		Long num =  this.jdbcTemplate.queryForObject(sql, new Object[]{instance.getInstanceUuid()}, Long.class);
		logger.debug("计算临时表中实际导入数据数量.({}:{}): num:{}", instance.getTempTable(), instance.getInstanceUuid(), num);
		return num;
	} 
	
	@Transactional(readOnly=false)
	public void clearTempTable(SysExcelImportInstance instance) {
		logger.debug("导入完成事务提交后，执行临时表的清理，临时文件的清理，清理当前导入相关数据和文件，同时也清理一天前执行的导入遗留的数据和文件."); 
		//取所有的instanceUuid
		String sql = "select distinct import_instance_uuid from " + instance.getTempTable();
		List<String> instanceUuids = this.jdbcTemplate.queryForList(sql, String.class);
		for(String instanceUuid : instanceUuids) {
			SysExcelImportInstance current = instanceUuid.equals(instance.getInstanceUuid()) ? instance : this.getById(instanceUuid);
			//是当前导入，或者该导入是一天前的数据，则清除。
			if(current.getInstanceUuid().equals(instance.getInstanceUuid())
					|| (new Date().getTime() - instance.getUpdatedDate().getTime()) >= (1000 * 60 * 60 *24)) {
				//删除表数据
				current.appendMessage("清除临时表中数据.");
				this.deleteInstanceData(current);
				//删除临时文件
				File file = new File(current.getFilePath());
				file.deleteOnExit();
				current.appendMessage("清除临时文件.");
				this.saveSysExcelImportInstance(current);
			}
		}
	} 

	
	/**
	 * 样件零件上传
	 * @param file
	 * @return
	 */
	public PageResponseData<Map> uploadO2oIePart(MultipartFile file){
		PageResponseData<Map> map = null;
		logger.debug("上载导入文件.");
		File temp;
		FileDto fileDto = new FileDto();
		try {
			File tempDir = FileUtils.getTempDirectory();
			if (!tempDir.exists()) {
				tempDir.mkdirs();
			}
			temp = File.createTempFile("uploadimp_", ".tmp", tempDir);
			if (!temp.exists()) {
				temp.createNewFile();
			}
			logger.debug("tempDir.exists(): {}", tempDir.exists());
			logger.debug("temp.exists(): {}", temp.exists());
			logger.debug("file name {}", file.getOriginalFilename());
			logger.debug("contentType:{}, size: {}", file.getContentType(), file.getSize());
			FileUtils.copyInputStreamToFile(file.getInputStream(), temp);

			fileDto.setContentType(file.getContentType());
			fileDto.setName(file.getName());
			fileDto.setOriginalFilename(file.getOriginalFilename());
			fileDto.setSize(file.getSize());
			fileDto.setPath(temp.getAbsolutePath());
			logger.debug("upload file in {}", temp.getAbsolutePath());
			String fileName = file.getOriginalFilename();
			if(fileName.indexOf(".xls") != -1) {
				Workbook wb = ExcelUtil.createWorkBook(fileDto.getPath());
				int num = wb.getNumberOfSheets();
				for(int i=0; i<num; i++) {
					fileDto.getSheetNames().add(wb.getSheetName(i));
				}
				SysExcelImport sysExcelImport = this.sysExcelImportService.findByImportCode("O2O_IMPORTS_EXPORTS_DETAIL");
				SysExcelImportInstance instance = new SysExcelImportInstance();
				instance.setAccountUuid(UserContextUtil.getAccountUuid());
				instance.setCompanyUuid(UserContextUtil.getCompanyUuid());
				instance.setFileName(file.getName());
				instance.setFilePath(fileDto.getPath());
				instance.setImportUuid(sysExcelImport == null?"":sysExcelImport.getImportUuid());
				instance.setSheetName(fileDto.getSheetNames().get(0));
				instance.setNameRowIndex(0l);
				instance.setStartRowIndex(1l);
				instance.setPageSize(1000l);				
				instance = importExcel2TempTable(instance, instance.buildExceTableIterable(wb), new HashMap<String, Object>());  
				ExcelUtil.close(wb);
				NuiPageRequestData request = new NuiPageRequestData();
				request.setPageSize(1000);
				request.setPageIndex(0);
				map = queryImportTempTable(instance.getInstanceUuid(), request);
				if(map != null && map.getData() != null && map.getData().size() > 0){
					List<Map> list = map.getData();
					List<Map> list_out = new ArrayList<Map>();
					
					for(int i=0;i<list.size();i++){
						Map m = new HashMap();
						Map m_out = new HashMap();
						m = list.get(i);
						Set<String> set = m.keySet();
						for(String s:set){
							//System.out.println(s+"="+m.get(s));
							m_out.put(StringUtil.buildFieldName(s), m.get(s));
						}
						list_out.add(m_out);
					}
					map.setData(list_out);
				}
			}else{
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}