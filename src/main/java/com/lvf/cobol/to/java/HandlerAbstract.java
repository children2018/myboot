package com.lvf.cobol.to.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class HandlerAbstract implements Handler {
	
	protected String str = null;
	protected List<String> parseArray = null; 
	protected StringBuffer buf = new StringBuffer();
	
	public final static Map<String, String> replaceMap = new HashMap<String, String>();
	static {
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
		replaceMap.put("", "");
	}
	
	public String proccess() {
		parseArray = Arrays.asList(
			str.replaceAll("'\\s+'", "null")
			.split("\\s+")
		).stream().map(o -> {
			o = o.trim()
			.replaceAll("'", "\"")
			.replaceAll("-", "_")
			.replaceAll("SPACES", "null")
			.replaceAll("SPACE", "null")
			.replaceAll(" ", " ")
			.replaceAll("AND", o.equals("AND")? "&&" : "AND")
			.replaceAll("OR", o.equals("OR")? "||" : "OR")
			.replaceAll("ZERO", o.equals("ZERO")? "0" : "ZERO");
			for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
				o = o.replaceAll(entry.getKey(), entry.getValue());
			};
			return o;
		}).collect(Collectors.toList());
		parseArray.remove(0);
		removeLast(parseArray);
		buf.append("//");
		buf.append(str);
		buf.append("\n");
		handler();
		buf.append("\n");
		return buf.toString();
	}
	
	public void removeLast(List<String> parseArray) {
		
		if (parseArray.size() == 0) {
			return ;
		}
		String REGEX = "[0-9]{5,8}";
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(REGEX);
		matcher = pattern.matcher(parseArray.get(parseArray.size() - 1));
		if (matcher.matches()) {
			parseArray.remove(parseArray.size() - 1);
		}
	}
	
	public abstract void handler();
	
}
