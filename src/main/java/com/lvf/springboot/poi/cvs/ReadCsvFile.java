package com.lvf.springboot.poi.cvs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ReadCsvFile {
	
	public static void main(String[] args) {
		InputStreamReader in = null;
		Reader read = null;
		List<CSVRecord> list = null;
		try {
			Resource resource = new ClassPathResource("file/abc.csv");
			in = new InputStreamReader(resource.getInputStream(), "gbk");
			read = new BufferedReader(in);
			//CSVParser parser = CSVParser.parse(read, CSVFormat.RFC4180.withFirstRecordAsHeader().withDelimiter(','));
			CSVParser parser = new CSVParser(read, CSVFormat.RFC4180.withFirstRecordAsHeader().withDelimiter(','));
			list = parser.getRecords();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (read != null) {
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (list != null) {
			for (CSVRecord rec : list) {
				System.out.print(rec.get("BOUND01"));
				System.out.print("\t\t|");
				System.out.print(rec.get("BOUND02"));
				System.out.print("\t\t|");
				System.out.print(rec.get("BOUND03"));
				System.out.print("\t\t|");
				System.out.print(rec.get("BOUND04"));
				System.out.print("\t\t|");
				System.out.print(rec.get("BOUND05"));
				System.out.print("\t\t|");
				System.out.print(rec.get("BOUND06"));
				System.out.print("\t\t|");
				System.out.print(rec.get("BOUND07"));
				System.out.println("\t\t|");
			}
		}
		
	}
}