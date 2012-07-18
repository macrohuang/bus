package com.sogou.bizdev.limit.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTest {
	protected ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
}
