package com.ctop.base.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.ListUtil;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.base.entity.BaseAttachment;
import com.ctop.base.repository.BaseAttachmentRepository;
import com.ctop.base.dto.BaseAttachmentDto;
import com.ctop.base.entity.BaseAttachment;


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
public class BaseAttachmentService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseAttachmentRepository baseAttachmentRepository; 
	@Value("${attachment.upload.diretory}")
	private String uploadDir; 
	
	@Transactional(readOnly=true)
	public BaseAttachmentDto getById(String id) {
		BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(id);
		return CommonAssembler.assemble(baseAttachment, BaseAttachmentDto.class);
	}
	
	@Transactional
	public BaseAttachmentDto addBaseAttachment(BaseAttachmentDto baseAttachmentDto) {
		BaseAttachment baseAttachment = CommonAssembler.assemble(baseAttachmentDto, BaseAttachment.class);
		baseAttachment = this.baseAttachmentRepository.save(baseAttachment);
		return CommonAssembler.assemble(baseAttachment, BaseAttachmentDto.class);
	} 

	@Transactional
	public BaseAttachmentDto updateBaseAttachment(BaseAttachmentDto baseAttachmentDto) {
		BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(baseAttachmentDto.getAttachmentUuid());
		CommonAssembler.assemble(baseAttachmentDto, baseAttachment);
		baseAttachment = this.baseAttachmentRepository.save(baseAttachment);
		return CommonAssembler.assemble(baseAttachment, BaseAttachmentDto.class);
	}
	
	@Transactional
	public BaseAttachmentDto updateBaseAttachmentByRefUuid(BaseAttachmentDto baseAttachmentDto) {
		BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(baseAttachmentDto.getAttachmentUuid());
		baseAttachment.setRefUuid(baseAttachmentDto.getRefUuid());
		baseAttachment = this.baseAttachmentRepository.save(baseAttachment);
		return CommonAssembler.assemble(baseAttachment, BaseAttachmentDto.class);
	}
	
	/**
	 * 根据一级模板的图片。复制一张二级模板的图片
	 * @param picUuid
	 * @param pbtpUuid
	 */
	public BaseAttachmentDto genAttchByOldId(String id,String refUuid){
		if(StringUtil.isNotEmpty(id)){
			BaseAttachmentDto attch = this.getById(id);
			attch.setRefUuid(refUuid);
			attch.setAttachmentUuid("");
			return this.addBaseAttachment(attch);
		}
		return new BaseAttachmentDto();
	}
	
	@Transactional
	public void deleteBaseAttachment(String id) {
		BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(id);
		baseAttachment.setIsActive("N");
		this.baseAttachmentRepository.save(baseAttachment);
	}
		
	@Transactional
	public void deleteBaseAttachments(List<String> attachmentUuids) {
		Iterable<BaseAttachment> baseAttachments = this.baseAttachmentRepository.findAll(attachmentUuids);
		Iterator<BaseAttachment> it = baseAttachments.iterator();
		while(it.hasNext()) {
			BaseAttachment baseAttachment = it.next();
			baseAttachment.setIsActive("N");
			this.baseAttachmentRepository.save(baseAttachment);
		}
	} 

 
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<BaseAttachmentDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseAttachment.class, "t");
		sql.append(" from BASE_ATTACHMENT t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseAttachmentDto.class);
	}

	/**
	 * 批量上传文件
	 * @param files
	 * @return
	 */
	@Transactional
	public List<BaseAttachment> uploadFiles(MultipartFile[] files) {
		List<BaseAttachment> fileEntityList = new ArrayList<BaseAttachment>();
		for (MultipartFile file : files) {
			fileEntityList.add(this.uploadFile(file));
		}
		return fileEntityList;
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @return
	 */
	@Transactional
	public BaseAttachment uploadFile(MultipartFile file) {
		BaseAttachment fileEntity = new BaseAttachment();
		String baseDir = this.uploadDir;
		Calendar calender = Calendar.getInstance();
		String dir = calender.get(Calendar.YEAR) + "/" + (calender.get(Calendar.MONTH)+1);// month是从0开始的
		String orgFileName = file.getOriginalFilename();
		try{
			orgFileName = new String(orgFileName.getBytes(), "UTF-8");
		}catch(Exception e) {
			e.printStackTrace();
		}

		String newFileName = replaceFileName(orgFileName);
		String suffix = orgFileName.substring(orgFileName.lastIndexOf("."),orgFileName.length());
		String fileName = UUID.randomUUID().toString().replace("-", "")+ suffix;
		try {
			File dirFile = new File(baseDir + "/" + dir);
			if(!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File localFile = new File(baseDir + "/" + dir + "/" + fileName);
			FileUtils.copyInputStreamToFile(file.getInputStream(), localFile);
			
			fileEntity.setContentType(file.getContentType());
			fileEntity.setName(newFileName);
			fileEntity.setContentSize(file.getSize());
			fileEntity.setUrl(localFile.getAbsolutePath());
			fileEntity.setRefType("ppobuild");
			this.baseAttachmentRepository.save(fileEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileEntity;
	}
	
	public String replaceFileName(String oldFileName){
		String newFileName = "";
		if(StringUtil.isNotEmpty(oldFileName)){
			oldFileName = oldFileName.replaceAll("\\\\", "/");
			newFileName  = oldFileName.substring(oldFileName.lastIndexOf("/")+1);
		}
		return newFileName;
	}
	
	/**
	 * 对应的业务表只对应一个附件的保存方法
	 * @param refUuid
	 * @param attachmentUuid
	 */
	public void saveSingleAttachment4Biz(String refUuid, String attachmentUuid) {
		// 1、若此业务表已经存在对应的附件，则删除
		BaseAttachment existence = new BaseAttachment();
		existence.setIsActive("Y");
		existence.setRefUuid(refUuid);
		List<BaseAttachment> existenceList = this.baseAttachmentRepository.findAll(Example.of(existence));
		if(existenceList != null && existenceList.size() > 0) {
			ListUtil.setFieldValue(existenceList, "isActive", "N");
			this.baseAttachmentRepository.save(existenceList);
		}
		 
		// 2、把附件和业务表关联
		if(StringUtil.isNotEmpty(attachmentUuid)) {
			BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(attachmentUuid);
			if(baseAttachment != null) {
				baseAttachment.setRefUuid(refUuid);
				baseAttachment.setIsActive("Y");
				this.baseAttachmentRepository.save(baseAttachment);
			}
		}
	}
	
	/**
	 * 对应的业务表只对应多个附件的保存方法
	 * @param refUuid
	 * @param attachmentUuid
	 */
	public void saveMultipleAttachment4Biz(String refUuid, String attachmentUuids) {
		// 1、若此业务表已经存在对应的附件，则删除
		BaseAttachment existence = new BaseAttachment();
		existence.setIsActive("Y");
		existence.setRefUuid(refUuid);
		List<BaseAttachment> existenceList = this.baseAttachmentRepository.findAll(Example.of(existence));
		if(existenceList != null && existenceList.size() > 0) {
			ListUtil.setFieldValue(existenceList, "isActive", "N");
			this.baseAttachmentRepository.save(existenceList);
		}
		 
		// 2、把附件和业务表关联
		if(StringUtil.isNotEmpty(attachmentUuids)) {
			String[] attachmentUuidArr = attachmentUuids.split(",");
			for(int i = 0; i < attachmentUuidArr.length; i++) {
				BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(attachmentUuidArr[i]);
				if(baseAttachment != null) {
					baseAttachment.setRefUuid(refUuid);
					baseAttachment.setIsActive("Y");
					this.baseAttachmentRepository.saveAndFlush(baseAttachment);
				}
			}
		}
	}
	
	/**
	 * 给对应的业务表增附件
	 * @param refUuid
	 * @param attachmentUuid
	 */
	public void appendMultipleAttachment4Biz(String refUuid, String attachmentUuids) {
		// 把附件和业务表关联
		if(StringUtil.isNotEmpty(attachmentUuids)) {
			String[] attachmentUuidArr = attachmentUuids.split(",");
			for(int i = 0; i < attachmentUuidArr.length; i++) {
				BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(attachmentUuidArr[i]);
				if(baseAttachment != null) {
					baseAttachment.setRefUuid(refUuid);
					baseAttachment.setIsActive("Y");
					this.baseAttachmentRepository.saveAndFlush(baseAttachment);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param refUuid
	 * @return
	 */
	public BaseAttachment findByRefUuid(String refUuid){
		BaseAttachment existence = new BaseAttachment();
		existence.setIsActive("Y");
		existence.setRefUuid(refUuid);
		List<BaseAttachment> existenceList = this.baseAttachmentRepository.findAll(Example.of(existence));
		if(!ListUtil.isNullOrEmpty(existenceList)){
			existence = existenceList.get(0);
		}
		return existence;
	}
	
	public List<BaseAttachmentDto> findListByRefUuid(String refUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseAttachment.class, "t");
		sql.append(" from BASE_ATTACHMENT t where t.IS_ACTIVE='Y' ");
		sql.andEqualNotNull("t.ref_uuid", refUuid);
		return sql.query(entityManager, BaseAttachmentDto.class);
	}
	
	/**
	 * 删除附件
	 * @param attachmentUuid
	 */
	public void delFile(String attachmentUuid) {
		BaseAttachment baseAttachment = this.baseAttachmentRepository.findOne(attachmentUuid);
		baseAttachment.setIsActive("N");
		this.baseAttachmentRepository.save(baseAttachment);
		//删除文件
		File localFile = new File(baseAttachment.getUrl());
		localFile.delete();
	}
	
}

