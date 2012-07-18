package com.sogou.bizdev.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sogou.bizdev.limit.annotation.TimeUnitLimitCall.TimeUnit;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeeLimitCall {
	public int value();

	public TimeUnit unit() default TimeUnit.SECOND;
}