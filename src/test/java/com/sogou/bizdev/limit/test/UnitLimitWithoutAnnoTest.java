package com.sogou.bizdev.limit.test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.services.AbstractLimitWrapper;
import com.sogou.bizdev.limit.services.UnitLimitWrapper;
import com.sogou.bizdev.limit.test.service.MyService;

public class UnitLimitWithoutAnnoTest {
	UnitLimitWrapper unitLimitWrapper;
	MyService myService;
	private String[] timeUntis = new String[] { "second", "minute", "hour", "day" };

	@Before
	public void init() {
		unitLimitWrapper = new UnitLimitWrapper();
		myService = new MyService();
		unitLimitWrapper.setSpecifiedMethods(new HashMap<String, Map<String, Object>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put(MyService.class.getName(), new HashMap<String, Object>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					{
						put(AbstractLimitWrapper.METHOD_KEY, new HashSet<String>() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							{
								add("service1");
							}
						});
						put(AbstractLimitWrapper.LIMIT_KEY, Math.min((long) (Integer.MAX_VALUE * Math.random()), (long) Math.pow(10, 9)) / 100000);
						put(AbstractLimitWrapper.TIME_UNIT_KEY, timeUntis[(int) (timeUntis.length * Math.random())]);
					}
				});
			}
		});
	}

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testUnitLimit() throws SecurityException, NoSuchMethodException, InterruptedException {
		Method method = myService.getClass().getDeclaredMethod("service1");
		while (true) {
			unitLimitWrapper.beforeTargetInvokedManual(method);
			myService.service1();
		}
	}
}
