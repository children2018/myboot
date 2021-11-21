package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerGoTo extends HandlerAbstract implements Handler {
	
	public HandlerGoTo(String str) {
		this.str = str;
	}
	
	//IF B-PRINT-KIND = "1"
	@Override
	public void handler() {
		buf.append("return ;");
		buf.append("\n");
		buf.append("}");
		buf.append("\n");
	}

}
