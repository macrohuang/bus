package com.sogou.bizdev.limit.test.aspect;

import org.junit.Test;

import com.sogou.bizdev.limit.core.BaseTest;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.test.service.MyService;

public class UnitLimitTest extends BaseTest {
	MyService myService;

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testUnitLimit() throws InterruptedException {
		while (true) {
			getMyService().service1();
			Thread.sleep((long) (10 * Math.random()));
		}
	}

	public MyService getMyService() {
		return (MyService) applicationContext.getBean("myService");
	}

	public void setMyService(MyService myService) {
		this.myService = myService;
	}
}
