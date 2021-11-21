package com.lvf.springboot.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ErrorCallTask implements Callable<List<String>> {
	
	private List<String> list;
	
	public ErrorCallTask(List<String> list) {
		this.list = list;
	}

	@Override
	public List<String> call() throws Exception {
		List<String> result = new ArrayList<String>();
		for (String str : list) {
			System.out.println("task:" + str);
			result.add("task:" + str);
		}
		Thread.sleep(1000 * 5);
		return result;
	}

}
