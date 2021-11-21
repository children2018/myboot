package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerEndPerform extends HandlerAbstract implements Handler {
	
	public HandlerEndPerform(String str) {
		this.str = str;
	}
	
	//000-OPEN-FILE.
	@Override
	public void handler() {
		buf.append("}");
	}

}
