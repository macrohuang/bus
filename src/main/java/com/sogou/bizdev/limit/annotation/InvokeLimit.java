package com.sogou.bizdev.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sogou.bizdev.limit.annotation.UnitLimit.TimeUnit;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeLimit {
	public int value();

	public boolean async() default true;

	public long timeOut() default 60 * 1000;

	public TimeUnit unit() default TimeUnit.SECOND;
}