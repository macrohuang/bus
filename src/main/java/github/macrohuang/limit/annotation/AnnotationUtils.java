package github.macrohuang.limit.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {

	public static boolean isInvokeLimitMethod(Method method) {
		return checkAnnotation(method, InvokeeLimitCall.class);
	}

	public static boolean isUnitLimitMethod(Method method) {
		return checkAnnotation(method, TimeUnitLimitCall.class);
	}

	private static <T extends Annotation> boolean checkAnnotation(Method method, Class<T> annotClass) {
		if (method == null)
			return false;
		return method.getAnnotation(annotClass) != null;
	}
}
