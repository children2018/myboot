package com.lvf.springboot.poi.cvs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

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
 * @author administrator如果单元格内容存在敏感字符的情况下出现英文逗号整个单元
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
	
	@Test
	public void handler2() {
		try {
			Resource resource = new ClassPathResource("file/abc.csv");
			InputStreamReader in = new InputStreamReader(resource.getInputStream(), "gbk");
			BufferedReader read = new BufferedReader(in);
			char[] line = new char[200];
			read.read(line, 0, 200);
			System.out.println(line);
			String str = new String(line);
			System.out.println(str.indexOf('\r'));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 1万条记录768 KB
	 * 10万条记录7.46 MB
	 * 50万条记录37.3 MB
	 * 100万条记录74.7 MB 
	 * 150万条记录112 MB （用EXCEL只能打开100万条记录，剩余的记录末加载到EXCEL里面去）
	 * 200万条记录，只能用notepad++打开
	 * 500万条记录373 MB，只能用notepad++打开
	 * 1000万条记录747 MB，只能用notepad++打开，生成文件用时29秒
	 * 3000万条记录2.18 GB，notepad++弹框显示文件太大打不开哈哈，生成文件用时87秒，CPU使用率15%，其它硬件运行指标正常
	 */
	@Test
	public void createCsvFile() {
		
		int row = 10000000;
		int col = 7;
		try {
			String content = "administrator如果单元格内容存在敏感字符的情况下出现英文逗号整个单元";
			File f = new File("C:\\Users\\Administrator\\Desktop\\test.csv");
			OutputStream output = new FileOutputStream(f);
			OutputStreamWriter in = new OutputStreamWriter(output, "gbk");
			StringBuffer sbf = new StringBuffer();
			sbf.append("SYSTEM01,SYSTEM02,SYSTEM03,SYSTEM04,SYSTEM05,SYSTEM06,SYSTEM07");
			sbf.append('\r');
			sbf.append('\n');
			in.write(sbf.toString());
			for (int i = 1; i <= row; i++) {
				sbf = new StringBuffer(); 
				for (int j = 1; j <= col; j++) {
					int n = 10;
					int m = 2;
					int i1 = new Random().nextInt((n - m) + 1) + m;
					for (int l =1 ; l <= i1 ;l ++) {
						n = content.length() - 1;
						m = 0;
						int i2 = new Random().nextInt((n - m) + 1) + m;
						sbf.append(content.charAt(i2));
					}
					if (j == col) {
						sbf.append('\r');
						sbf.append('\n');
					} else {
						sbf.append(",");
					}
				}
				in.write(sbf.toString());
			}
			in.flush();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
