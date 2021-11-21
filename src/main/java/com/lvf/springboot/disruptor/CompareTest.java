package com.lvf.springboot.disruptor;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * 担心影响， 分开执行测试
 * 
 * @author admin
 *
 */
public class CompareTest {
	public static int THREAD = 256; // 线程数量
	public static int PER = 65536; // 单个线程生产数量
	public static int TOTAL_COUNT = THREAD * PER; // 数据总量
	public static int SIZE = 33554432; // 最大容量:2的乘方

	public static void main(String[] args) {
		println("线程数：" + THREAD + " 单线程生产量: " + PER + " 容量：" + SIZE + " 数据总量：" + TOTAL_COUNT);
		//new Thread(() -> ArrayBlockingQueueTest.execute()).start();
		new Thread(() -> DisruptorTest.execute()).start();
	}

	public static void println(String msg) {
		System.out.println(DateTimeFormatter.ISO_INSTANT.format(Instant.now()) + "  " + msg);
	}
}

/*
本次代码测试基于相同的 容量、生产线程数、单个线程生产量， 仅有一个消费线程。

修改各参数得到的结果：

数据规模、并发线程数、 最主要的是容量小时：Disruptor没有优势

2019-08-29T07:42:35.235Z  线程数：64 单线程生产量: 2048 容量：32 数据总量：131072
2019-11-26T07:57:42.087Z  dataEvent newInstance: 1
2019-11-26T07:57:42.087Z  dataEvent newInstance: 2
...
2019-11-26T07:57:42.087Z  dataEvent newInstance: 64
2019-08-29T07:42:48.742Z  EventProcessor wrapper
2019-08-29T07:42:48.743Z  disruptor start success！
2019-08-29T07:42:51.113Z  处理的sequence：131071 count：131072  Disruptor 总耗时：2369

2019-08-29T07:42:36.200Z  ArrayBlockingQueue 生产耗时：962
2019-08-29T07:42:36.200Z  处理count：131072  ArrayBlockingQueue 消费耗时：962
2019-08-29T07:42:36.201Z  ArrayBlockingQueue 总耗时：963
2019-08-29T08:24:38.641Z  线程数：512 单线程生产量: 2048 容量：32 数据总量：1048576
2019-08-29T08:24:38.670Z  EventProcessor wrapper
2019-08-29T08:24:38.670Z  disruptor start success！
2019-08-29T08:25:08.590Z  处理的sequence：1048575 count：1048576  Disruptor 总耗时：29918

2019-08-29T08:25:54.753Z  处理count：1048576  ArrayBlockingQueue 消费耗时：9231
2019-08-29T08:25:54.753Z  ArrayBlockingQueue 生产耗时：9230
2019-08-29T08:25:54.753Z  ArrayBlockingQueue 总耗时：9231
增大容量：  Disruptor的性能上升

2019-08-29T07:40:28.980Z  线程数：64 单线程生产量: 2048 容量：128 数据总量：131072
2019-08-29T07:40:29.008Z  EventProcessor wrapper
2019-08-29T07:40:29.008Z  disruptor start success！
2019-08-29T07:40:29.694Z  处理的sequence：131071 count：131072  Disruptor 总耗时：685

2019-08-29T07:47:42.436Z  处理count：131072  ArrayBlockingQueue 消费耗时：508
2019-08-29T07:47:42.436Z  ArrayBlockingQueue 生产耗时：508
2019-08-29T07:47:42.436Z  ArrayBlockingQueue 总耗时：508
2019-08-29T07:43:39.073Z  线程数：64 单线程生产量: 2048 容量：512 数据总量：131072
2019-08-29T07:43:39.101Z  EventProcessor wrapper
2019-08-29T07:43:39.101Z  disruptor start success！
2019-08-29T07:43:39.269Z  处理的sequence：131071 count：131072  Disruptor 总耗时：167

2019-08-29T07:43:53.722Z  ArrayBlockingQueue 生产耗时：383
2019-08-29T07:43:53.722Z  处理count：131072  ArrayBlockingQueue 消费耗时：383
2019-08-29T07:43:53.722Z  ArrayBlockingQueue 总耗时：383
2019-08-29T07:44:05.995Z  线程数：64 单线程生产量: 2048 容量：1024 数据总量：131072
2019-08-29T08:18:10.426Z  EventProcessor wrapper
2019-08-29T08:18:10.426Z  disruptor start success！
2019-08-29T08:18:10.524Z  处理的sequence：131071 count：131072  Disruptor 总耗时：97

2019-08-29T07:44:06.365Z  ArrayBlockingQueue 生产耗时：367
2019-08-29T07:44:06.365Z  处理count：131072  ArrayBlockingQueue 消费耗时：367
2019-08-29T07:44:06.365Z  ArrayBlockingQueue 总耗时：367
再增大各指标参数： Disruptor优势越来越明显

2019-08-29T07:50:59.911Z  线程数：64 单线程生产量: 65536 容量：1048576 数据总量：4194304
2019-08-29T07:51:28.075Z  EventProcessor wrapper
2019-08-29T07:51:28.075Z  disruptor start success！
2019-08-29T07:51:28.577Z  处理的sequence：4194303 count：4194304  Disruptor 总耗时：501

2019-08-29T07:51:11.549Z  ArrayBlockingQueue 生产耗时：11633
2019-08-29T07:51:11.575Z  处理count：4194304  ArrayBlockingQueue 消费耗时：11659
2019-08-29T07:51:11.575Z  ArrayBlockingQueue 总耗时：11659
2019-08-29T07:57:22.994Z  线程数：128 单线程生产量: 65536 容量：1048576 数据总量：8388608
2019-08-29T07:57:23.074Z  EventProcessor wrapper
2019-08-29T07:57:23.074Z  disruptor start success！
2019-08-29T07:57:24.036Z  处理的sequence：8388607 count：8388608  Disruptor 总耗时：961

2019-08-29T07:58:25.567Z  ArrayBlockingQueue 生产耗时：47941
2019-08-29T07:58:25.646Z  处理count：8388608  ArrayBlockingQueue 消费耗时：48020
2019-08-29T07:58:25.647Z  ArrayBlockingQueue 总耗时：48021
再大线程数， ArrayBlockingQueue 更耗时了，而Disruptor仍旧很快

2019-08-29T08:05:17.927Z  线程数：256 单线程生产量: 65536 容量：1048576 数据总量：16777216
2019-08-29T08:05:18.026Z  EventProcessor wrapper
2019-08-29T08:05:18.027Z  disruptor start success！
2019-08-29T08:05:20.060Z  处理的sequence：16777215 count：16777216  Disruptor 总耗时：2032
测试结论： 

容量大小、消费者的消费速度  与耗时 成反比。
Disruptor的性能在高并发、高数据规模（bufferSize 要大些）时表现更突出。
Disruptor与LinkedBlockingQueue（因读/写分开锁控制，比ArrayBlockingQueue性能好些）比对而言，当bufferSize大些的时候，也有优势。 
*/
