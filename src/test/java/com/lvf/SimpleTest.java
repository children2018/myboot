package com.lvf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lvf.springboot.callable.Kabc;
import com.lvf.springboot.mapper.UserMapper;
import com.lvf.springboot.model.User;
import com.lvf.springboot.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private Kabc kabc;
	
	@Test
	public void test() {
		System.out.println("kabc:" + kabc.getUrl());
		System.out.println("current....");
	}
	
	
	@Test
	public void insertsListWithForkWithSubregion() {
		long begin = System.currentTimeMillis();
    	userService.insertsListWithForkWithSubregion();
    	long end = System.currentTimeMillis();
    	System.out.println("cost:" + (end - begin));
	}
	
	/**
	 * 总量10万的数量，一笔一笔查询耗时50秒
	 * @throws Exception
	 */
	@Test
	public void doTest() throws Exception {
		List<User> list = userMapper.findUserInfo();
		long start = System.currentTimeMillis();
		long sum = 0 ;
		for (User user : list) {
			System.out.println(++sum);
			User userr = userMapper.queryUserInfoById(user.getId());
			if (userr == null || StringUtils.isEmpty(userr.getName())) {
				throw new Exception("查询失败");
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
		System.out.println("it's ok");
	}
	
	/**
	 * 总量10万的数量，1000笔为一个批次查询耗时2秒，实际是1.6秒
	 * @throws Exception
	 */
	@Test
	public void testIds() throws Exception {
		List<User> list = userMapper.findUserInfo();
		long start = System.currentTimeMillis();
		long sum = 0 ;
		List<User> queryList = new ArrayList<User>();
		for (User user : list) {
			queryList.add(user);
			System.out.println(++sum);
			if (queryList.size() % 1000 == 0) {
				List<String> sss = queryList.stream().map(o -> o.getId()).collect(Collectors.toList());
				List<User> userList = userMapper.queryUserInfoByIds("'" + StringUtils.join(sss, "','") + "'");
				if (userList.stream().anyMatch(o -> StringUtils.isEmpty(o.getName())) || userList.size() != queryList.size()) {
					throw new Exception("查询失败1");
				}
				queryList.clear();
			}
		}
		
		if (queryList.size() > 0) {
			System.out.println("queryList.size():" + queryList.size());
			List<User> userList = userMapper.queryUserInfoByIds("'" + StringUtils.join(queryList.stream().map(o -> o.getId()).collect(Collectors.toList()), "','") + "'");
			if (userList.stream().anyMatch(o -> StringUtils.isEmpty(o.getName())) || userList.size() != queryList.size()) {
				throw new Exception("查询失败2");
			}
			queryList.clear();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
		System.out.println("it's ok");
	}
	
	/**
	 * 不知道是不是oracle跟MYSQL有差别的原因，oracle的SQL条件只能in一千个左右，但是MYSQL这里能in一万个，也没报错，暂时不知道原因
	 * queryUserInfoByIdsForeach
	 * @throws Exception
	 */
	@Test
	public void testIdsForeach() throws Exception {
		List<User> list = userMapper.findUserInfo();
		long start = System.currentTimeMillis();
		long sum = 0 ;
		List<User> queryList = new ArrayList<User>();
		for (User user : list) {
			queryList.add(user);
			System.out.println(++sum);
			if (queryList.size() % 10000 == 0) {
				List<String> sss = queryList.stream().map(o -> o.getId()).collect(Collectors.toList());
				List<User> userList = userMapper.queryUserInfoByIdsForeach(sss);
				if (userList.stream().anyMatch(o -> StringUtils.isEmpty(o.getName())) || userList.size() != queryList.size()) {
					throw new Exception("查询失败1");
				}
				queryList.clear();
			}
		}
		
		if (queryList.size() > 0) {
			System.out.println("queryList.size():" + queryList.size());
			List<String> sss = queryList.stream().map(o -> o.getId()).collect(Collectors.toList());
			List<User> userList = userMapper.queryUserInfoByIdsForeach(sss);
			if (userList.stream().anyMatch(o -> StringUtils.isEmpty(o.getName())) || userList.size() != queryList.size()) {
				throw new Exception("查询失败2");
			}
			queryList.clear();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
		System.out.println("it's ok");
	}
	
	/**
	 *
	 * @author Administrator
	 *该程序用来模拟发送命令与运行命令，主线程代表指挥官。新建3个线程代表战士，战士一直等待着指挥官下达命令，
	 *若指挥官没有下达命令，则战士们都必须等待。
	一旦命令下达，战士们都去运行自己的任务。指挥官处于等待状态，战士们任务运行完成则报告给
	 *指挥官。指挥官则结束等待。
	 */
	@Test
	public void testCountDownlatch() {
		ExecutorService service = Executors.newCachedThreadPool(); // 创建一个线程池
		final CountDownLatch cdOrder = new CountDownLatch(1);// 指挥官的命令。设置为1，指挥官一下达命令。则cutDown,变为0，战士们运行任务
		final CountDownLatch cdAnswer = new CountDownLatch(3);// 由于有三个战士，所以初始值为3，每个战士运行任务完成则cutDown一次，当三个都运行完成，变为0。则指挥官停止等待。
		for (int i = 0; i < 3; i++) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						System.out.println("线程" + Thread.currentThread().getName() + "正准备接受命令");
						cdOrder.await(); // 战士们都处于等待命令状态
						System.out.println("线程" + Thread.currentThread().getName() + "已接受命令");
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println("线程" + Thread.currentThread().getName() + "回应命令处理结果");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						cdAnswer.countDown(); // 任务运行完成，返回给指挥官，cdAnswer减1。
					}
				}
			};
			service.execute(runnable);// 为线程池加入任务
		}
		try {
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println("线程" + Thread.currentThread().getName() + "即将公布命令");
			cdOrder.countDown(); // 发送命令，cdOrder减1，处于等待的战士们停止等待转去运行任务。
			System.out.println("线程" + Thread.currentThread().getName() + "已发送命令，正在等待结果");
			cdAnswer.await(); // 命令发送后指挥官处于等待状态。一旦cdAnswer为0时停止等待继续往下运行
			System.out.println("线程" + Thread.currentThread().getName() + "已收到全部响应结果");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		service.shutdown(); // 任务结束。停止线程池的全部线程
	}
	
}