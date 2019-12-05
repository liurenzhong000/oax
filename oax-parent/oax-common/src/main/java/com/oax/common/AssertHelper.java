package com.oax.common;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

public class AssertHelper {
	
	public static void notEmpty(Object o, RuntimeException exception) {
		if (o == null) {
			throw exception;
		} else if (o instanceof String) {
			if (EmptyHelper.isEmpty((String) o)) {
				throw exception;
			}
		} else if (o instanceof Collection) {
			if (EmptyHelper.isEmpty((Collection<?>) o)) {
				throw exception;
			}
		} else if (o instanceof Map) {
			if (EmptyHelper.isEmpty((Map<?, ?>) o)) {
				throw exception;
			}
		} else if (o instanceof Object[]) {
			if (EmptyHelper.isEmpty((Object[]) o)) {
				throw exception;
			}
		}
	}
	
	public static void stringLengthLessThan(String string, int length, RuntimeException exception) {
		if (string != null) {
			if (string.length() > length) {
				throw exception;
			}
		}
	}
	
	public static void stringLengthLessThan(String string, int length, String message) {
		stringLengthLessThan(string, length, new IllegalArgumentException(message));
	}
	
	public static void notEmpty(Object o, String message) {
		notEmpty(o, new IllegalArgumentException(message));
	}
	
	public static void isEmpty(Object o, String message) {
		isEmpty(o, new IllegalArgumentException(message));
	}
	
	public static void isEmpty(Object o, RuntimeException exception) {
		if (o instanceof String) {
			if (!EmptyHelper.isEmpty((String) o)) {
				throw exception;
			}
		} else if (o instanceof Collection) {
			if (!EmptyHelper.isEmpty((Collection<?>) o)) {
				throw exception;
			}
		} else if (o instanceof Map) {
			if (!EmptyHelper.isEmpty((Map<?, ?>) o)) {
				throw exception;
			}
		} else if (o instanceof Object[]) {
			if (!EmptyHelper.isEmpty((Object[]) o)) {
				throw exception;
			}
		} else {
			if (o != null) {
				throw exception;
			}
		}
	}
	
	public static void isTrue(boolean expression, RuntimeException exception) {
		if (!expression) {
			throw exception;
		}
	}
	
	public static void isTrue(boolean expression, String message) {
		isTrue(expression, new IllegalArgumentException(message));
	}
}
