package com.oax.common;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

public class HexHelper {
	
	public static String toHexString(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}
	
	public static byte[] toBytes(String hexString) {
		if(StringUtils.isBlank(hexString)){
			return null;
		}
		if(hexString.length()%2!=0){
			throw new IllegalArgumentException("illegal hex string");
		}
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}
}
