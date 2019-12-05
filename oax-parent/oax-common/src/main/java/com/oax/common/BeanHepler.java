package com.oax.common;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanHepler {
	
	private static BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
	
	private static PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
	
	static {
		// beanUtilsBean.getConvertUtils().register(converter, Date.class);
	}

	/**
	 * 基于反射的field拷贝，支持采用注解的方式{@code Mapping}指定别名，别名支持表达式
	 *
	 * @param source
	 * @param destClass
	 * @return
	 */
	public static <T> T copyFields(Object source, Class<T> destClass) {
		T dest;
		try {
			dest = destClass.newInstance();
			Field[] fields = destClass.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isPublic(field.getModifiers())) {
					Mapping mapping = field.getAnnotation(Mapping.class);
					String fieldName = field.getName();
					if (mapping != null) {
						fieldName = mapping.value();
					}
					try {
						Object val = propertyUtilsBean.getProperty(source, fieldName);
						field.set(dest, val);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dest;
	}
	
	/**
	 * 基于setter/getter方式的属性copy
	 * 
	 * @param source
	 * @param destClass
	 * @return
	 */
	public static <T> T copy(Object source, Class<T> destClass) {
		
		T dest;
		try {
			dest = destClass.newInstance();
			if (null != source) {
				beanUtilsBean.copyProperties(dest, source);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dest;
	}
	
	/**
	 * 基于setter/getter方式的属性copy非空值
	 * 
	 * @param source
	 * @param dest
	 * @return
	 */
	
	public static <T> T copyNotNullValue(Object source, T dest) {
		try {
			Field[] fields = source.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(source);
				if (value == null || Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				field.set(dest, value);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dest;
	}
	
	/**
	 * 这个方法没有实现deep clone
	 * 
	 * @param obj
	 * @return
	 */
	public static Object clone(Object obj) {
		try {
			return beanUtilsBean.cloneBean(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> List<T> copyList(List<?> list, Class<T> clazz) {
		List<T> result = new ArrayList<>();
		if (list != null && list.size() != 0) {
			for (Object obj : list) {
				T vo = BeanHepler.copyFields(obj, clazz);
				result.add(vo);
			}
		}
		return result;
	}
	
	/**
	 * 基于getter/setter的方式复制列表
	 * 
	 * @param list
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> copyListByProperties(List<?> list, Class<T> clazz) {
		List<T> result = new ArrayList<>();
		if (list != null && list.size() != 0) {
			for (Object obj : list) {
				T vo = BeanHepler.copy(obj, clazz);
				result.add(vo);
			}
		}
		
		return result;
	}

	public static List<Map<String, Object>> copyListToMapList(List<?> list) {
		List<Map<String, Object>> result = new ArrayList<>();
		if (list != null && list.size() != 0) {
			for (Object obj : list) {
				Map<String, Object> map = BeanHepler.copyToMap(obj);
				result.add(map);
			}
		}
		return result;
	}

	public final static <T> Map<String, Object> copyToMap(T object) {
		if (object == null) {
			return new HashMap<>();
		}
		Field[] fields = object.getClass().getDeclaredFields();
		Map<String, Object> map = new HashMap<>();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object value = field.get(object);
				if (field.getType().equals(String.class)) {
					if (StringUtils.isBlank((String) value)) {
						continue;
					}
				}
				map.put(fieldName, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return map;
	}

	/**
	 * 把对象中的枚举类型变成ordinal，数字
	 */
	public final static <T> Map<String, Object> copyToMapEnumOrdinal(T object) {
		if (object == null) {
			return new HashMap<>();
		}
		Field[] fields = object.getClass().getDeclaredFields();
		Map<String, Object> map = new HashMap<>();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object value = field.get(object);
				if (field.getType().equals(String.class)) {
					if (StringUtils.isBlank((String) value)) {
						continue;
					}
				}
				if (field.getType().isEnum()) {
					if (value != null) {
						Enum enumValue = (Enum) value;
						map.put(fieldName, enumValue.ordinal());
					}
				}else {
					map.put(fieldName, value);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * 把src中的属性值复制到dest中对应的属性中
	 * 基于setter/getter
	 * @param src
	 * @param dest
	 */
	public static void copySrcToDest(Object src, Object dest) {

		Map<String, Object> srcMap = new HashMap<>();
		Field[] srcFields = src.getClass().getDeclaredFields();
		for (Field field : srcFields) {
			field.setAccessible(true);
			try {
				srcMap.put(field.getName(), field.get(src)); //获取属性值
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Field[] destFields = dest.getClass().getDeclaredFields();
		for (Field field : destFields) {
			field.setAccessible(true);
			if (srcMap.get(field.getName()) == null) {
				continue;
			}
			try {
				field.set(dest, srcMap.get(field.getName())); //给属性赋值
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 把src中的不为空的属性值复制到dest中对应的属性中
	 * 基于setter/getter
	 * @param src
	 * @param dest
	 */
	public static void copyNotNullValueSrcToDest(Object src, Object dest) {

		Map<String, Object> srcMap = new HashMap<>();
		Field[] srcFields = src.getClass().getDeclaredFields();
		for (Field field : srcFields) {
			field.setAccessible(true);
			try {
				if (field.get(src) == null || Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())){
					continue;
				}
				srcMap.put(field.getName(), field.get(src)); //获取属性值
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Field[] destFields = dest.getClass().getDeclaredFields();
		for (Field field : destFields) {
			field.setAccessible(true);
			if (srcMap.get(field.getName()) == null) {
				continue;
			}
			try {
				field.set(dest, srcMap.get(field.getName())); //给属性赋值
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
