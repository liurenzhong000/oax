package com.oax.common.json;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public interface TypeReferences {
	TypeReference<Map<String, Object>> REF_MAP_OBJECT = new TypeReference<Map<String, Object>>() {
	};

	TypeReference<Map<String, String>> REF_MAP_STRING = new TypeReference<Map<String, String>>() {
	};

	TypeReference<List<Map<String, Object>>> REF_LIST_MAP_OBJECT = new TypeReference<List<Map<String, Object>>>() {
	};

	TypeReference<List<Map<String, String>>> REF_LIST_MAP_STRING = new TypeReference<List<Map<String, String>>>() {
	};

	TypeReference<Map<String, Map<String, Object>>> REF_MAP_MAP_OBJECT = new TypeReference<Map<String, Map<String, Object>>>() {
	};

	TypeReference<List<String>> REF_LIST_STRING = new TypeReference<List<String>>() {
	};

}
