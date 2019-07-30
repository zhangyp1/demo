package com.newland.paas.paastool.testngmocktool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
	public static String getValue(String keyName) {
		String value = "";
		Properties p = new Properties();
		try {
			p.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream("cfg.properties"),
					"UTF-8"));
			value = p.getProperty(keyName, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}
