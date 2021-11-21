package com.lvf.springboot.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ErrorTest {
	
	public static void main(String[] args) {
		List<String> result = new ArrayList<String>();
		
		for (int i = 0 ; i < 10 ; i ++) {
			List<String> list = new ArrayList<String>();
			list.add("2222222222222:" + i);
			Callable<List<String>> call = new ErrorCallTask(list);
			FutureTask<List<String>> task = new FutureTask<List<String>>(call);
			Thread thread = new Thread(task);
			thread.start();
			try {
				//这样直接写竟然是错误的？
				result.addAll(task.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		for (String item : result) {
			System.out.println(item);
		}
	}

}
