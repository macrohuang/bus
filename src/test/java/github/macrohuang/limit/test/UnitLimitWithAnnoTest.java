package github.macrohuang.limit.test;

import github.macrohuang.limit.exception.BizdevIncokeLimitationException;
import github.macrohuang.limit.test.service.MyService;
import github.macrohuang.limit.test.service.impl.MyServiceImpl;
import github.macrohuang.limit.wrapper.UnitLimitWrapper;

import java.lang.reflect.Method;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;


public class UnitLimitWithAnnoTest {
	UnitLimitWrapper unitLimitWrapper;
	MyService myService;

	@Before
	public void init() {
		unitLimitWrapper = new UnitLimitWrapper();
		myService = new MyServiceImpl();
		unitLimitWrapper.setPackagesToScan(new HashSet<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7793009169584923392L;

			{
				add("com.sogou.bizdev.limit.test.service");
			}
		});
	}

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testSecondLimit() throws SecurityException, NoSuchMethodException, InterruptedException {
		Method method = myService.getClass().getDeclaredMethod("service1");
		while (true) {
			unitLimitWrapper.beforeTargetInvokedManual(method);
			myService.service1();
			Thread.sleep((long) (10 * Math.random()));
		}
	}

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testMinuteLimit() throws SecurityException, NoSuchMethodException, InterruptedException {
		Method method = myService.getClass().getDeclaredMethod("service2");
		while (true) {
			unitLimitWrapper.beforeTargetInvokedManual(method);
			myService.service2();
			Thread.sleep((long) (100 * Math.random()));
		}
	}
}
