package com.lvf.cobol.to.java;

public class HandlerCompute extends HandlerAbstract implements Handler {
	
	public HandlerCompute(String str) {
		this.str = str;
	}
	
	//000-OPEN-FILE.
	@Override
	public void handler() {
		int computeIndex = parseArray.indexOf("COMPUTE");
		for (int index = computeIndex + 1; index < parseArray.size(); index++) {
			if ("_".equals(parseArray.get(index))) {
				buf.append(" - ");
				continue;
			} 
			buf.append(parseArray.get(index));
		}
		buf.append(";");
	}

}
