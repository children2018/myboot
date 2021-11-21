package com.lvf.cobol.to.java;

public class HandlerMove extends HandlerAbstract implements Handler {
	
	public HandlerMove(String str) {
		this.str = str;
	}
	
	@Override
	public void handler() {
		int moveIndex = parseArray.indexOf("MOVE");
		int toIndex = parseArray.indexOf("TO");
		for (int index = toIndex + 1 ;index < parseArray.size() ; index++ ) {
			String value = parseArray.get(index);
			if (value.endsWith(".")) {
				value = value.substring(0, value.length() - 1);
			}
			buf.append(value);
			buf.append(" = ");
			buf.append(parseArray.get(moveIndex + 1));
			
			buf.append(";");
			buf.append("\n");
		}
	}

}
