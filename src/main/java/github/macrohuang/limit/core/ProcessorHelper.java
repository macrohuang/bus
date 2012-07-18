package github.macrohuang.limit.core;

import github.macrohuang.limit.annotation.InvokeeLimitCall;
import github.macrohuang.limit.annotation.SyncCall;
import github.macrohuang.limit.annotation.TimeUnitLimitCall;

import java.lang.reflect.Method;


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

