package com.lvf.springboot.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.lvf.springboot.model.User;

public class ExcelService {
	
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	
	public ExcelService() {
		//创建HSSFWorkbook对象
		wb = new HSSFWorkbook();
		//创建HSSFSheet对象
		sheet = wb.createSheet("sheet0");
	}

	public static void main(String[] args) {
		new ExcelService().abc();
		System.out.println("it's done");
	}
	
	public void abc1() {
		
		List<User> list = new ArrayList<User>();
		for (int i=1; i<= 50000;i++) {
    		User user = new User();
    		user.setId(UUID.randomUUID().toString());
    		user.setAge(i);
    		user.setName("jack" + i);
    		user.setPassword("password" + i);
    		list.add(user);
    	}
		
		//创建HSSFWorkbook对象
		HSSFWorkbook wb = new HSSFWorkbook();
		//创建HSSFSheet对象
		HSSFSheet sheet = wb.createSheet("sheet0");
		for (int i=0 ;i<list.size() ;i ++) {
			User user = list.get(i);
			//创建HSSFRow对象
			HSSFRow row = sheet.createRow(list.size() - i );
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(user.getId());
			
			cell = row.createCell(1);
			cell.setCellValue(user.getAge());
			
			cell = row.createCell(2);
			cell.setCellValue(user.getName());
			
			cell = row.createCell(3);
			cell.setCellValue(user.getPassword());
		}
		
		//输出Excel文件
		try {
			FileOutputStream output = new FileOutputStream("d:\\workbook.xls");
			wb.write(output);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void abc() {
		
		
		long begin1 = System.currentTimeMillis();
		List<User> list = new ArrayList<User>();
		for (int i=1; i<= 50000;i++) {
    		User user = new User();
    		user.setId(UUID.randomUUID().toString());
    		user.setAge(i);
    		user.setName("jack" + i);
    		user.setPassword("password" + i);
    		list.add(user);
    	}
		long begin2 = System.currentTimeMillis();
		System.out.println("cost1:" + (begin2 - begin1));
		
		ExecutorService er = Executors.newFixedThreadPool(100);
		CountDownLatch ch = new CountDownLatch(list.size());
		for (int i =0;i <list.size() ;i++) {
			FutureTask<Boolean> fu = new FutureTask<Boolean>(new Task(i, list.get(i), ch));
			er.submit(fu);
			try {
				Boolean bn = fu.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		long begin3 = System.currentTimeMillis();
		System.out.println("cost2:" + (begin3 - begin2));
		try {
			ch.await();
			er.shutdown();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long begin4 = System.currentTimeMillis();
		System.out.println("cost3:" + (begin4 - begin3));
		//输出Excel文件
		FileOutputStream output = null;
		try {
			File file = new File("d://test_workbook.xls");
			output = new FileOutputStream(file);
			wb.write(output);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long begin5 = System.currentTimeMillis();
		System.out.println("cost3:" + (begin5 - begin4));
		System.out.println("cost4:" + (begin5 - begin1));
	}
	
	class Task implements Callable<Boolean>{
		private int index;
		private User user;
		private CountDownLatch ch;
		public Task(int index ,User user, CountDownLatch ch) {
			this.index = index;
			this.user = user;
			this.ch = ch;
		}
		
	    @Override
	    public Boolean call() throws Exception {
	    	System.out.println("index:" + index);
	    	//创建HSSFRow对象
			HSSFRow row = sheet.createRow(index);
			HSSFCell cell = row.createCell(0);
			setValue(cell, user.getId());
			
			cell = row.createCell(1);
			setValue(cell, "" + user.getAge());
			
			cell = row.createCell(2);
			setValue(cell, "" + user.getName());
			
			cell = row.createCell(3);
			setValue(cell, "" + user.getPassword());
			ch.countDown();
			return true;
	    }
	    
	}
	
	private synchronized void setValue(final Cell cell, String value) {
		cell.setCellValue(value);
	}

}
