package com.sogou.bizdev.limit.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {
	public static boolean isGlobalLimitMethod(Method method) {
		return checkAnnotation(method, GlobalLimit.class);
	}

	public static boolean isInvokeLimitMethod(Method method) {
		return checkAnnotation(method, InvokeLimit.class);
	}

	public static boolean isUnitLimitMethod(Method method) {
		return checkAnnotation(method, UnitLimit.class);
	}

	private static <T extends Annotation> boolean checkAnnotation(Method method, Class<T> annotClass) {
		if (method == null)
			return false;
		return method.getAnnotation(annotClass) != null;
	}
}
