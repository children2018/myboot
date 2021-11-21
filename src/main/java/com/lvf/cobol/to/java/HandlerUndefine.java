package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerUndefine extends HandlerAbstract implements Handler {
	
	public HandlerUndefine(String str) {
		this.str = str;
	}
	
	@Override
	public void handler() {
		
		if (parseArray.size() <= 0) {
			return ;
		}
		
		if (parseArray.get(0).equals("TO")) {
			for (int index = 0 ;index < parseArray.size() ; index++ ) {
				buf.append(parseArray.get(index));
				buf.append(" ");
			}
			return ;
		}
		
	}

}
