package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerDisplay extends HandlerAbstract implements Handler {
	
	public HandlerDisplay(String str) {
		this.str = str;
	}
	
	//000-OPEN-FILE.
	@Override
	public void handler() {
		int displayIndex = parseArray.indexOf("DISPLAY");
		buf.append("System.out.println(");
		for (int index = displayIndex + 1; index < parseArray.size(); index ++ ){
			buf.append(parseArray.get(index).replaceAll("null.", "\"null\""));
			if (index != parseArray.size() - 1) {
				buf.append(" + ");
			}
		}
		buf.append(")");
		buf.append(";");
	}

}
