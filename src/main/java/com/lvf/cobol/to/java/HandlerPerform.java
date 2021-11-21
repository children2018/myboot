package com.lvf.cobol.to.java;

public class HandlerPerform extends HandlerAbstract implements Handler {
	
	public HandlerPerform(String str) {
		this.str = str;
	}
	
	//000-OPEN-FILE.
	@Override
	public void handler() {
		int performIndex = parseArray.indexOf("PERFORM");
		buf.append("METHOD_");
		for (int index = performIndex + 1; index < parseArray.size(); index ++ ){
			if (parseArray.get(index).indexOf(".") >= 0) {
				buf.append(parseArray.get(index).substring(0, parseArray.get(index).indexOf(".")));
			} else {
				buf.append(parseArray.get(index));
			}
		}
		buf.append("()");
		buf.append(";");
	}

}
