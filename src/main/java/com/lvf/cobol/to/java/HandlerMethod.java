package com.lvf.cobol.to.java;
import java.util.concurrent.atomic.AtomicLong;

public class HandlerMethod extends HandlerAbstract implements Handler {
	
	public static AtomicLong atomicLong = new AtomicLong(0);
	
	public HandlerMethod(String str) {
		this.str = str;
	}
	
	//000-OPEN-FILE.
	@Override
	public void handler() {
		if (atomicLong.incrementAndGet() != 1){
			buf.append("}");
			buf.append("\n");
		}
		buf.append("public void METHOD_");
		buf.append(parseArray.get(0).substring(0, parseArray.get(0).indexOf(".")));
		buf.append("() throws Exception {");
	}

}
