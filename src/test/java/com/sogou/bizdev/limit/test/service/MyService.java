package com.sogou.bizdev.limit.test.service;

import com.sogou.bizdev.limit.annotation.InvokeLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit.TimeUnit;

public class MyService {
	@UnitLimit(unit = TimeUnit.SECOND, value = 178767987)
	public void service1() {
		System.out.println("service1");
	}

	@UnitLimit(unit = TimeUnit.MINUTE, value = 1000)
	public void service2() {
		System.out.println("service2");
	}

	@UnitLimit(unit = TimeUnit.HOUR, value = 10000000)
	public void service3() {
		System.out.println("service3");
	}

	@InvokeLimit(unit = TimeUnit.SECOND, value = 100)
	public void service4(long uid) {
		System.out.println("service4:" + uid);
	}

	public void service5(long uid) {
		System.out.println("service5:" + uid);
	}

	public void service6() {
		System.out.println("service6");
	}
}
