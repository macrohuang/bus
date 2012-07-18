package github.macrohuang.limit.test.aspect;

import github.macrohuang.limit.core.BaseTest;
import github.macrohuang.limit.exception.BizdevIncokeLimitationException;
import github.macrohuang.limit.test.service.MyService;

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Test;


public class SyncCallTest extends BaseTest {
	MyService myService;

	@Test
	public void testInvokeLimit() throws InterruptedException {
		int count = 0;
		while (true) {
			getMyService().service6(1L);
			count++;
			if (count == MAX_CALL) {
				System.out.println("Max call time hit, will break.");
				break;
			}
		}
	}

	@Test
	public void testSyncCallMultThread() throws SecurityException, NoSuchMethodException, InterruptedException {
		int size = 10;
		final long[] ids = new long[size];
		for (int i = 0; i < size; i++) {
			ids[i] = (long) (Long.MAX_VALUE * Math.random());
		}
		final CountDownLatch countDownLatch = new CountDownLatch(size);
		for (int i = 0; i < size; i++) {
			try {
				final long id = ids[(int) (size * Math.random())];
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						getMyService().service6(id);
						countDownLatch.countDown();
					}
				});
			} catch (Exception e) {
				Assert.assertTrue(e instanceof BizdevIncokeLimitationException);
			}
		}
		countDownLatch.await();
	}

	public MyService getMyService() {
		return (MyService) applicationContext.getBean("myService");
	}

	public void setMyService(MyService myService) {
		this.myService = myService;
	}
}
