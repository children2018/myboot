package com.lvf.springboot.callable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class NumberCalculateTask extends RecursiveTask<Long> {
	private static final long serialVersionUID = 1L;
	// 阈值
	private static final int times = 10;
	private static final int THRESHOLD = 10000;
	private int start;
	private int end;

	public NumberCalculateTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		long sum = 0;
		// 判断任务是否足够小
		boolean canCompute = (end - start) <= THRESHOLD;
		if (canCompute) {
			// 如果小于阈值，就进行运算
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			// 如果大于阈值，就再进行任务拆分
			int middle = (start + end) / 2;
			NumberCalculateTask leftTask = new NumberCalculateTask(start, middle);
			NumberCalculateTask rightTask = new NumberCalculateTask(middle + 1, end);
			// 执行子任务
			leftTask.fork();
			rightTask.fork();
			// 等待子任务执行完，并得到执行结果
			long leftResult = leftTask.join();
			long rightResult = rightTask.join();
			// 合并子任务
			sum = leftResult + rightResult;

		}
		return sum;
	}

	public static void main(String[] args) {
		a1();
		a2();
	}

	public static void a1() {
		long begin = System.currentTimeMillis();
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		long sum = 0;
		for (int i = 1; i <= times; i++) {
			NumberCalculateTask task = new NumberCalculateTask(1, 10000000);
			// 执行一个任务
			forkJoinPool.submit(task);
			sum += task.join();
		}
		forkJoinPool.shutdown();
		long end = System.currentTimeMillis();
		System.out.println("sum:" + sum);
		System.out.println("cost:" + (end - begin));
	}

	public static void a2() {
		long begin = System.currentTimeMillis();
		long sum = 0;
		for (int j = 1; j <= times; j++) {
			long item = 0;
			for (long i = 1; i <= 10000000; i++) {
				item += i;
			}
			sum += item;
		}
		long end = System.currentTimeMillis();
		System.out.println("sum:" + sum);
		System.out.println("cost2:" + (end - begin));
	}

	public static void a3() {
		List a = null;
		a.subList(0, 1);
	}

}