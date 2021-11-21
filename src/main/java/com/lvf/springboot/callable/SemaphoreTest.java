package com.lvf.springboot.callable;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

	public static void main(String[] args) {

		class Process implements Runnable {

			Semaphore sem = new Semaphore(3);

			@Override
			public void run() {
				try {
					sem.acquire();
					int sec = new Random().nextInt(10);
					System.out.println(Thread.currentThread().getName() + " say: i am coming..." + sec);
					Thread.sleep(1000 * sec);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sem.release();
				}
			}
		}
		
		Process process = new Process();

		for (int i = 1; i <= 23; i++) {
			new Thread(process).start();
		}
		
		System.out.println("it's done!");
		
	}

}
