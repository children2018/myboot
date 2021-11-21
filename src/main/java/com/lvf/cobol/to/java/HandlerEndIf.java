package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerEndIf extends HandlerAbstract implements Handler {
	
	public HandlerEndIf(String str) {
		this.str = str;
	}
	
	//END-IF
	@Override
	public void handler() {
		buf.append("}");
	}

}
