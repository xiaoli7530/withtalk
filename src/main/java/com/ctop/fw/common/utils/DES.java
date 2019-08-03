package com.ctop.fw.common.utils;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {

	private final static String DES = "DES"; 	  
	private static DES instance;	
	
	private DES(){
		
	}
	
	public static DES getInstance(){
		if (instance == null) {  
	        instance = new DES();  
	    }
		return instance;
	}
	
	/** 
	   * 解密 ，输入String密文，返回String明文，方法中实际是
	   * 将密文和key转换成byte[]作为参数调用本类中的字节数组加密方法
	   * @param data 
	   * @return 
	   * @throws Exception 
	   */ 

	public String strKey(){
		String strKey = "1784xiff--ehs";
		return strKey;
	}
	
	public String decrypt(String data){ 
	    //先定义一个明文字符串，赋值为空  
		String decStr = "";
		  try { 
			  //解密，实际上是调用的字节串解密的方法。先要将字符串型的密文和key转换成字节数组
			  decStr = new String(decrypt(hex2byte(data.getBytes("UTF-8")),
					  this.strKey().getBytes()));
	      }catch(Exception e) { 
	    	  decStr = "decrypt false";
	      } 
	      return decStr;
	} 

	  /** 

	   * 加密，输入明文信息，返回密文 。方法中实际是将明文和key转换成byte[]
	   * 作为参数调用类中的字节数组解密方法。
	   * @param massage 
	   * @return 
	   * @throws Exception 
	   */ 
	public String encrypt(String massage){ 
		 //先定义一个密文字符串，赋值为空
		  String encStr = "";
	      try { 
	    	  //加密，调用字节串加密方法。先将key和密文字符串转换成字节数组
	    	  encStr = encrypt(massage.getBytes("UTF-8"),this.strKey().getBytes());
	      }catch(Exception e) { 
	    	  encStr = "encrypt false";
	      } 
	      return encStr; 
	}
	
	
	/** 
	 * 字节数组加密，输入字节数组型的数据源和密钥，返回String型的密文。 
	 * @param src 数据源 
	 * @param key 密钥，长度必须是8的倍数 
	 * @return  返回加密后的数据 
	 * @throws Exception 
	 */ 
	private String encrypt(byte[] src, byte[] key)throws Exception { 
			byte[] dencryptedData = null;
			String result = null;
	        //DES算法要求有一个可信任的随机数源 
	        SecureRandom sr = new SecureRandom(); 
	        // 从原始密匙数据创建DESKeySpec对象 
	        DESKeySpec dks = new DESKeySpec(key); 
	        // 创建一个密匙工厂，然后用它把DESKeySpec转换成 
	        // 一个SecretKey对象 
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); 
	        SecretKey securekey = keyFactory.generateSecret(dks); 
	        // Cipher对象实际完成加密操作 
	        Cipher cipher = Cipher.getInstance(DES); 
	        // 用密匙初始化Cipher对象 
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr); 
	        // 现在，获取数据并加密 
	        // 正式执行加密操作 
	        dencryptedData=cipher.doFinal(src); 
	        result = byte2hex(dencryptedData);
	        return result; 
	 } 
     /** 
     * 解密 ，输入字节数组型的密文和密钥，返回String型的明文
     * @param src 数据源 
     * @param key 密钥，长度必须是8的倍数 
     * @return   返回解密后的原始数据 
     * @throws Exception 
     */ 
     private  String decrypt(byte[] src, byte[] key)throws Exception { 
    	byte[] dencryptedData = null;
 		String result = null;
        // DES算法要求有一个可信任的随机数源 
        SecureRandom sr = new SecureRandom(); 
        // 从原始密匙数据创建一个DESKeySpec对象 
        DESKeySpec dks = new DESKeySpec(key); 
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 
        // 一个SecretKey对象 
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); 
        SecretKey securekey = keyFactory.generateSecret(dks); 
        // Cipher对象实际完成解密操作 
        Cipher cipher = Cipher.getInstance(DES); 
        // 用密匙初始化Cipher对象 
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr); 
        // 现在，获取数据并解密 
        // 正式执行解密操作 
        dencryptedData=cipher.doFinal(src); 
        result = new String(dencryptedData, "UTF-8");
        return result; 
     } 

	   

	/** 
	 * 二行制转字符串 
	 * @param b 
	 * @return 
	 */ 
	  private  String byte2hex(byte[] b) { 
	        String hs = ""; 
	        String stmp = ""; 
	        for (int n = 0; n < b.length; n++) { 
	            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF)); 
	            if (stmp.length() == 1) 
	                hs = hs + "0" + stmp; 
	            else 
	                hs = hs + stmp; 
	        } 
	        return hs.toUpperCase(); 
	   } 
	  /**
	   * 字符串转二进制
	   * @param b
	   * @return
	   */
	  private  byte[] hex2byte(byte[] b) { 
	       if((b.length%2)!=0) {
	    	   throw new IllegalArgumentException("长度不是偶数"); 
	       }
	       byte[] b2 = new byte[b.length/2]; 
	       for (int n = 0; n < b.length; n+=2) { 
	          String item = new String(b,n,2); 
	          b2[n/2] = (byte)Integer.parseInt(item,16); 
	       } 
	       return b2; 
	  }
	  
	public static void main(String[] args) {
		DES des = new DES();
		String a = des.encrypt("1234456");
		System.out.println(a);
		
		System.out.println(des.decrypt(a));
	}

}
