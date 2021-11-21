package com.lvf.springboot.callable;

import java.util.Random;
import java.util.concurrent.Callable;

class CallableExample2 implements Callable {
	
	public int index;
	
	public CallableExample2(int index) {
		// TODO Auto-generated constructor stub
		this.index = index;
	}
	
	public Object call() throws Exception {
		//主线程忙碌的情况下，线程已经在运行
		for (int i = 0;i < 5;i++) {
			System.out.println("【xx】".replace("xx", ""+index) + i);
		}
		
		Random generator = new Random(); 
		Integer randomNumber = generator.nextInt(30); 
		System.out.println("{xx}return:".replace("xx", ""+ index) + randomNumber);
		if (index==2) randomNumber = 36;
		Thread.sleep(1000 * (randomNumber));
		//主线程忙碌的情况下，线程已经结束并返回值
		return randomNumber;
	}

}
