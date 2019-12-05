package com.oax.common;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SMSUtil {


	//----------------国际版的麦炫通-----------------------
	static final String Usergnzh = "MXT800691";
	static final String Pswdgn = "Mxt800691";

	static final String Usergjzh ="MXT800692";
	static final String Pswdgj = "Mxt800692";

//	public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";

	public static final String REGEX_MOBILE = "^(((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8})$";

	public static String sendCodeChinaPhone(String phonenNo, String templateParam){

		if(phonenNo.startsWith("0086")) {
			phonenNo = phonenNo.substring(4);
		}
		return sendCode(phonenNo, templateParam);
	}

	public static String sendCode(String phonenNo, String templateParam){
		Map<String, String> params = new HashMap<String, String>();
		String account = null;
		String pswd = null;

		Pattern regex = Pattern.compile(REGEX_MOBILE);
		Matcher matcher = regex.matcher(phonenNo);
		if(matcher.matches()){
			account = Usergnzh;
			pswd = Pswdgn;
		}else{
			account = Usergjzh;
			pswd = Pswdgj;
		}
		params.put("account", account);
		params.put("pswd", pswd);
		params.put("mobile", phonenNo);
		params.put("needstatus", "false");
		params.put("msg",templateParam + "【新比特XBTC】");

		String response = HttpRequestUtil.sendPost("http://116.62.212.142/msg/HttpSendSM?",
				params);


		String str ;
		String resultCode = null;
		try {
			str = new String(response.getBytes(), "GBK");

			if (str.contains("GBK")) {
				str = str.replaceAll("GBK", "utf-8");
			}
			String[] strArray = str.split(",");
			if(strArray[1].equals("0")){
				resultCode = "Sucess";
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCode;
	}

	public static String getSmsCode(int charCount) {
		String charValue = "";
		for (int i = 0; i < charCount; i++) {
			char c = (char) (randomInt(0, 10) + '0');
			charValue += String.valueOf(c);
		}
		return charValue;
	}

	public static int randomInt(int from, int to) {
		Random r = new Random();
		return from + r.nextInt(to - from);
	}


	/**
	 * 测试
	 * */
	public static void main(String[] args) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("account", "MXT800691");
		params.put("pswd", "Mxt800691");
		params.put("mobile", "18750217682");
		params.put("needstatus", "false");
		params.put("msg", "1233【新比特XBTC】");

		String response = HttpRequestUtil.sendPost("http://116.62.212.142/msg/HttpSendSM?",
				params);

		String str;
		try {
			str = new String(response.getBytes(), "GBK");

			if (str.contains("GBK")) {
				str = str.replaceAll("GBK", "utf-8");
			}
			String[] strArray = str.split(",");
			System.out.println(strArray[1]);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
