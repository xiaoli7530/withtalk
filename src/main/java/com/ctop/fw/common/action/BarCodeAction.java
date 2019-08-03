package com.ctop.fw.common.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctop.fw.common.service.BarCodeService;
import com.ctop.fw.common.utils.PdfUtil;

@RestController
@RequestMapping(path = "/rest/common/barcode")
public class BarCodeAction {
	
	@Autowired
	BarCodeService barCodeService;
	 
	/**
	 * 
	 * 功能说明:二维码生成
	 * 创建人:cinny
	 * 最后修改日期:2016年10月11日
	 * @param code
	 * @param width
	 * @param height
	 * @param response
	 * void
	 */
	@RequestMapping(value = "/generatorQrcode", method = RequestMethod.GET)
	public void generatorQrcode(@RequestParam("code") String code,
			@RequestParam("width") Integer width,
			@RequestParam("height") Integer height,
			HttpServletResponse response){
		try {
			width = width == null?300:width;
			height = height == null?300:height;
			//String orgCode = new String(Base64.decodeBase64(code),"UTF-8");
			String orgCode = code;
			barCodeService.createBarCode_QR_CODE(orgCode, width, height, response.getOutputStream());
			response.flushBuffer();
			System.out.println("请求生成条码：" + code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 功能说明:条码生成
	 * 创建人:cinny
	 * 最后修改日期:2016年10月11日
	 * @param code
	 * @param width
	 * @param height
	 * @param response
	 * void
	 */
	@RequestMapping(value = "/generatorCode128", method = RequestMethod.GET)
	public void generatorCode128(@RequestParam("code") String code,
			@RequestParam("width") Integer width,
			@RequestParam("height") Integer height,
			HttpServletResponse response){
		try {
			width=width==null?200:width;
			height=height==null?50:height;
			System.out.println(response.getBufferSize());
			barCodeService.createBarCode_CODE_128(code, width, height, response.getOutputStream());
			response.flushBuffer();
			System.out.println("请求生成条码：" + code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@RequestMapping(value = "/genarate", method = RequestMethod.GET)
	@ResponseBody
	public void genarate(HttpServletRequest request) {
		PdfUtil pdfUtil = new PdfUtil();
		FileOutputStream os = null;
		Map<String, Object> map = new HashMap<String, Object>();
		String prefixPath = "http://" + request.getServerName() + ":" + request.getServerPort();
		map.put("prefixPath",prefixPath);
		try {
			os = new FileOutputStream(new File("d://dy.pdf"));
			pdfUtil.generatePdfWriter(map, os, "inventory_out.tpl");
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
