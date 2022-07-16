package com.lvf.springboot.poi.cvs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

/**
 * 如果单元格内容存在敏感字符的情况下： 1、出现英文逗号，整个单元格内容会加上双引号，例：[我们,] -> ["我们,"]
 * 2、出现英文引号，整个单元格内容会加上双引号，例：[我们"爱]-> ["我们""爱"] 3、出现英文引号，每个引号都会调整为双引号，例：[我们"爱]->
 * ["我们""爱"]
 * "(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,"
 * @author Administrator
 *
 */
public class CvsParser {

	@Test
	public void handler() {
		try {
			Resource resource = new ClassPathResource("file/abc.csv");
			InputStreamReader in = new InputStreamReader(resource.getInputStream(), "gbk");
			BufferedReader read = new BufferedReader(in);
			String line;
			String[] header = null;
			while ((line = read.readLine()) != null) {

				System.out.println(line);

				// 表头部分
				if (header == null) {
					header = line.split(",");
					continue;
				}

				// 表体部分
				String lineStr = line + ",";
				while (StringUtils.isNotEmpty(lineStr)) {
					String item = lineStr.substring(0, lineStr.indexOf(","));
					if (item.indexOf("\"") == -1) { // 如果没有引号
						System.out.println("item noquote:" + item);
						lineStr = lineStr.substring(lineStr.indexOf(",") + 1, lineStr.length());
					} else { // 如果有引号
						int quoteCount = item.length() - item.replace("\"", "").length(); // 引号的数量
						if (quoteCount % 2 == 1) { // 引号的数量为奇数
							lineStr = lineStr.replaceFirst(",", "<[[-LFS-]]>");
						} else { // 引号的数量为偶数
							item = item.substring(1, item.length() - 1); // 去掉字符串前后添加的引号
							System.out.println("item hsquote:" + item.replace("<[[-LFS-]]>", ",").replace("\"\"", "\"")); // 将双引号合并成一个引号
							lineStr = lineStr.substring(lineStr.indexOf(",") + 1, lineStr.length());
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
