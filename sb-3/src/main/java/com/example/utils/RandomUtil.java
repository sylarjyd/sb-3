package com.example.utils;

import java.util.UUID;

public class RandomUtil {
	
	
	public static String getRandomId() {
		String randomid ="";
		long currentTimeMillis = System.currentTimeMillis();
		String uuidStr= UUID.randomUUID().toString().substring(0, 6);
		randomid = currentTimeMillis+uuidStr;
		return randomid;
	}
	
}
