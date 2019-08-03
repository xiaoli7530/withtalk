package com.ctop.fw.common.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;


/**
 * 
* <pre>
* 功能说明：PDF文件发送给打印机打印
* 示例程序如下：	
* 版本：0.1
* @author  cinny
* 2016年9月9日
 */

public class PrintUtil implements Printable {
	//PDF文件
	private PDFFile pdfFile = null;
	//PDF文件名
	private String fileName = null;
	
	private File file = null;
	/**
	 * 
	 * @param Graphic指明打印的图形环境
	 * 
	 * @param PageFormat指明打印页格式
	 *            （页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）
	 * 
	 * @param pageIndex指明页号
	 **/

	public int print(Graphics gra, PageFormat pf, int pageIndex)
			throws PrinterException {

		System.out.println("pageIndex=" + pageIndex);
		Component component = null;
		// 转换成Graphics2D
		Graphics2D g2 = (Graphics2D) gra;
		// 设置打印颜色为黑色
		g2.setColor(Color.black);
		// 打印起点坐标
		double x =pf.getImageableX();
		double y =pf.getImageableY();
		// 设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
		// Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput
		Font font = new Font("新宋体", Font.PLAIN, 9);
		g2.setFont(font);// 设置字体
		// BasicStroke bs_3=new BasicStroke(0.5f);
		float[] dash1 = { 2.0f };
		int result = genarateImg(pageIndex);
		// 设置打印线的属性。
		// 1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量
		g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));
		// g2.setStroke(bs_3);//设置线宽
		float heigth = font.getSize2D();// 字体高度
		System.out.println("x=" + x);
		// -1- 用Graphics2D直接输出
		// 首字符的基线(右下部)位于用户空间中的 (x, y) 位置处
		// g2.drawLine(10,10,200,300);
		Image src = Toolkit.getDefaultToolkit().getImage("D:/Image" + pageIndex + ".png");
		//获得图片高度宽度
		int img_Height = src.getHeight(component);
		int img_width = src.getWidth(component);
		g2.drawImage(src,(int)x,(int)y,500,800,component);
		System.out.println("pageIndex:"+pageIndex+",result:"+result + "img_Height:" + img_Height + "  img_width:" + img_width);
		//删除文件
		File imgFile = new File("D:/Image" + pageIndex + ".png");
		if(imgFile.exists()) {
			imgFile.delete();
		}
		return result;

	}
	
	/**
	 * 
	 * 功能说明:设置PDF文件名
	 * 创建人:cinny
	 * 最后修改日期:2016年9月12日
	 * @param fileName
	 * void
	 */
	public void pintFileSet(String fileName) {
		try {
			file = new File(fileName);
			// set up the PDF reading
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
					channel.size());
			pdfFile = new PDFFile(buf);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public int genarateImg(int pageIndex) {
		try {
			if (pageIndex < pdfFile.getNumPages()) {
				// 设置将第pagen也生成png图片
				PDFPage page = pdfFile.getPage(pageIndex + 1);
				if (page == null) {
					return NO_SUCH_PAGE;
				}
				// create and configure a graphics object
				int width = (int) page.getBBox().getWidth();
				int height = (int) page.getBBox().getHeight();
				BufferedImage img = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = img.createGraphics();
				// img.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				// RenderingHints.VALUE_ANTIALIAS_ON);
				// do the actual drawing
				PDFRenderer renderer = new PDFRenderer(page, g2, new Rectangle(
						0, 0, width, height), null, Color.WHITE);
				try {
					page.waitForFinish();
				} catch (Exception e) {
					e.printStackTrace();
				}
				renderer.run();
				// g2.drawImage(img, null, 500*i, 500*i);
				ImageIO.write(img, "png", new File("D:/Image" + pageIndex
						+ ".png"));
				g2.dispose();
				return PAGE_EXISTS;
			} else {
				return NO_SUCH_PAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NO_SUCH_PAGE;
	}
	
	public void printJobSet(String fileName) {
		//设置PDF文件
		pintFileSet(fileName);
		// 通俗理解就是书、文档
		Book book = new Book();
		// 设置成竖打
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT);

		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
		Paper paper = new Paper();
		
		paper.setSize(590, 840);// 纸张大小
		paper.setImageableArea(10, 10, 590, 840);// A4(595 X
												// 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
		pf.setPaper(paper);

		// 把 PageFormat 和 Printable 添加到书中，组成一个页面
		book.append(this, pf,pdfFile.getNumPages());
		
		// 获取打印服务对象
		PrinterJob job = PrinterJob.getPrinterJob();

		// 设置打印类
		job.setPageable(book);

		try {
			// 可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
			// boolean a=job.printDialog();
			// if(a)
			// {
			job.print();
			// }else{
			// job.cancel();
			// }
			if(file.exists()) {
				file.delete();
			}
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		PrintUtil printDemo = new PrintUtil();
		printDemo.printJobSet("d:/dy.pdf");
		

	}

}
