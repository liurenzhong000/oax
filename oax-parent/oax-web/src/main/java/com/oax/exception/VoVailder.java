package com.oax.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.alibaba.fastjson.JSONObject;

public class VoVailder {

	public static void valid(BindingResult result) throws VoException {
		
		String msg="";
		
		if(result.hasErrors()){
			
            for (ObjectError error : result.getAllErrors()) {
            	
            	JSONObject json=JSONObject.parseObject(JSONObject.toJSONString(error.getArguments()[0]));
                
            	msg+=json.getString("defaultMessage")+":"+error.getDefaultMessage()+"; ";
            }
        }
		
		if(!msg.equals("")) {
			throw new VoException(msg);
		}

	}
}
