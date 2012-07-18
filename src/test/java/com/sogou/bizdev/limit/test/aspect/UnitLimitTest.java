package com.sogou.bizdev.limit.test.aspect;

import org.junit.Test;

import com.sogou.bizdev.limit.core.BaseTest;
import com.sogou.bizdev.limit.test.service.MyService;

public class UnitLimitTest extends BaseTest {
	MyService myService;

	@Test
	public void testUnitLimit() {
		getMyService().service1();
	}

	public MyService getMyService() {
		return (MyService) applicationContext.getBean("myService");
	}

	public void setMyService(MyService myService) {
		this.myService = myService;
	}
}
