package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerAdd extends HandlerAbstract implements Handler {
	
	public HandlerAdd(String str) {
		this.str = str;
	}
	
	//ADD 1 TO WK-PAGE
	@Override
	public void handler() {
		int addIndex = parseArray.indexOf("ADD");
		int toIndex = parseArray.indexOf("TO");
		buf.append(parseArray.get(toIndex + 1).replaceAll("\\.", ""));
		buf.append(" += ");
		buf.append(parseArray.get(addIndex + 1));
		buf.append(";");
	}

}
