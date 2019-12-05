package com.oax.common;

import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */
public class SMSUtil1 {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String PRODUCT = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String DOMAIN = "dysmsapi.aliyuncs.com";

    static final String ACCESS_KEY_ID = "LTAIlaVL48fVXoY5";
    static final String ACCESS_KEY_SECRET = "AUo55NfMXYC81iQM6pSXs1uLXAkrKr";

    public static String sendCode(String phonenNo,String templateCode,String templateParam) {

        try {
			//可自助调整超时时间
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");

			//初始化acsClient,暂不支持region化
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
			IAcsClient acsClient = new DefaultAcsClient(profile);

			//组装请求对象-具体描述见控制台-文档部分内容
			SendSmsRequest request = new SendSmsRequest();
			request.setMethod(MethodType.POST);
			//必填:待发送手机号
			request.setPhoneNumbers(phonenNo);
			//必填:短信签名-可在短信控制台中找到
			request.setSignName("OAX");
			//必填:短信模板-可在短信控制台中找到
			//request.setTemplateCode("SMS_134115311");
			request.setTemplateCode(templateCode);
			//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			//request.setTemplateParam("{\"code\":\""+code+"\"}");
			request.setTemplateParam(templateParam);

			//选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			//request.setSmsUpExtendCode("90997");

			//可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			request.setOutId("yourOutId");

			//hint 此处可能会抛出异常，注意catch
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			System.out.println(sendSmsResponse.getCode());
			// ("OK").equals(sendSmsResponse.getCode());
			return sendSmsResponse.getCode();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
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
    	new Thread(new Runnable() {
			@Override
			public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name","aaa");
                jsonObject.put("qty",1.2);
               // String s = SMSUtil.sendCode("17665329290", "SMS_138072444", jsonObject.toJSONString());
                //System.out.println(s);
			}
		}).start();
	}

}
