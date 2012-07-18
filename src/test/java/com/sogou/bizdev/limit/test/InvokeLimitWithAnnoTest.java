package com.sogou.bizdev.limit.test;

import java.lang.reflect.Method;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.services.InvokeLimitWrapper;
import com.sogou.bizdev.limit.test.service.MyService;
import com.sogou.bizdev.limit.test.service.impl.MyServiceImpl;

public class InvokeLimitWithAnnoTest {
	class Starter implements Runnable {
		private long uid;
		private Method method;

		public Starter(Method method, long uid) {
			this.uid = uid;
			this.method = method;
		}

		@Override
		public void run() {
			for (int i = 0; i < 1000000; i++) {
				invokeLimitWrapper.beforeTargetInvokedManual(method, uid);
				myService.service4(uid);
			}
		}
	}
	InvokeLimitWrapper invokeLimitWrapper;
	MyService myService;

	@Before
	public void init() {
		invokeLimitWrapper = new InvokeLimitWrapper();
		myService = new MyServiceImpl();
		invokeLimitWrapper.setPackagesToScan(new HashSet<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7793009169584923392L;

			{
				add("com.sogou.bizdev.limit.test.service");
			}
		});
	}

	@Test
	public void testSecondLimit() throws SecurityException, NoSuchMethodException, InterruptedException {
		Method method = myService.getClass().getDeclaredMethod("service4", new Class<?>[] { long.class });
		int size = 10;
		long[] ids = new long[size];
		for (int i = 0; i < size; i++) {
			ids[i] = (long) (Long.MAX_VALUE * Math.random());
		}

		for (int i = 0; i < size; i++) {
			try {
				new Thread(new Starter(method, ids[i])).start();
				Thread.sleep((long) (100 * Math.random()));
			} catch (InterruptedException e) {
			} catch (Exception e) {
				Assert.assertTrue(e instanceof BizdevIncokeLimitationException);
			}
		}
	}

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testSyncInvokeUntilTimeout() throws SecurityException, NoSuchMethodException, InterruptedException {
		Method method = myService.getClass().getDeclaredMethod("service5", new Class<?>[] { long.class });
		while (true) {
			invokeLimitWrapper.beforeTargetInvokedManual(method, 1L);
			myService.service5(1L);
			Thread.sleep((long) (400 * Math.random()));
		}
	}

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testSyncInvoke() throws SecurityException, NoSuchMethodException, InterruptedException {
		Method method = myService.getClass().getDeclaredMethod("service5", new Class<?>[] { long.class });
		while (true) {
			int rand = (int) (100 * Math.random());
			invokeLimitWrapper.beforeTargetInvokedManual(method, 1L);
			myService.service5(1L);
			if (rand % 2 == 0)
			invokeLimitWrapper.afterTargetInvokedManual(method, 1L);
		}
	}

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testSyncInvokeNoTimeout() throws SecurityException, NoSuchMethodException, InterruptedException {
		Method method = myService.getClass().getDeclaredMethod("service6", new Class<?>[] { long.class });
		while (true) {
			invokeLimitWrapper.beforeTargetInvokedManual(method, 2L);
			myService.service6(2L);
			if ((int) (100 * Math.random()) % 2 == 0) {
				invokeLimitWrapper.afterTargetInvokedManual(method, 2L);
			}
		}
	}
}
