package com.lvf.springboot.mutilthread;

import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;

public class DataThread<T> extends Thread {
	
	private Data<T> data = null;
	private String uuid = UUID.randomUUID().toString();
	private DataService<T> dataService = null;
	
	public DataThread(Data<T> data, DataService<T> dataService) {
		this.data = data;
		this.dataService = dataService;
		System.out.println(uuid + " created.");
	}
	
	@Override
	public void run() {
		while (true) {
			List<T> list = data.getData();
			
			if (list == null || list.isEmpty()) {
				break;
			}
			
			for (T t : list) {
				System.out.println(uuid + " list.size:" + list.size());
			}
		}
		System.out.println(uuid + " done");
		this.dataService.doneNum(uuid);
	}

}
