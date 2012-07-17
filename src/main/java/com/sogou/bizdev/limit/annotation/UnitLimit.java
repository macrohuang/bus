package com.sogou.bizdev.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnitLimit {
	public enum TimeUnit {
		SECOND((long) Math.pow(10, 9)), MINUTE(60 * (long) Math.pow(10, 9)), HOUR(3600 * (long) Math.pow(10, 9)), DAY(24 * 3600 * (long) Math.pow(10,
				9));
		private TimeUnit(long ns) {
			this.ns = ns;
		}

		private long ns;

		public long getNs() {
			return ns;
		}
	}

	public TimeUnit unit() default TimeUnit.SECOND;

	public int value();
}