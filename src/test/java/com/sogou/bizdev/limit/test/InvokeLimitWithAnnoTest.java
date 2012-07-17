package com.sogou.bizdev.limit.test;

import java.lang.reflect.Method;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.services.InvokeLimitWrapper;
import com.sogou.bizdev.limit.test.service.MyService;

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
		myService = new MyService();
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
		int size = 100;
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
}
