package com.ctop.fw.sys.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ctop.fw.common.excelexport.ExcelUtil;
import com.ctop.fw.common.model.FileDto;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.entity.SysExcelImport;
import com.ctop.fw.sys.entity.SysExcelImportInstance;
import com.ctop.fw.sys.service.SysExcelImportInstanceService;
import com.ctop.fw.sys.service.SysExcelImportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/rest/sys/sysExcelImportInstance")
public class SysExcelImportInstanceAction {
	private static Logger logger = LoggerFactory.getLogger(SysExcelImportInstanceAction.class);

	@Autowired
	SysExcelImportInstanceService sysExcelImportInstanceService;
	@Autowired
	SysExcelImportService sysExcelImportService;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SysExcelImportInstance getSysExcelImportInstance(@RequestParam("instanceUuid") String instanceUuid) {
		return sysExcelImportInstanceService.getById(instanceUuid);
	}
 
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("file") MultipartFile file) throws JsonProcessingException {
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
//			System.out.println(file.getBytes());
//			file.transferTo(temp);
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
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(fileDto);
	}
	
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public SysExcelImportInstance importExcel(@RequestBody SysExcelImportInstance instance) {
		logger.debug("导入文件.");
		instance.setAccountUuid(UserContextUtil.getAccountUuid());
		instance.setCompanyUuid(UserContextUtil.getCompanyUuid());
		Workbook wb = ExcelUtil.createWorkBook(instance.getFilePath());
		instance = sysExcelImportInstanceService.importExcel2TempTable(instance, instance.buildExceTableIterable(wb), new HashMap<String, Object>());  
		ExcelUtil.close(wb);
		return instance;
	}
	 
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryTempTable", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<Map> queryImportTempTable(@RequestParam("instanceUuid") String instanceUuid, @RequestBody NuiPageRequestData request) {
		return sysExcelImportInstanceService.queryImportTempTable(instanceUuid, request);
	}
	
	@RequestMapping(value = "/queryColumnList", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public Map<String,Object> queryListPage(@RequestParam("instanceUuid") String instanceUuid,@RequestParam("tempName") String tempName) {
		return sysExcelImportInstanceService.queryColumnList(instanceUuid,tempName);
	}
	
	@RequestMapping(value = "/processTempTable", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public SysExcelImportInstance processTempTable(@RequestBody SysExcelImportInstance instance) {
		return sysExcelImportInstanceService.processTempTable(instance, new HashMap<String, Object>());
	}
	
	@RequestMapping(value = "/transferTempTable", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public SysExcelImportInstance transferTempTable(@RequestBody SysExcelImportInstance instance) {
		return sysExcelImportInstanceService.transferTempTable(instance, new HashMap<String, Object>());
	}
	
	@RequestMapping(value = "/deleteInvalidRow", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public SysExcelImportInstance deleteInvalidRow(@RequestBody SysExcelImportInstance instance) {
		return sysExcelImportInstanceService.deleteInvalidRow(instance, instance.getParamsMap());
	} 
	
	@RequestMapping(value = "/uploadO2oFile", method = RequestMethod.POST)
	public PageResponseData<Map> uploadO2oFile(@RequestParam("file") MultipartFile file){
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
				SysExcelImport sysExcelImport = this.sysExcelImportService.findByImportCode("O2O_MR_MATERIAL_REQUIRMENTS");
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
				instance = sysExcelImportInstanceService.importExcel2TempTable(instance, instance.buildExceTableIterable(wb), new HashMap<String, Object>());  
				ExcelUtil.close(wb);
				NuiPageRequestData request = new NuiPageRequestData();
				request.setPageSize(1000);
				request.setPageIndex(0);
				map = sysExcelImportInstanceService.queryImportTempTable(instance.getInstanceUuid(), request);
				if(map != null && map.getData() != null && map.getData().size() > 0){
					List<Map> list = map.getData();
					List<Map> list_out = new ArrayList<Map>();
					
					for(int i=0;i<list.size();i++){
						Map m = new HashMap();
						Map m_out = new HashMap();
						m = list.get(i);
						Set<String> set = m.keySet();
						for(String s:set){
							if("MRD_DATE".equals(s)){
								String mrdDate="";
								if(m.get(s)!=null){
									String tmp = m.get(s).toString();
									if(tmp.length()>9){
										mrdDate = tmp.substring(0, 10);
									}else{
										mrdDate = tmp;
									}									
								}
								m_out.put(StringUtil.buildFieldName(s), mrdDate);
							}else if("MRD_UUID".equals(s)){
								
							}else{
								m_out.put(StringUtil.buildFieldName(s), m.get(s));
							}
							
						}
						if(m_out.get("importMessage") == null){
							//错误数据   todo
						}else{
							//没验证的数据
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
	
	
	/**
	 * 样件零件上传
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadO2oIePartFile", method = RequestMethod.POST)
	public PageResponseData<Map> uploadO2oIePart(@RequestParam("file") MultipartFile file){
		 return  sysExcelImportInstanceService.uploadO2oIePart(file);
	}
	
}