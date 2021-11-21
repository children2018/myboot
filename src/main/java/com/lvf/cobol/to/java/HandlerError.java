package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerError extends HandlerAbstract implements Handler {
	
	public HandlerError(String str) {
		this.str = str;
	}
	
	@Override
	public void handler() {
		
		int errorIndex = parseArray.indexOf("ERROR");
		buf.append("");
		buf.append(parseArray.get(errorIndex + 1));
		buf.append("");
		buf.append(";");
		buf.append("\n");
		
	}

}
