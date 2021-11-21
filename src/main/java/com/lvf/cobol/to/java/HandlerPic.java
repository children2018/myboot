package com.lvf.cobol.to.java;

/*
9	数字
A	字母
X	字母数字
V	隐式小数
S	符号
P	假定小数
*/
public class HandlerPic extends HandlerAbstract implements Handler {
	
	public HandlerPic(String str) {
		this.str = str;
	}
	
	////004400    05 K1-EXPDA                PIC S9(08)V99.
	@Override
	public void handler() {
		
		int picIndex = parseArray.indexOf("PIC");
		if (picIndex == 0) {
			return ;
		}
		
		buf.append(" private ");
		if (parseArray.get(picIndex + 1).startsWith("X")) {
			buf.append("String ");
			buf.append(parseArray.get(picIndex - 1));
		} else if (parseArray.get(picIndex + 1).startsWith("9")) {
			buf.append("double ");
			buf.append(parseArray.get(picIndex - 1));
		} else if (parseArray.get(picIndex + 1).startsWith("A")) {
			buf.append("String ");
			buf.append(parseArray.get(picIndex - 1));
		} else if (parseArray.get(picIndex + 1).startsWith("S")) {
			buf.append("double ");
			buf.append(parseArray.get(picIndex - 1));
		} else {
			return ;
		}
		
		if (parseArray.indexOf("VALUE") >= 0) {
			buf.append(" = ");
			String value =parseArray.get(parseArray.indexOf("VALUE") + 1);
			if (value.endsWith(".")) {
				value = value.substring(0, value.length() - 1);
			}
			buf.append(value);
		}
		
		buf.append(";");
	}

}
