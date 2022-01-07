package com.lvf.springboot.singlelist;

import java.util.List;

public class Example extends SingleToList<String> {
	
	public static void main(String[] args) {

		int max = 107123;
		long start = System.currentTimeMillis();

		SingleToList<String> test = new Example();
		test.setMaxSize(2000);
		test.start();
		
		for (int i = 0; i < max; i++) {
			final int iii = i;
		/*	try {
				Thread.sleep(9);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			new Thread(new Runnable() {
				public void run() {
					test.add("index:" + iii);
				}
			}).start();
		}

		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
	}


	@Override
	public void save(List<String> list) {
		System.out.println("list.size():" + list.size());
	}

}
