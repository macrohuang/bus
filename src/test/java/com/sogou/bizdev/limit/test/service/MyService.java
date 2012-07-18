package com.sogou.bizdev.limit.test.service;

import com.sogou.bizdev.limit.annotation.InvokeLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit.TimeUnit;

public interface MyService {

	@UnitLimit(unit = TimeUnit.SECOND, value = 500)
	public abstract void service1();

	@UnitLimit(unit = TimeUnit.MINUTE, value = 1000)
	public abstract void service2();

	@UnitLimit(unit = TimeUnit.HOUR, value = 10000000)
	public abstract void service3();

	@InvokeLimit(unit = TimeUnit.SECOND, value = 100)
	public abstract void service4(long uid);

	@UnitLimit(unit = TimeUnit.SECOND, value = 500)
	@InvokeLimit(unit = TimeUnit.SECOND, value = 100, async = false, timeOut = 100)
	public abstract void service5(long uid);

	@InvokeLimit(unit = TimeUnit.SECOND, value = 100, async = false, timeOut = -1)
	public abstract void service6(long uid);

}