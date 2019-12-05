package com.oax.admin.controller;

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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocumentType;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.oax.common.AliOSSUtil;
import com.oax.common.ResultResponse;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/7
 * Time: 14:08
 * <p>
 * 上传文件 Controller
 */

@RestController
@RequestMapping("fileUpload")
public class FileUploadController {

    @Value("${pic.upload.dir}")
    private String picDir;

    @Value("${pic.upload.dir.banner}")
    private String banner;
    @Value("${pic.upload.dir.coin}")
    private String coin;
    @Value("${pic.upload.dir.article}")
    private String article;
    @Value("${pic.upload.dir.appfile}")
    private String app;

    @Value("${pic.upload.dir.appfile.iosPrefix}")
    private String iosPrefix;

    @Value("${pic.upload.dir.file_host}")
    private String FILE_HOST;
    @Value("${pic.upload.dir.file_host_https}")
    private String FILE_HOST_HTTPS;

    @Value("${pic.upload.endPoint}")
    private String endPoint;
    @Value("${pic.upload.bucketName}")
    private String bucketName;


    /**
     * 图片上传
     *
     * @param request
     * @return
     */
    @PostMapping("/uploadPic")
    public ResultResponse upload(@RequestParam("picType") Integer picType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        List<String> files = new ArrayList<>();

        String baseURL;
        if (picType == 1) {
            baseURL = this.banner;
        } else if (picType == 2) {
            baseURL = this.coin;
        } else if (picType == 3) {
            baseURL = this.article;
        } else {
            return new ResultResponse(false, "不存的图片类型");
        }

        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    String fileName = file.getOriginalFilename();
                    if (StringUtils.isNoneEmpty(fileName)) {
                        /*
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
                        //图片压缩
                        Thumbnails.of(file.getInputStream())
                                .addFilter(new ThumbnailsImgFilter())
                                .scale(1f)
                                .outputQuality(0.6)
                                .toFile(fileName);
                                */
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
                        File newFile = new File(fileName);
                        FileOutputStream outStream = new FileOutputStream(newFile);
                        outStream.write(file.getBytes());
                        outStream.close();
                        file.transferTo(newFile);

                        // 上传到阿里云
                        Date date = new Date();
                        String dir = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        files.add(FILE_HOST + AliOSSUtil.uploadFile(endPoint,bucketName,newFile, picDir + "/" + baseURL + "/" + dir + "/"));
                        newFile.delete();
                    }
                }
            }
        }
        return new ResultResponse(true, files);
    }


    @PostMapping("/uploadFile")
    public ResultResponse uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {


        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        List<String> files = new ArrayList<>();
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    String fileName = file.getOriginalFilename();
                    if (StringUtils.isNoneEmpty(fileName)) {
                        File newFile = new File(fileName);
                        FileOutputStream outStream = new FileOutputStream(newFile);
                        outStream.write(file.getBytes());
                        outStream.close();
                        file.transferTo(newFile);
                        // 上传到阿里云
                        Date date = new Date();
                        String dir = new SimpleDateFormat("yyyy/MM/dd").format(date);


                        String uploadFile = FILE_HOST_HTTPS + AliOSSUtil.uploadFile(endPoint,bucketName,newFile, picDir + "/" + app + "/" + dir + "/");
                        newFile.delete();
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if (suffix.equalsIgnoreCase("ipa")) {
                            //ios
                            String xmlFilename = fileName.substring(0, fileName.lastIndexOf("."));


                            String xmlDir = picDir + "/" + app + "/" + dir + "/";
                            String xmlfileUrl = iosPrefix + iosXmlCreate(xmlFilename + ".plist", uploadFile, xmlDir);
                            files.add(xmlfileUrl);
                        } else {
                            files.add(uploadFile);
                        }
                    }
                }
            }
        }
        return new ResultResponse(true, files);
    }


    private String iosXmlCreate(String filename, String fileUrl, String dir) throws IOException {
        Document document = DocumentHelper.createDocument();

        DocumentType documentType = new DOMDocumentType(
                "plist",
                "-//Apple//DTD PLIST 1.0//EN",
                "http://www.apple.com/DTDs/PropertyList-1.0.dtd");
        document.setDocType(documentType);
        Element plist = document.addElement("plist").addAttribute("version", "1.0");
        Element dict = plist.addElement("dict");
        dict.addElement("key").addText("items");
        Element array = dict.addElement("array");
        Element arrayDict = array.addElement("dict");


        arrayDict.addElement("key").addText("assets");
        Element arrayArray = arrayDict.addElement("array");
        Element arrayArrayDict = arrayArray.addElement("dict");
        arrayArrayDict.addElement("key").addText("kind");
        arrayArrayDict.addElement("string").addText("software-package");
        arrayArrayDict.addElement("key").addText("url");
        arrayArrayDict.addElement("string").addText(fileUrl);


        arrayDict.addElement("key").addText("metadata");
        Element arrayArrayDict1 = arrayDict.addElement("dict");
        arrayArrayDict1.addElement("key").addText("bundle-identifier");
        arrayArrayDict1.addElement("string").addText("com.xbtc.xbtc");
        arrayArrayDict1.addElement("key").addText("bundle-version");
        arrayArrayDict1.addElement("string").addText("1.0.0");
        arrayArrayDict1.addElement("key").addText("kind");
        arrayArrayDict1.addElement("string").addText("software");
        arrayArrayDict1.addElement("key").addText("title");
        arrayArrayDict1.addElement("string").addText("XBTC");
        OutputFormat format = OutputFormat.createPrettyPrint();
        File file = new File(filename);
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
        writer.write(document);
        writer.close();
        String s = FILE_HOST_HTTPS + AliOSSUtil.uploadFile(endPoint,bucketName,file, dir);
        FileUtils.deleteQuietly(file);
        return s;
    }
}
