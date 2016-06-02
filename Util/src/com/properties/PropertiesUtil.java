package com.properties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertiesUtil {
    
	/**
	 * //根据Key读取Value
	 * @param filePath
	 * @param key
	 * @return
	 */
    public static String getValueByKey(String filePath, String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));  
            pps.load(in);
            String value = pps.getProperty(key);
           return value;
           
       }catch (IOException e) {
           e.printStackTrace();
           return null;
       }
   }
   
    
    /**
     * //读取Properties的全部信息
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Map<String,String> getAllProperties(String filePath) {
        Properties pps = new Properties();
        try {
        	 InputStream in = new BufferedInputStream(new FileInputStream(filePath));
             pps.load(in);
             Enumeration en = pps.propertyNames(); //得到配置文件的名字
             Map<String, String> m = new HashMap<>();
             while(en.hasMoreElements()) {
                 String strKey = (String) en.nextElement();
                 String strValue = pps.getProperty(strKey);
                 m.put(strKey, strValue);
             }
             return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
       return null;
    }
   
   
    /**
     * //写入Properties信息
     * @param filePath
     * @param pKey
     * @param pValue
     * @throws IOException
     */
   public static void writeProperties (String filePath, String pKey, String pValue)  {
       Properties pps = new Properties();
       try {
    	   InputStream in = new BufferedInputStream(new FileInputStream(filePath));
           //从输入流中读取属性列表（键和元素对） 
           pps.load(in);
           //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。  
           //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
           OutputStream out = new FileOutputStream(filePath);
           pps.setProperty(pKey, pValue);
           //以适合使用 load 方法加载到 Properties 表中的格式，  
           //将此 Properties 表中的属性列表（键和元素对）写入输出流  
           pps.store(out, "Update " + pKey + " name");
		} catch (Exception e) {
			e.printStackTrace();
		}
       
   }
   
   public static void main(String [] args) throws IOException{
	   //   /bin/test.properties
	   String s =PropertiesUtil.class.getClassLoader().getResource("PI.properties").getPath();
	   //s = s.substring(6);
      // System.out.println(s);
      // String value = getValueByKey(s, "long");
       //System.out.println(value);
       //getAllProperties("Test.properties");
	   
	   writeProperties(s,"aaaa", "212");
   }
}
