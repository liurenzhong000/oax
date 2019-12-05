package com.oax.common;

import java.util.Collection;
import java.util.Map;

public  class EmptyHelper {

	public static boolean isEmpty(String s, boolean trim) {
		if (s == null) {
			return true;
		}
		if (s.length() == 0) {
			return true;
		}
		if (trim) {
			if (s.trim().length() == 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(String s) {
		return isEmpty(s, true);
	}

	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.size() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Collection<?> collection){
		if(collection==null||collection.size()==0){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Object[] array){
		if(array==null||array.length==0){
			return true;
		}
		return false;
	}
}
