package github.macrohuang.limit.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class BaseTest {
	protected ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	protected final int MAX_CALL = 10000;
	protected ExecutorService executorService = Executors.newFixedThreadPool(10);
}
