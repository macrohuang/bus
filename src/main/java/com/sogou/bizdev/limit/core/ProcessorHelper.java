package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;

import com.sogou.bizdev.limit.annotation.InvokeeLimitCall;
import com.sogou.bizdev.limit.annotation.SyncCall;
import com.sogou.bizdev.limit.annotation.TimeUnitLimitCall;

public final class ProcessorHelper {
	public static String getKey(Method method) {
		return String.format("%s.%s", method.getClass().getName(), method.getName());
	}

	public static boolean hasInvokeLimitAnnotation(Method method) {
		return method.getAnnotation(InvokeeLimitCall.class) != null;
	}

	public static boolean hasTimeUnitLimitAnnotation(Method method) {
		return method.getAnnotation(TimeUnitLimitCall.class) != null;
	}

	public static boolean hasSyncCallAnnotation(Method method) {
		return method.getAnnotation(SyncCall.class) != null;
	}
}

