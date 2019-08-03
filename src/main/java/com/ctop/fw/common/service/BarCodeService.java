package com.ctop.fw.common.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.xmlgraphics.util.UnitConv;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.ctop.fw.common.utils.BusinessException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
@Transactional
public class BarCodeService {
	private static final int BLACK = 0xff000000;  
    private static final int WHITE = 0xFFFFFFFF;
    private static final String CODE = "utf-8"; 
    
	public static void main(String[] args) throws FileNotFoundException {
		BarCodeService cpc = new BarCodeService();
		FileOutputStream outputFile = new FileOutputStream(new File("c:/temp/" + System.currentTimeMillis() + ".jpg"));
		cpc.createBarCode_CODE_128("11112342312",200,50,outputFile);
	}
	
	/**
	 * 
	 * 功能说明:创建二维条码
	 * 创建人:cinny
	 * 最后修改日期:2016年9月8日
	 * @param text
	 * @param width
	 * @param height
	 * @param outputStream
	 * void
	 */
	public void createBarCode_QR_CODE(String text,int width,int height,OutputStream outputStream) {
		//二维码图片格式
		String imgFormat = "jpg";
		//设置二维码参数
		HashMap hints = new HashMap();
		//内容所使用的编码
		hints.put(EncodeHintType.CHARACTER_SET, CODE);
		hints.put(EncodeHintType.MARGIN, 0);//白边设置
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text,BarcodeFormat.QR_CODE, width, height,hints);
			MatrixToImageWriter.writeToStream(bitMatrix, imgFormat, outputStream);
			outputStream.flush();
		} catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException("生成二维码异常");
		}
		
	}
	
	/**
	 * 
	 * 功能说明:生成一维码（条码）
	 * 创建人:cinny
	 * 最后修改日期:2016年10月11日
	 * @param text
	 * @param width
	 * @param height
	 * @param outputStream
	 * void
	 */
	public void createBarCode_CODE_128(String text,int width,int height,OutputStream outputStream) {
		//条码图片格式
		String imgFormat = "jpg";
		//设置条码参数
		HashMap hints = new HashMap();
		
		//内容所使用的编码
		hints.put(EncodeHintType.CHARACTER_SET, CODE);
		hints.put(EncodeHintType.MARGIN, 0);//白边设置
//		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text,BarcodeFormat.CODE_128, width, height,hints);
			MatrixToImageWriter.writeToStream(bitMatrix, imgFormat, outputStream);
			outputStream.flush();
		} catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException("生成条码异常");
		}
//		generate(text,outputStream);
	}
	
	
	
	
	   public static void generate(String msg, OutputStream ous) {
	        if (StringUtils.isEmpty(msg) || ous == null) {
	            return;
	        }
	        Code128Bean bean = new Code128Bean();
	        // 精细度
	        final int dpi = 150;
	        // module宽度
	        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);
	 
	        // 配置对象
	        bean.setModuleWidth(moduleWidth);
//	        bean.setWideFactor(3);
	        bean.doQuietZone(false);
	        String format = "image/png";
	        try {
	            // 输出到流
	        	BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
	                    BufferedImage.TYPE_BYTE_BINARY, false, 0);
	            // 生成条形码
	            bean.generateBarcode(canvas, msg);
	            // 结束绘制
	            canvas.finish();
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	
}
