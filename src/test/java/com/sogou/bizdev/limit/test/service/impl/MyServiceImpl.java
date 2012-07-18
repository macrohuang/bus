package com.sogou.bizdev.limit.test.service.impl;

import org.springframework.stereotype.Service;

import com.sogou.bizdev.limit.annotation.InvokeLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit;
import com.sogou.bizdev.limit.annotation.UnitLimit.TimeUnit;
import com.sogou.bizdev.limit.test.service.MyService;

@Service(value = "myService")
public class MyServiceImpl implements MyService {
	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service1()
	 */
	@Override
	@UnitLimit(unit = TimeUnit.SECOND, value = 500)
	public void service1() {
		System.out.println("service1");
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service2()
	 */
	@Override
	@UnitLimit(unit = TimeUnit.MINUTE, value = 1000)
	public void service2() {
		System.out.println("service2");
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service3()
	 */
	@Override
	@UnitLimit(unit = TimeUnit.HOUR, value = 10000000)
	public void service3() {
		System.out.println("service3");
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service4(long)
	 */
	@Override
	@InvokeLimit(unit = TimeUnit.SECOND, value = 100)
	public void service4(long uid) {
		System.out.println("service4:" + uid);
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service5(long)
	 */
	@Override
	@UnitLimit(unit = TimeUnit.SECOND, value = 500)
	@InvokeLimit(unit = TimeUnit.SECOND, value = 100, async = false, timeOut = 100)
	public void service5(long uid) {
		System.out.println("service5:" + uid);
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service6(long)
	 */
	@Override
	@InvokeLimit(unit = TimeUnit.SECOND, value = 100, async = false, timeOut = -1)
	public void service6(long uid) {
		System.out.println("service6");
	}
}
