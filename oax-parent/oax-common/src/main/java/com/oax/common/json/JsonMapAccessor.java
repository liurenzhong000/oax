package com.oax.common.json;


import com.oax.common.AssertHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMapAccessor {

	private Map<String,Object> jsonMap;
	
	public JsonMapAccessor(Map<String,Object> jsonMap){
		this.jsonMap = jsonMap;
	}
	
	public Integer getInt(String key){
		return getInt(key,null);
	}
	
	public Integer getInt(String key,Integer defVal){
		Object val = getValue(key);
		AssertHelper.isTrue(val!=null||defVal!=null,"json field "+key+"[int] not exists");
		if(val==null)
			return defVal;
		if(val instanceof Integer){
			return (Integer)val;
		}
		if(val instanceof String){
			return Integer.parseInt((String)val);
		}
		return defVal;
	}
	
	public Long getLong(String key){
		return getLong(key,null);
	}
	
	public Long getLong(String key,Long defVal){
		Object val = getValue(key);
		AssertHelper.isTrue(val!=null||defVal!=null,"json field "+key+"[long] not exists");
		if(val==null)
			return defVal;
		if(val instanceof Long){
			return (Long)val;
		}else if(val instanceof Integer){
			return ((Integer)val).longValue();
		}
		return defVal;
	}
	
	public Boolean getBoolean(String key){
		Object val = getValue(key);
		if(val==null)
			return Boolean.FALSE;
		if(val instanceof Boolean){
			return (Boolean)val;
		}
		return Boolean.FALSE;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key,boolean nullable){
		Object val = getValue(key);
		if(val!=null&&val instanceof List){
			return (List<T>)val;
		}
		if(!nullable)
			throw new RuntimeException("json field "+key+"[list] not exists");
		return null;
	}
	
	public String getString(String key){
		return getString(key,null);
	}
	
	public String getString(String key,String defVal){
		Object val = getValue(key);
		AssertHelper.isTrue(val!=null||defVal!=null,"json field "+key+"[str] not exists");
		if(val!=null){
			return val.toString();
		}
		return defVal;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getMap(String key,boolean nullable){
		Object val = getValue(key);
		if(val!=null&&val instanceof Map){
			return (Map<String,Object>)val;
		}
		if(!nullable)
			throw new RuntimeException("json field "+key+"[map] not exists");
		return null;
	}
	
	public BigDecimal getBigDecimal(String key,int scale){
		return getBigDecimal(key, scale,true);
	}
	
	public BigDecimal getBigDecimal(String key,int scale,boolean nullable){
		Object val = getValue(key);
		if(val!=null){
			if(val instanceof BigDecimal){
				return (BigDecimal)val;
			}if(val instanceof Integer){
				return BigDecimal.valueOf((Integer)val);
			}if(val instanceof Double){
				return BigDecimal.valueOf((Double)val);
			}else if(val instanceof String){
				BigDecimal dec = new BigDecimal((String)val);
				return dec.setScale(scale, RoundingMode.DOWN);
			}
		}
		if(!nullable)
			throw new RuntimeException("json field "+key+"[decimal] not exists");
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Object getValue(String key){
		String[] keyChain = key.split("\\.");
		Map<String,Object> map = jsonMap;
		int nest = keyChain.length;
		Object val = null;
		for(int i=0;i<nest;i++){
			String pkey = keyChain[i];
			val = map.get(pkey);
			if(val==null){
				return null;
			}
			if(i!=(nest-1)&&(val instanceof Map)){
				map = (Map<String,Object>)val;
			}
		}
		return val;
	}
	
	public static void main(String[] args) {
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("id", "dd");
		Map<String,Object> info = new HashMap<>();
		info.put("name", "liuxiong");
		info.put("id", 1000000L);
		jsonMap.put("info", info);
		jsonMap.put("price", 11);
		List<String> friends = new ArrayList<>();
		friends.add("dddd");
		friends.add("ffff");
		jsonMap.put("friends", friends);
		String json = JsonHelper.writeValueAsString(jsonMap);
		System.out.println(json);
		jsonMap = JsonHelper.readValue(json, TypeReferences.REF_MAP_OBJECT);
		JsonMapAccessor acc = new JsonMapAccessor(jsonMap);
		System.out.println(acc.getBoolean("id"));
		System.out.println(acc.getString("info.name"));
		System.out.println(acc.getLong("info.id"));
		System.out.println(acc.getInt("info.id"));
		System.out.println(acc.getList("friends",false));
		System.out.println(acc.getBigDecimal("price", 2, false));
		System.out.println(acc.getMap("priceMap", true));
	}
}
