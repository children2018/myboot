package com.lvf.springboot.poi.cvs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Regex {
	
	@Test
	public void a1() {
		String str = "2017/7/21 0:01:30.32104820184201842018412022-06/224324324324";
		Pattern pattern = Pattern.compile("\\d{4}[-/]\\d{1,2}[-/]\\d{2}");
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			System.out.println("m.group():" + m.group());
		}
	}
	
	@Test
	public void a2() {
		String str = "2017/7/21 0:01:30.32104820人184201ABC842018412022-06/224324的324324";
		Pattern pattern = Pattern.compile("\\d");
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			System.out.print(m.group());
		}
	}
	
	@Test
	public void a3() {
		String str = "2017/7/21 0:01:30.32104820184201842018412022-06/224324324324";
		Pattern pattern = Pattern.compile("2017[-/]\\d{1,2}[-/]\\d{2}");
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			System.out.println("m.group():" + m.group());
		}
	}
	
	@Test
	public void a4() {
		String str = "2017/7/21 0:01:30.321048201843人018412022-06/224324324324";
		Pattern pattern = Pattern.compile("\\.\\d+");
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			System.out.println("m.group():" + m.group());
		}
	}

}
