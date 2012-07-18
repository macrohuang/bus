package github.macrohuang.limit.annotation;

import github.macrohuang.limit.annotation.TimeUnitLimitCall.TimeUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeeLimitCall {
	public int value();

	public TimeUnit unit() default TimeUnit.SECOND;
}