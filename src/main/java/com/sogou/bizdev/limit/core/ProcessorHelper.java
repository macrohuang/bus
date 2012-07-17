package com.sogou.bizdev.limit.core;

import java.lang.reflect.Method;

public final class ProcessorHelper {
	public static String getKey(Method method) {
		return String.format("%s.%s", method.getClass().getName(), method.getName());
	}
}

