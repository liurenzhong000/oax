package com.oax.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.oax.Constant;
import com.oax.common.AliOSSUtil;
import com.oax.common.ResultResponse;
import com.oax.service.I18nMessageService;

@RestController
@RequestMapping("/fileUpload")
public class FileUploadController {
	
	  @Value("${pic.upload.dir}")
	  private String picDir;
	  
	  @Value("${pic.upload.pre}")
	  private String picPre;

      @Value("${pic.upload.endPoint}")
      private String endPoint;

      @Value("${pic.upload.bucketName}")
      private String bucketName;
	  
	  @Autowired
	  private I18nMessageService I18nMessageService;
	
		/**
		 * 图片上传
		 * @param request
		 * @return
		 */
	  @RequestMapping("/uploadPic")  
	  public ResultResponse upload(HttpServletRequest request) throws IOException {
          // 创建一个通用的多部分解析器  
          CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
          String lang=request.getHeader(Constant.api_header_lang);
          List<String> files = new ArrayList<>();  
          boolean success=false;
          
          if (multipartResolver.isMultipart(request)) {  
              // 转换成多部分request  
              MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;  
              // 取得request中的所有文件名  
              Iterator<String> iter = multiRequest.getFileNames();  
              
              while (iter.hasNext()) {
            	  success=true;
                  // 取得上传文件  
                  MultipartFile file = multiRequest.getFile(iter.next());  
                  if (file != null) {  
                      String fileName = file.getOriginalFilename();  
                      if (fileName!=null &&!"".equals(fileName.trim())) {  
                    	  String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
                    	  fileName=UUID.randomUUID().toString().replace("-", "")+"."+suffix;
                          File newFile = new File(fileName);  
                          FileOutputStream outStream = new FileOutputStream(newFile);
                          outStream.write(file.getBytes());  
                          outStream.close(); 
                          file.transferTo(newFile);  
                          // 上传到阿里云  
                          Date date = new Date();  
                          String dir = new SimpleDateFormat("yyyy/MM/dd").format(date);
                          files.add(picPre+AliOSSUtil.uploadFile(endPoint,bucketName,newFile, picDir+"/idCard/"+dir+"/"));
                          newFile.delete();  
                      }  
                  }  
              }  
          }  
          if(success) {
        	  return new ResultResponse(success, files);
          }else {
        	  return new ResultResponse(success, I18nMessageService.getMsg(10064, lang));
          }
     
	  }  
}
