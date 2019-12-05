package com.oax.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.EmailException;

/**
 * 邮件发送工具类
 */
public class EmailUtil {


    public static final int PORT = 465;
    public static final String HOST = "smtpdm.aliyun.com";   //smtp.exmail.qq.com
    public static final String FROMMAIL = "xbtc@email.xbtc.cx";
    public static final String USER = "xbtc@email.xbtc.cx";
    public static final String PWD = "XBTCaq454848";
    public static final String NIKENAME = "XBTC";
    private static EmailAPI instance = new EmailAPI();

    static {
        instance.setFrom(FROMMAIL);
        instance.setHostName(HOST);
        instance.setUserName(USER);
        instance.setPassword(PWD);
        instance.setNickName(NIKENAME);
    }

    /**
     * 发送简单HTML邮件
     *
     * @param to          --接收人邮件
     * @param subject     --发送主题
     * @param contextHtml --html发送内容
     * @param replaceText --如果html发送不了，就发送该内容
     * @param ssl         --布尔类型，是否安全发送
     */
    public static String sendHtmlMail(String to, String subject, String contextHtml, String replaceText, boolean ssl) {
        String resultStr = "";
        try {
            return resultStr = instance.sendHtmlEmail(to, NIKENAME, subject, contextHtml, replaceText, ssl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 发送简单HTML邮件 |群发
     *
     * @param to          --接收人邮件数姐
     * @param subject     --发送主题
     * @param contextHtml --html发送内容
     * @param replaceText --如果html发送不了，就发送该内容
     * @param ssl         --布尔类型，是否安全发送
     */
    public static String sendMulitHtmlMail(String subject, String contextHtml, String replaceText, boolean ssl, String... to) {
        String resultStr = "";
        try {
            return resultStr = instance.sendHtmlEmail(subject, contextHtml, replaceText, ssl, to);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 发送简单文本邮件
     *
     * @param to      --接收人邮件数姐
     * @param subject --发送主题
     * @param text    --文本内容
     * @param ssl     --布尔类型，是否安全发送
     */
    public static String sendTextMail(String to, String subject, String text, boolean ssl) {
        String resultStr = "";
        try {
            return resultStr = instance.sendSimpleEmail(to, NIKENAME, subject, text, ssl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 发送简单文本邮件 |群发
     *
     * @param to      --接收人邮件数姐
     * @param subject --发送主题
     * @param text    --文本内容
     * @param ssl     --布尔类型，是否安全发送
     */
    public static String sendMulitTextMail(String subject, String text, boolean ssl, String... to) {
        String resultStr = "";
        try {
            return resultStr = instance.sendSimpleEmail(subject, text, ssl, to);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 发送带附件的简单文本 |群发
     *
     * @param subject 邮件主题
     * @param msg     邮件内容
     * @param name    附件名称
     * @param desc    附件描述
     * @param path    附件路径
     * @param ssl     是否使用SSL进行连接 param recipients 接收人列表
     */
    public static String sendAttachmentEmail(String subject, String msg, String name, String desc, String path, boolean ssl,
                                             String... to) {

        String resultStr = "";
        try {
            return resultStr = instance.sendAttachmentEmail(subject, msg, name, desc, path, ssl, to);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 发送邮件：带附件(网络文件)的简单邮件，纯文本
     *
     * @param to       接收人
     * @param nickName 接收人别名
     * @param subject  邮件主题
     * @param msg      邮件内容
     * @param name     附件名称
     * @param desc     附件描述
     * @param url      网络文件地址
     * @param ssl      是否使用SSL进行连接
     */
    public String sendAttachmentEmail(String to, String subject, String msg, String name, String desc, URL url, boolean ssl) {
        String resultStr = "";
        try {
            return resultStr = instance.sendAttachmentEmail(to, NIKENAME, subject, msg, name, desc, url, ssl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }


    /**
     * 测试
     */
    public static void main(String[] args) throws MalformedURLException, EmailException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resultStr = EmailUtil.sendHtmlMail("1048106695@qq.com", "邮件title", "验证码是：xxxx_hotmail333", "" +
                        "", true);
                System.out.println(resultStr);
                System.out.println("发送成功！");
            }
        }).start();

    }

}
