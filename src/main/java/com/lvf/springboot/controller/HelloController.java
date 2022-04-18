package com.lvf.springboot.controller;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvf.springboot.callable.Kabc;
import com.lvf.springboot.model.User;
import com.lvf.springboot.opentsdb.OpentsdbTest;


@RequestMapping("/hello")
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(@RequestParam(value="username", required=false, defaultValue="helloworld") String username, Model model) {
        model.addAttribute("username", username);
        return "hello";
    }
    
	@Autowired
	Kabc kabc;
	
	@GetMapping("abc")
	public void abc(HttpServletRequest req) {
		System.out.println("fds");
		System.out.println("kabc.getUrl():" + kabc.getUrl());
	}
	
	
	@ResponseBody
	@GetMapping("/test")
	public Kabc test() {
		System.out.println("dd");
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	
	int sba = 4000;
	int countSba = 0;
	long start;
	long end;
	
	private void modifySba() {
		synchronized ("fdsfdsfdsfdsfdsfdsfdsfdsfdsfdsfds") {
			countSba++;
			if (countSba == 1) {
				start = System.currentTimeMillis();
			} else if (countSba == sba) {
				end = System.currentTimeMillis();
				System.out.println("cost :" + (end - start));
			}
		}
	}
	
	@ResponseBody
	@GetMapping("/testTomcatNio")
	public Kabc testTomcatNio(String sss) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					long current = System.currentTimeMillis();
					int count = Thread.activeCount();
					System.out.println("" + current +".Thread.activeCount:" + count);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	
	@ResponseBody
	@GetMapping("/countTomcatNio")
	public Kabc countTomcatNio(String sss) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					ThreadGroup currentGroup = 
						      Thread.currentThread().getThreadGroup();
					long current = System.currentTimeMillis();
				    int noThreads = currentGroup.activeCount();
					System.out.println("" + current +".Thread.activeCount:" + noThreads);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	
	@ResponseBody
	@GetMapping("/startTomcatNio")
	public Kabc startTomcatNio(String sss) {
		int a = 1;
		while (a == 1) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	
	@ResponseBody
	@GetMapping("/testTomcatNioRest")
	public Kabc testTomcatNioRest() {
		RestTemplate restTemplate = new RestTemplate();
		for (int i=1; i <= sba; i++) {
			final int j = i;
			new Thread(new Runnable() {
				public void run() {
					String url = "http://192.168.1.2:8080/hello/startTomcatNio?sss=" + j;
					String resultStr = null;
					JSONObject data = new JSONObject();
					try {
						resultStr = restTemplate.getForObject(url, String.class, data);
						System.out.println("sss=" + j + "---" + resultStr);
					} catch (Exception e) {
						System.out.println("testTomcatNioRest:response:error:resultStr:" + resultStr);
						System.out.println("testTomcatNioRest:response:error:" + e.getMessage());
					}
				}
			}).start();
		}
		return null;
	}
	
	@ResponseBody
	@GetMapping("/startTomcatNio2")
	public Kabc startTomcatNio2(String sss) {
		Kabc kabc = new Kabc();
		kabc.setUrl("startTomcatNio2.OK2");
		return kabc;
	}
	int sout = 0;
	@ResponseBody
	@GetMapping("/testTomcatNioRest2")
	public Kabc testTomcatNioRest2() {
		System.out.println("sout:" + sout);
		RestTemplate restTemplate = new RestTemplate();
		long start = System.currentTimeMillis();
		CountDownLatch cdl = new CountDownLatch(sba);
		Thread[] thds = new Thread[sba];
		int thdsInt = 0;
		for (int i = sout + 1; i <= sout + sba; i++) {
			final int j = i;
			thds[thdsInt ++] = new Thread(new Runnable() {
				public void run() {
					String url = "http://192.168.1.2:8080/hello/startTomcatNio2?sss=" + j;
					String resultStr = null;
					JSONObject data = new JSONObject();
					try {
						resultStr = restTemplate.getForObject(url, String.class, data);
						System.out.println("sss=" + j + "---" + resultStr);
						cdl.countDown();
					} catch (Exception e) {
						System.out.println("testTomcatNioRest:response:error:resultStr:" + resultStr);
						System.out.println("testTomcatNioRest:response:error:" + e.getMessage());
					}
				}
			});
		}
		
		for (Thread threadItem : thds) {
			threadItem.start();
		}
		
		try {
			cdl.await(1000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("sba:" + sba + "--- cdl.getCount:" + cdl.getCount() + "--- cost :" + (end - start));
		sout = sout + sba;
		Kabc kabc = new Kabc();
		kabc.setUrl("testTomcatNioRest2.OK2");
		return kabc;
	}
	
	@ResponseBody
	@GetMapping("/testTomcatNioRest3")
	public Kabc testTomcatNioRest3() {
		System.out.println("sout:" + sout);
		RestTemplate restTemplate = new RestTemplate();
		long start = System.currentTimeMillis();
		CountDownLatch cdl = new CountDownLatch(sba);
		Thread[] thds = new Thread[sba];
		int thdsInt = 0;
		for (int i = sout + 1; i <= sout + sba; i++) {
			final int j = i;
			thds[thdsInt ++] = new Thread(new Runnable() {
				public void run() {
					String url = "http://192.168.1.4:8080/user/getUserInfo?sss=" + j;
					String resultStr = null;
					JSONObject data = new JSONObject();
					try {
						resultStr = restTemplate.getForObject(url, String.class, data);
						System.out.println("sss=" + j + "---" + resultStr);
						cdl.countDown();
					} catch (Exception e) {
						System.out.println("testTomcatNioRest:response:error:resultStr:" + resultStr);
						System.out.println("testTomcatNioRest:response:error:" + e.getMessage());
					}
				}
			});
		}
		
		for (Thread threadItem : thds) {
			threadItem.start();
		}
		
		try {
			cdl.await(1000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("sba:" + sba + "--- cdl.getCount:" + cdl.getCount() + "--- cost :" + (end - start));
		sout = sout + sba;
		Kabc kabc = new Kabc();
		kabc.setUrl("testTomcatNioRest2.OK2");
		return kabc;
	}
	
	//http://192.168.1.4:8082/hello/test2
	//nohup java -Xmx9216m -Xms9216m -jar myboot-0.0.1.jar &
	@ResponseBody
	@GetMapping("/test2")
	public void test2() {
		int max = 5000;
		long start = System.currentTimeMillis();
		final CountDownLatch cdl = new CountDownLatch(max);
		final CountDownLatch cd2 = new CountDownLatch(max);
//		CountDownLatch cd3 = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		for (int index = 1 ; index <= max ; index ++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot2();
					cdl.countDown();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot3();
					cd2.countDown();
				}
			}).start();
			
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot5();
					cd3.countDown();
				}
			}).start();*/
		}
		try {
			cdl.await(60 , TimeUnit.SECONDS);
			cd2.await(60 , TimeUnit.SECONDS);
//			cd3.await(60 , TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
		System.out.println("1yes:" + openTest.yes.intValue());
		System.out.println("1no:" + openTest.no.intValue());
		System.out.println("2yes:" + openTest.yes2.intValue());
		System.out.println("2no:" + openTest.no2.intValue());
//		System.out.println("3yes:" + openTest.yes3.intValue());
//		System.out.println("3no:" + openTest.no3.intValue());
	}
	//http://192.168.1.4:8082/hello/test3
	@ResponseBody
	@GetMapping("/test3")
	public void test3() {
		int max = 10000;
		long start = System.currentTimeMillis();
		final CountDownLatch cdl = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		for (int index = 1 ; index <= max ; index ++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot2();
					cdl.countDown();
				}
			}).start();
			
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					openTest.testLocalhostSpringBoot3();
//					cd2.countDown();
//				}
//			}).start();
			
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot5();
					cd3.countDown();
				}
			}).start();*/
		}
		try {
			cdl.await(60 , TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
		System.out.println("1yes:" + openTest.yes.intValue());
		System.out.println("1no:" + openTest.no.intValue());
	}
	
	//http://192.168.1.4:8082/hello/test5
	@ResponseBody
	@GetMapping("/test5")
	public void test5() {
		int max = 10000;
		long start = System.currentTimeMillis();
		final CountDownLatch cdl = new CountDownLatch(max);
		final CountDownLatch cd2 = new CountDownLatch(max);
		final CountDownLatch cd3 = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		for (int index = 1 ; index <= max ; index ++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot2();
					cdl.countDown();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot3();
					cd2.countDown();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot5();
					cd3.countDown();
				}
			}).start();
		}
		try {
			cdl.await(60 , TimeUnit.SECONDS);
			cd2.await(60 , TimeUnit.SECONDS);
			cd3.await(60 , TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
		System.out.println("1yes:" + openTest.yes.intValue());
		System.out.println("1no:" + openTest.no.intValue());
		System.out.println("2yes:" + openTest.yes2.intValue());
		System.out.println("2no:" + openTest.no2.intValue());
		System.out.println("3yes:" + openTest.yes3.intValue());
		System.out.println("3no:" + openTest.no3.intValue());
	}
	
	//http://192.168.1.5:8082/hello/test6
	@ResponseBody
	@GetMapping("/test6")
	public void test6() {
		int max = 2001;
		long start = System.currentTimeMillis();
		final CountDownLatch cdl = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		
		AtomicInteger yes = new AtomicInteger();
		AtomicInteger no = new AtomicInteger();
		String ip = "192.168.1.5:8082";
		
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + ip + "/singleList/ok";
		

		for (int index = 1 ; index <= max ; index ++) {
			
			final int i = index;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//JSONObject data = new JSONObject();
					User user = new User();
		    		user.setId(UUID.randomUUID().toString());
		    		user.setAge(i);
		    		user.setName("jack" + i);
		    		user.setPassword("password" + i);
		    		
		    		JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(user));
		    		
		    		String resultStr = null;
					try {
						resultStr = restTemplate.postForObject(url, data, String.class);
						yes.incrementAndGet();
					} catch (Exception e) {
						no.incrementAndGet();
						System.out.println("hello:response:error:resultStr:" + resultStr);
						System.out.println("hello:response:error:" + e.getMessage());
					}
					cdl.countDown();
				}
			}).start();
			
		}
		try {
			cdl.await(60 , TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
		System.out.println("1yes:" + yes.intValue());
		System.out.println("1no:" + no.intValue());
	}
	
	//http://192.168.1.5:8082/hello/test7
	@ResponseBody
	@GetMapping("/test7")
	public void test7(int maxx) {
		int max = maxx;
		long start = System.currentTimeMillis();
		final CountDownLatch cdl = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		
		AtomicInteger yes = new AtomicInteger();
		AtomicInteger no = new AtomicInteger();
		String ip = "192.168.1.5:8082";
		
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + ip + "/singleList/ok";
		

		for (int index = 1 ; index <= max ; index ++) {
			
			final int i = index;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					//JSONObject data = new JSONObject();
					User user = new User();
		    		user.setId(UUID.randomUUID().toString());
		    		user.setAge(i);
		    		user.setName("jack" + i);
		    		user.setPassword("password" + i);
		    		
		    		JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(user));
		    		
		    		String resultStr = null;
					try {
						resultStr = restTemplate.postForObject(url, data, String.class);
						yes.incrementAndGet();
					} catch (Exception e) {
						no.incrementAndGet();
						System.out.println("hello:response:error:resultStr:" + resultStr);
						System.out.println("hello:response:error:" + e.getMessage());
					}
					cdl.countDown();
				}
			}).start();
			
		}
		try {
			cdl.await(60 , TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
		System.out.println("1yes:" + yes.intValue());
		System.out.println("1no:" + no.intValue());
	}
	
	//http://192.168.1.5:8082/hello/test8?maxx=202313
		@ResponseBody
		@GetMapping("/test8")
		public void test8(int maxx) {
			int max = maxx;
			long start = System.currentTimeMillis();
			final CountDownLatch cdl = new CountDownLatch(max);
			
			Semaphore spe = new Semaphore(5000);
			
			AtomicInteger yes = new AtomicInteger();
			AtomicInteger no = new AtomicInteger();
			String ip = "192.168.1.5:8082";
			
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://" + ip + "/singleList/ok";
			

			for (int index = 1 ; index <= max ; index ++) {
				
				final int i = index;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						//JSONObject data = new JSONObject();
						
						try {
							spe.acquire();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						User user = new User();
			    		user.setId(UUID.randomUUID().toString());
			    		user.setAge(i);
			    		user.setName("jack" + i);
			    		user.setPassword("password" + i);
			    		
			    		JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(user));
			    		
			    		String resultStr = null;
						try {
							resultStr = restTemplate.postForObject(url, data, String.class);
							yes.incrementAndGet();
						} catch (Exception e) {
							no.incrementAndGet();
							System.out.println("hello:response:error:resultStr:" + resultStr);
							System.out.println("hello:response:error:" + e.getMessage());
						}
						spe.release();
						cdl.countDown();
					}
				}).start();
				
			}
			try {
				cdl.await(60 , TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long end = System.currentTimeMillis();
			System.out.println("it's done");
			System.out.println("cost.mis:" + (end - start));
			System.out.println("cost.sec:" + (end - start)/1000);
			System.out.println("1yes:" + yes.intValue());
			System.out.println("1no:" + no.intValue());
		}
    
}
