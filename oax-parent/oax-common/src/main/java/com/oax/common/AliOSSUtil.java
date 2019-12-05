package com.oax.common;

import java.io.File;

import org.apache.log4j.Logger;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;


public class AliOSSUtil {
    private final static Logger logger = Logger.getLogger(AliOSSUtil.class);
    private final static String ACCESS_KEY_ID = "LTAIQDLWQ1yGUcrS";
    private final static String ACCESS_KEY_SECRET = "mqUFcmwTpuhchzrsiUlpHK1dXu2SBY";

    /**
     * @param file 本地文件
     * @param dir  bucket目录
     * @return 访问地址
     */
    public static String uploadFile(String endPoint,String bucketName,File file, String dir) {
        if (file == null) {
            return null;
        }
        String filePath = dir + file.getName();
        OSSClient ossClient = new OSSClient(endPoint, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, filePath, file));
            if (null != result) {
                return filePath;
            } else {
                return null;
            }
        } catch (OSSException | ClientException oe) {
            logger.error("picture upload fail:", oe);
            oe.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
    }
}
