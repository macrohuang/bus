package com.sogou.bizdev.limit.test.aspect;

import org.junit.Test;

import com.sogou.bizdev.limit.core.BaseTest;
import com.sogou.bizdev.limit.exception.BizdevIncokeLimitationException;
import com.sogou.bizdev.limit.test.service.MyService;

public class InvokeLimitTest extends BaseTest {
	MyService myService;

	@Test(expected = BizdevIncokeLimitationException.class)
	public void testInvokeLimit() throws InterruptedException {
		while (true) {
			getMyService().service5(1L);
			Thread.sleep((long) (100 * Math.random()));
		}
	}

	public MyService getMyService() {
		return (MyService) applicationContext.getBean("myService");
	}

	public void setMyService(MyService myService) {
		this.myService = myService;
	}
}
