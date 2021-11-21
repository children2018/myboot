package com.lvf.cobol.to.java;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerIf extends HandlerAbstract implements Handler {
	
	public HandlerIf(String str) {
		this.str = str;
	}
	
	//IF B-PRINT-KIND = "1"
	@Override
	public void handler() {
		
		while (parseArray.indexOf("GREATER") >= 0) {
			int greaterIndex = parseArray.indexOf("GREATER");
			if (parseArray.get(greaterIndex - 1).equals("NOT") && parseArray.get(greaterIndex + 1).equals("THAN")) {
				parseArray.set(greaterIndex, "<=");
				parseArray.remove(greaterIndex + 1);
				parseArray.remove(greaterIndex - 1);
			} else if (parseArray.get(parseArray.indexOf("GREATER") + 1).equals("THAN")) {
				parseArray.set(greaterIndex, ">");
				parseArray.remove(greaterIndex + 1);
			}
		}
		
		while (parseArray.indexOf("LESS") >= 0) {
			int lessIndex = parseArray.indexOf("LESS");
			if (parseArray.get(lessIndex - 1).equals("NOT") && parseArray.get(lessIndex + 1).equals("THAN")) {
				parseArray.set(lessIndex, ">=");
				parseArray.remove(lessIndex + 1);
				parseArray.remove(lessIndex - 1);
			} else if (parseArray.get(lessIndex + 1).equals("THAN")) {
				parseArray.set(lessIndex, "<");
				parseArray.remove(lessIndex + 1);
			}
		}
		
		if (parseArray.indexOf("EQUAL") >= 0 && parseArray.indexOf("EQUAL") < parseArray.size() - 1){
			parseArray.set(parseArray.indexOf("EQUAL") + 1, 
				".equals(" + parseArray.get(parseArray.indexOf("EQUAL") + 1) + ")");
			parseArray.remove(parseArray.indexOf("EQUAL"));
		}
		
		int ifIndex = parseArray.indexOf("IF");
		buf.append("if");
		buf.append(" (");
		
		for (int index = ifIndex + 1; index < parseArray.size(); index ++ ){
			buf.append(parseArray.get(index).replaceAll(" AND ", " && ").replaceAll(" OR ", " || "));
			buf.append(" ");
		}
		buf.append(") ");
		buf.append("{");
	}

}
