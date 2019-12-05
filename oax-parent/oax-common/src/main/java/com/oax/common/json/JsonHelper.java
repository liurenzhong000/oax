package com.oax.common.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class JsonHelper {

	public static final ObjectMapper OM = new ObjectMapper();

	static {
		OM.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		OM.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}

	public static JsonFactory getJsonFactory() {
		return OM.getFactory();
	}

	public static <T> T readValue(String json, Class<T> type) {
		if (json == null) {
			return null;
		}
		try {
			return (T) OM.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> T readValue(String json, TypeReference<T> type) {
		if (json == null) {
			return null;
		}
		try {
			return OM.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> T readValue(byte[] content, int offset, int len, TypeReference<T> ref) {
		if (content == null) {
			return null;
		}
		try {
			return OM.readValue(content, offset, len, ref);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> T readValue(byte[] content, int offset, int len, Class<T> type) {
		if (content == null) {
			return null;
		}
		try {
			return (T) OM.readValue(content, offset, len, type);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> String writeValueAsString(T value) {
		try {
			return OM.writeValueAsString(value);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> void writeValue(OutputStream out, T value) {
		try {
			OM.writeValue(out, value);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> void writeValue(Writer out, T value) {
		try {
			OM.writeValue(out, value);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> T convert(Object value, Class<T> type) {
		try {
			return OM.convertValue(value, type);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static <T> T convert(Object value, TypeReference<T> type) {
		try {
			return OM.convertValue(value, type);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
