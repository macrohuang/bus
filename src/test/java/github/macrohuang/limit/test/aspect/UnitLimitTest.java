package github.macrohuang.limit.test.aspect;

import github.macrohuang.limit.core.BaseTest;
import github.macrohuang.limit.exception.BizdevIncokeLimitationException;
import github.macrohuang.limit.test.service.MyService;

import org.junit.Test;


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
