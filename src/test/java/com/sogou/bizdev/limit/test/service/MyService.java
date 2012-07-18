package com.sogou.bizdev.limit.test.service;

import com.sogou.bizdev.limit.annotation.InvokeeLimitCall;
import com.sogou.bizdev.limit.annotation.SyncCall;
import com.sogou.bizdev.limit.annotation.TimeUnitLimitCall;
import com.sogou.bizdev.limit.annotation.TimeUnitLimitCall.TimeUnit;

public interface MyService {

	@TimeUnitLimitCall(unit = TimeUnit.SECOND, value = 500)
	public abstract void service1();

	@TimeUnitLimitCall(unit = TimeUnit.MINUTE, value = 1000)
	public abstract void service2();

	@TimeUnitLimitCall(unit = TimeUnit.HOUR, value = 10000000)
	public abstract void service3();

	@InvokeeLimitCall(unit = TimeUnit.SECOND, value = 100)
	public abstract void service4(long uid);

	@TimeUnitLimitCall(unit = TimeUnit.SECOND, value = 500)
	@InvokeeLimitCall(unit = TimeUnit.SECOND, value = 100)
	public abstract void service5(long uid);

	@SyncCall(timeout = 100)
	public abstract void service6(long uid);

}