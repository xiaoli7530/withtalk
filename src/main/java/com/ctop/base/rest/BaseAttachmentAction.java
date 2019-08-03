package com.ctop.base.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctop.base.dto.BaseAttachmentDto;
import com.ctop.base.entity.BaseAttachment;
import com.ctop.base.service.BaseAttachmentService;
import com.ctop.fw.common.model.NuiPageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.StringUtil;

@RestController
@RequestMapping(path = "/rest/base/baseAttachment")
public class BaseAttachmentAction {
	private static Logger logger = LoggerFactory.getLogger(BaseAttachmentAction.class);

	@Autowired
	BaseAttachmentService baseAttachmentService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public PageResponseData<BaseAttachmentDto> getBaseAttachmentsList(@RequestBody NuiPageRequestData request) {
		return baseAttachmentService.sqlPageQuery(request);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public BaseAttachmentDto getBaseAttachment(@RequestParam("attachmentUuid") String attachmentUuid) {
		return baseAttachmentService.getById(attachmentUuid);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public BaseAttachmentDto createBaseAttachment(@RequestBody BaseAttachmentDto baseAttachmentDto) {
		return this.baseAttachmentService.addBaseAttachment(baseAttachmentDto);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseAttachmentDto updateBaseAttachment(@RequestBody BaseAttachmentDto baseAttachmentDto) {
		return this.baseAttachmentService.updateBaseAttachment(baseAttachmentDto);
	}
	
	@RequestMapping(value = "/updateByRefUuid", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public BaseAttachmentDto updateByRefUuid(@RequestBody BaseAttachmentDto baseAttachmentDto) {
		return this.baseAttachmentService.updateBaseAttachmentByRefUuid(baseAttachmentDto);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseAttachment(@RequestParam("attachmentUuid") String attachmentUuid) {
		baseAttachmentService.deleteBaseAttachment(attachmentUuid);
	}
		
	@RequestMapping(value = "/deleteList", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteBaseAttachments(@RequestBody List<String> attachmentUuids) {
		baseAttachmentService.deleteBaseAttachments(attachmentUuids);
	}
	@RequestMapping(value = "/downloadFiles", method = RequestMethod.GET)
	public void downloadTemplate(@RequestParam("attachmentUuid") String attachmentUuid, HttpServletRequest request, HttpServletResponse response) {
		BaseAttachmentDto files = baseAttachmentService.getById(attachmentUuid);
    	if(files==null){
    	}
	   try {	
           // 取得文件名。
           String filename = files.getName();
           // 以流的形式下载文件。
           InputStream fis = new BufferedInputStream(new FileInputStream(files.getUrl()));
           byte[] buffer = new byte[fis.available()];
           fis.read(buffer);
           fis.close();
           // 清空response
           response.reset();
           String userAgent = request.getHeader("User-Agent");
   		   String name = StringUtil.encodeDownloadName(filename, userAgent);
   		   response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", name));
           // 设置response的Header
       /*    response.addHeader("Content-Disposition", "attachment;filename=" + new String("车辆试验用途情况说明样本.docx".getBytes(), "utf-8"));
           response.addHeader("Content-Length", "" + file.length());*/
           //设置文件名中文编码
   /*        response.setHeader("content-disposition", "attachment;filename="  
                   + URLEncoder.encode(filename, "UTF-8")); */
           OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
           response.setContentType("application/octet-stream");
           toClient.write(buffer);
           toClient.flush();
           toClient.close();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        throw new BusinessException(ex.getMessage());
	    }
	}
	
	@RequestMapping(value = "/uploadFile")
	@ResponseBody
	public ResponseEntity<BaseAttachment> uploadFile(@RequestParam("file") MultipartFile file) {
		BaseAttachment dto = baseAttachmentService.uploadFile(file);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "text/plain;charset=UTF-8");
		return new ResponseEntity<BaseAttachment>(dto, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/uploadFiles")
	@ResponseBody
	public ResponseEntity<List<BaseAttachment>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
		List<BaseAttachment> dto = baseAttachmentService.uploadFiles(files);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Disposition", "");
		responseHeaders.set("Content-Type", "text/plain;charset=UTF-8");
		return new ResponseEntity<List<BaseAttachment>>(dto, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/delFile", method = RequestMethod.GET)
	@ResponseBody
	public void delFile(@RequestParam("attachmentUuid") String attachmentUuid) {
		baseAttachmentService.delFile(attachmentUuid);
	}
}
