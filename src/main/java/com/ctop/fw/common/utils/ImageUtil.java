package com.ctop.fw.common.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static void zoomImage(String imagePath, int newWidth, int newHeight) {
        File imgFile = new File(imagePath);     
        BufferedImage src;
		try {
			src = javax.imageio.ImageIO.read(imgFile);
			int width = src.getWidth();
			int height = src.getHeight();     
	        BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);     
	        // 绘制缩小后的图     
	        tag.getGraphics().drawImage(src, 0, 0, newWidth, newHeight, null);     
	        FileOutputStream out = new FileOutputStream(imagePath);     
	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);     
	        encoder.encode(tag);    
	        out.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param imagePath
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void cropImage(String imagePath, int x, int y, int width, int height) {
		Image img;     
        ImageFilter cropFilter;     
        // 读取源图像     
        BufferedImage src;
		try {
			src = ImageIO.read(new File(imagePath));
			cropFilter = new CropImageFilter(x, y, width, height);
			img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(src.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
            Graphics g = tag.getGraphics();     
            g.drawImage(img, 0, 0, null); // 绘制小图     
            g.dispose();     
            // 输出为文件     
            ImageIO.write(tag, "JPEG", new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
