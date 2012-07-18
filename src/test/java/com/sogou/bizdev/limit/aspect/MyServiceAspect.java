package com.sogou.bizdev.limit.aspect;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import com.sogou.bizdev.limit.services.InvokeLimitWrapper;
import com.sogou.bizdev.limit.services.UnitLimitWrapper;

@Service
@Aspect
public class MyServiceAspect {
	UnitLimitWrapper unitLimitWrapper = new UnitLimitWrapper();
	InvokeLimitWrapper invokeLimitWrapper = new InvokeLimitWrapper();

	@PostConstruct
	public void init() {
		unitLimitWrapper.setPackagesToScan(new HashSet<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7586298984894810548L;

			{
				add("com.sogou.bizdev.limit.test.service");
			}
		});

		invokeLimitWrapper.setPackagesToScan(new HashSet<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7586298984894810548L;

			{
				add("com.sogou.bizdev.limit.test.service");
			}
		});
	}

	@Pointcut("execution(com.sogou.bizdev.limit.test.service MyService.service*(..))")
	public void invokeService1() {

	}

	@Before("invokeService1()")
	public void beforeServiceInvoke(JoinPoint jp) {
		unitLimitWrapper.beforeTargetInvoked(jp);
		invokeLimitWrapper.beforeTargetInvoked(jp);
	}

	@AfterReturning("invokeService1()")
	public void afterServiceInvoke(JoinPoint jp) {
		invokeLimitWrapper.afterTargetInvoked(jp);
	}
}
