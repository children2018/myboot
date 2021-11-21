package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerSubtract extends HandlerAbstract implements Handler {
	
	public HandlerSubtract(String str) {
		this.str = str;
	}
	
	//SUBTRACT 1 FROM A
	@Override
	public void handler() {
		int subtractIndex = parseArray.indexOf("SUBTRACT");
		int fromIndex = parseArray.indexOf("FROM");
		buf.append(parseArray.get(fromIndex + 1).replaceAll("\\.", ""));
		buf.append(" -= ");
		buf.append(parseArray.get(subtractIndex + 1).replaceAll("\\.", ""));
		buf.append(";");
	}

}
