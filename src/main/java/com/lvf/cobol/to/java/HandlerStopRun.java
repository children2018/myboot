package com.lvf.cobol.to.java;

public class HandlerStopRun extends HandlerAbstract implements Handler {
	
	public HandlerStopRun(String str) {
		this.str = str;
	}
	
	//000-OPEN-FILE.
	@Override
	public void handler() {
		buf.append("return ;");
	}

}
