package com.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExportExcel {
	private final static Log log = LogFactory.getLog(ExportExcel.class);
	public static void main(String[] args) {
		List<Student> list = new ArrayList<Student>();  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");  
  
		try {
			 Student user1 = new Student(1, "张三", 16, df.parse("1997-03-12"));
		     Student user2 = new Student(2, "李四", 17, df.parse("1996-08-12"));  
		     Student user3 = new Student(3, "王五", 26, df.parse("1985-11-12"));  
		     list.add(user1);  
		     list.add(user2);  
		     list.add(user3);
		     String[] headers={"学号","姓名","年龄","日期"};
		     ExportExcel.exportExcel(list, headers, "sheet1", "E:/stu.xls",null);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	}
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 
	 * @param data 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象
	 * @param headers 表格属性列名数组
	 * @param sheetName Excel文件中的sheet名称  
	 * @param path 输出excel(xls)的路径 包含excel文件名
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	public static <T> void exportExcel(Collection<T> data,String[] headers,String sheetName,String path,String pattern){
		 // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(sheetName);  
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        
        // 第三步 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        
        // 生成并设置另一个样式
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = wb.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        
        // 第四步 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
        	HSSFCell cell = row.createCell(i) ; 
        	cell.setCellStyle(style); 
        	HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);  
        }

        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
        Iterator<T> it = data.iterator();
        int index=0;
        while(it.hasNext()){
        	index++;
        	row = sheet.createRow(index);
            T t = (T) it.next();
          //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for(int i=0;i<fields.length;i++){
            	HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                	Class<?> tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName);
                    Object value = getMethod.invoke(t);
                    String textValue = null;
                    log.info(value.toString());
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        if(pattern==null||pattern.equals("")){
                        	pattern = "yyyy-mm-dd";
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                         textValue = sdf.format(date);
                     } else{
                    	 textValue = value.toString();
                     }
                    if(textValue!=null){
                        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$"); 
                        Matcher matcher = p.matcher(textValue);
                        if(matcher.matches()){
                        	log.info("date------"+(textValue)+"---"+Double.parseDouble(textValue));
                           //是数字当作double处理
                           cell.setCellValue(Double.parseDouble(textValue));
                        }else{
                           HSSFRichTextString richString = new HSSFRichTextString(textValue);
                           HSSFFont font3 = wb.createFont();
                           font3.setColor(HSSFColor.BLUE.index);
                           richString.applyFont(font3);
                           cell.setCellValue(richString);
                        }
                     }
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
        // 第六步，将文件存到指定位置  
        FileOutputStream fos=null;
        try  
        {  
            fos = new FileOutputStream(path);  
            wb.write(fos);  
        }  catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
}

class Student  
{  
    private int id;  
    private String name;  
    private int age;  
    private Date birth;  
  
    public Student()  
    {  
    }  
  
    public Student(int id, String name, int age, Date birth)  
    {  
        this.id = id;  
        this.name = name;  
        this.age = age;  
        this.birth = birth;  
    }  
  
    public int getId()  
    {  
        return id;  
    }  
  
    public void setId(int id)  
    {  
        this.id = id;  
    }  
  
    public String getName()  
    {  
        return name;  
    }  
  
    public void setName(String name)  
    {  
        this.name = name;  
    }  
  
    public int getAge()  
    {  
        return age;  
    }  
  
    public void setAge(int age)  
    {  
        this.age = age;  
    }  
  
    public Date getBirth()  
    {  
        return birth;  
    }  
  
    public void setBirth(Date birth)  
    {  
        this.birth = birth;  
    }  
  
}  