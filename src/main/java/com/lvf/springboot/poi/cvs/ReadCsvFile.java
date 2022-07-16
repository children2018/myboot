package com.lvf.springboot.poi.cvs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ReadCsvFile {
	
	public static void main(String[] args) {
		try {
			Resource resource = new ClassPathResource("file/abc.csv");
			InputStreamReader in = new InputStreamReader(resource.getInputStream(), "gbk");
			Reader read = new BufferedReader(in);
			//CSVParser parser = new CSVParser(new FileReader(resource.getFile()), CSVFormat.RFC4180.withFirstRecordAsHeader().withDelimiter(','));
			CSVParser parser = CSVParser.parse(read, CSVFormat.RFC4180.withFirstRecordAsHeader().withDelimiter(','));
			for (CSVRecord rec : parser) {
				System.out.print(rec.get("BOUND01"));
				System.out.print("|\t");
				System.out.print(rec.get("BOUND02"));
				System.out.print("|\t");
				System.out.print(rec.get("BOUND03"));
				System.out.print("|\t");
				System.out.print(rec.get("BOUND04"));
				System.out.print("|\t");
				System.out.print(rec.get("BOUND05"));
				System.out.print("|\t");
				System.out.print(rec.get("BOUND06"));
				System.out.print("|\t");
				System.out.print(rec.get("BOUND07"));
				System.out.println("|\t");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}