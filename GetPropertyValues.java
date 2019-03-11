/*
 *For reading data from properties file. 
 */
package com.hrone.library;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author shalini.sharma
 *
 */
public class GetPropertyValues {
	private static String FILEPATH=".\\test-data\\config.properties";
	static FileInputStream fis;
	static FileOutputStream fos;
	
	public static String getPropertyValue(String key) {
		String value=null;
		try {
			fis=new FileInputStream(new File(FILEPATH));
			Properties prop = new Properties();
			prop.load(fis);
			value = prop.getProperty(key);
			
		} catch (Exception e) {
			e.getMessage();
		}
		return value;		
	}
	
	public static void writeToPropertyFile(String key, String value) {
		try {
			fis=new FileInputStream(new File(FILEPATH));
			Properties prop = new Properties();
			prop.load(fis);
			fos=new FileOutputStream(new File(FILEPATH));
			prop.setProperty(key, value);
			prop.store(fos, "Data Written");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getPropertyValue("UserName"));
		writeToPropertyFile("test", "01111");
	}

}
