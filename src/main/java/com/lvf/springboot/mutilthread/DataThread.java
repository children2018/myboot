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
		System.out.println(System.currentTimeMillis() + " uuid:" + uuid + " created.");
	}
	
	@Override
	public void run() {
		while (true) {
			List<T> list = data.getData();
			
			if (list == null || list.isEmpty()) {
				break;
			}
			
			System.out.println(System.currentTimeMillis() + " uuid:" + uuid + " list.size:" + list.size());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/*for (T t : list) {
			}*/
		}
		System.out.println(System.currentTimeMillis() + " uuid:" + uuid + " done");
		this.dataService.doneNum(uuid);
	}

}
