package com.lvf.cobol.to.java;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


public class TestStart {
	
	public final static String fileReadUrl = "F:/java/gitcache/springboot/src/main/java/com/spring/cobol/to/java/context.txt";
	public final static String fileWriteUrl = "F:/java/gitcache/springboot/src/main/java/com/spring/cobol/to/java/ContextResult.java";
	
	public static void main(String[] args) {
		List<Handler> arrayList = new ArrayList<Handler>();
		try {
			File file = new File(fileReadUrl);
			Reader input = new InputStreamReader(new FileInputStream(file), "utf-8");
			BufferedReader bf = new BufferedReader(input);
			
			String str;
			while ((str = bf.readLine()) != null) {
				
				String[] array = str.split("\\s+");
				if (array.length <= 1) {
					arrayList.add(new HandlerUndefine(str));
					continue;
				}
				
				Handler handler = null;
				if (array[1].indexOf("MOVE") >= 0){
					handler = new HandlerMove(str);
				} else if (array[1].indexOf("END-IF") >= 0) {
					handler = new HandlerEndIf(str);
				} else if (array[1].indexOf("ADD") >= 0) {
					handler = new HandlerAdd(str);
				} else if (array[1].indexOf("ELSE") >= 0) {
					handler = new HandlerElse(str);
				} else if (array[1].indexOf("IF") >= 0) {
					handler = new HandlerIf(str);
				} else if (str.indexOf(" PIC ") >= 0) {
					handler = new HandlerPic(str);
				} else if (array[1].indexOf("END-PERFORM") >= 0) {
					handler = new HandlerEndPerform(str);
				} else if (array[1].indexOf("PERFORM") >= 0) {
					handler = new HandlerPerform(str);
				} else if (array[1].indexOf("DISPLAY") >= 0) {
					handler = new HandlerDisplay(str);
				} else if (array[1].indexOf("STOPRUN") >= 0) {
					handler = new HandlerStopRun(str);
				} else if (array[1].indexOf("COMPUTE") >= 0) {
					handler = new HandlerCompute(str);
				} else if (array[1].equals("GO")) {
					handler = new HandlerGoTo(str);
				} else if (array[1].equals("EJECT")) {
					handler = new HandlerEject(str);
				} else if (array[1].equals("SUBTRACT")) {
					handler = new HandlerSubtract(str);
				} else if (array[1].equals("ERROR")) {
					handler = new HandlerError(str);
				} else if (isMethod(array[1])) {
					handler = new HandlerMethod(str);
				} else {
					handler = new HandlerUndefine(str);
				}
				arrayList.add(handler);
			}
			bf.close();
			input.close();
			
			File fileResult = new File(fileWriteUrl);
			FileOutputStream fos = new FileOutputStream(fileResult); 
	        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8"); 
	        osw.write("package com.spring.cobol.to.java;\n");
	        osw.write("public class ContextResult {\n");
			for (Handler item : arrayList) {
				String strResult = item.proccess();
				System.out.println(strResult);
				osw.write(strResult);
			}
			osw.write("}");
			osw.flush(); 
			osw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void otherTest() {
	}
	
	public static boolean isMethod(String str) {
		if (str.indexOf(".") < 0) {
			return false;
		} else {
			str = str.substring(0, str.indexOf(".") + 1);
		}
		String REGEX = "([0-9]{1,}-[a-zA-Z0-9]{1,}.)|([0-9]{1,}-[a-zA-Z0-9]{1,}-[a-zA-Z0-9]{1,}.)|([0-9]{1,}-[a-zA-Z0-9]{1,}-[a-zA-Z0-9]{1,}-[a-zA-Z0-9]{1,}.)|([0-9]{1,}-[a-zA-Z0-9]{1,}-[a-zA-Z0-9]{1,}-[a-zA-Z0-9]{1,}-[a-zA-Z0-9]{1,}.)";
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(REGEX);
		matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
}
