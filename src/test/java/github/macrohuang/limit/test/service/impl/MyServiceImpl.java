package github.macrohuang.limit.test.service.impl;

import github.macrohuang.limit.test.service.MyService;

import org.apache.log4j.Logger;


public class MyServiceImpl implements MyService {
	private static final Logger LOGGER = Logger.getLogger(MyServiceImpl.class);
	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service1()
	 */
	@Override
	public void service1() {
		LOGGER.info("service1");
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service2()
	 */
	@Override
	public void service2() {
		LOGGER.info("service2");
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service3()
	 */
	@Override
	public void service3() {
		LOGGER.info("service3");
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service4(long)
	 */
	@Override
	public void service4(long uid) {
		LOGGER.info("service4:" + uid);
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service5(long)
	 */
	@Override
	public void service5(long uid) {
		LOGGER.info("service5:" + uid);
	}

	/* (non-Javadoc)
	 * @see com.sogou.bizdev.limit.test.service.impl.MyService#service6(long)
	 */
	@Override
	public void service6(long uid) {
		LOGGER.info("service6:" + uid);
	}
}
