package com.oax.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * apche邮件工具类
 *
 * @author Jie
 * @date 2016年1月25日
 */
public class EmailAPI {

    private String hostName;
    //服务器 SSL协议
    private int port = 465;
    //本地 非SSL协议
//  private int port = 25;
    private String from;
    private String nickName;
    private String userName;
    private String password;

    public EmailAPI() {

    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 邮箱配置
     *
     * @param hostName 主机地址
     * @param port     Y端口号
     * @param from     Y 发件人地址
     * @param nickName N 发件人昵称
     * @param userName Y邮箱账号
     * @param password Y 密码
     */
    public EmailAPI(String hostName, int port, String from, String nickName, String userName, String password) {
        super();
        this.hostName = hostName;
        this.port = port;
        this.from = from;
        this.nickName = nickName;
        this.userName = userName;
        this.password = password;
    }

   
    /**
     * 发送邮件：简单邮件|纯文本
     *
     * @param recipient 接收人
     * @param nickName  接收人别名
     * @param subject   邮件主题
     * @param msg       邮件内容
     * @param ssl       是否使用SSL进行连接
     * @return 发送ID
     * @throws EmailException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendSimpleEmail(String recipient, String nickName, String subject, String msg, boolean ssl)
            throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.setFrom(getFrom(), getNickName());
        email.addTo(recipient, nickName, "UTF-8");
        email.setSubject(subject);
        email.setMsg(msg);
        email.setSSLOnConnect(ssl);
        return email.send();
    }

    /**
     * 发送邮件：简单邮件|群发|纯文本
     *
     * @param subject    邮件主题
     * @param msg        邮件内容
     * @param ssl        是否使用SSL进行连接
     * @param recipients 接收人列表
     * @return 发送ID
     * @throws EmailException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendSimpleEmail(String subject, String msg, boolean ssl, String... recipients) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.setFrom(getFrom(), getNickName());
        email.addTo(recipients);
        email.setSubject(subject);
        email.setMsg(msg);
        email.setSSLOnConnect(ssl);
        return email.send();
    }

    /**
     * 发送邮件：带附件(本地文件)的简单邮件，纯文本
     *
     * @param recipient 接收人
     * @param nickName  接收人别名
     * @param subject   邮件主题
     * @param msg       邮件内容
     * @param name      附件名称
     * @param desc      附件描述
     * @param path      附件路径
     * @param ssl       是否使用SSL进行连接
     * @return 发送ID
     * @throws EmailException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendAttachmentEmail(String recipient, String nickName, String subject, String msg, String name,
                                      String desc, String path, boolean ssl) throws EmailException {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(path);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(desc);
        attachment.setName(name);

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.addTo(recipient, nickName, "UTF-8");

        email.setFrom(getFrom(), getNickName());
        email.setSubject(subject);
        email.setMsg(msg);
        email.setSSLOnConnect(ssl);

        // add the attachment
        email.attach(attachment);

        // send the email
        return email.send();
    }

    /**
     * 发送邮件：带附件(本地文件)的简单邮件|群发|纯文本
     *
     * @param subject 邮件主题
     * @param msg     邮件内容
     * @param name    附件名称
     * @param desc    附件描述
     * @param path    附件路径
     * @param ssl     是否使用SSL进行连接 param recipients 接收人列表
     * @return 发送ID
     * @throws EmailException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendAttachmentEmail(String subject, String msg, String name, String desc, String path, boolean ssl,
                                      String... recipients) throws EmailException {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(path);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(desc);
        attachment.setName(name);

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthentication(getUserName(), getPassword());
        //email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.addTo(recipients);

        email.setFrom(getFrom(), getNickName());
        email.setSubject(subject);
        email.setMsg(msg);
        email.setSSLOnConnect(ssl);

        // add the attachment
        email.attach(attachment);

        // send the email
        return email.send();
    }

    /**
     * 发送邮件：带附件(网络文件)的简单邮件，纯文本
     *
     * @param recipient 接收人
     * @param nickName  接收人别名
     * @param subject   邮件主题
     * @param msg       邮件内容
     * @param name      附件名称
     * @param desc      附件描述
     * @param url       网络文件地址
     * @param ssl       是否使用SSL进行连接
     * @return 发送ID
     * @throws EmailException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendAttachmentEmail(String recipient, String nickName, String subject, String msg, String name,
                                      String desc, URL url, boolean ssl) throws EmailException {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setURL(url);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(desc);
        attachment.setName(name);

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthentication(getUserName(), getPassword());
        //email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.addTo(recipient, nickName, "UTF-8");
        email.setFrom(getFrom(), getNickName());
        email.setSubject(subject);
        email.setMsg(msg);
        email.setSSLOnConnect(ssl);

        // add the attachment
        email.attach(attachment);

        // send the email
        return email.send();
    }

    /**
     * 发送邮件：带附件(网络文件)的简单邮件 | 群发 | 纯文本
     *
     * @param subject   邮件主题
     * @param msg       邮件内容
     * @param name      附件名称
     * @param desc      附件描述
     * @param url       网络文件地址
     * @param ssl       是否使用SSL进行连接
     * @param recipient 接收人列表
     * @return 发送ID
     * @throws EmailException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendAttachmentEmail(String subject, String msg, String name, String desc, URL url, boolean ssl,
                                      String... recipients) throws EmailException {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setURL(url);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(desc);
        attachment.setName(name);

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthentication(getUserName(), getPassword());
        //email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.addTo(recipients);
        email.setFrom(getFrom(), getNickName());
        email.setSubject(subject);
        email.setMsg(msg);
        email.setSSLOnConnect(ssl);

        // add the attachment
        email.attach(attachment);

        // send the email
        return email.send();
    }

    /**
     * 发送邮件：HTML邮件
     *
     * @param recipient 接收人
     * @param nickName  接收人别名
     * @param subject   邮件主题
     * @param html      HTML模板
     * @param text      替代消息(不支持接收html邮箱有效)
     * @param ssl       是否使用SSL进行连接
     * @return 发送ID
     * @throws EmailException
     * @throws MalformedURLException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendHtmlEmail(String recipient, String nickName, String subject, String html, String text,
                                boolean ssl) throws EmailException, MalformedURLException {
        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.addTo(recipient, nickName, "UTF-8");
        email.setFrom(getFrom(), getNickName());
        email.setSubject(subject);
        email.setSSLOnConnect(ssl);

        // set the html message
        email.setHtmlMsg(html);

        // set the alternative message
        email.setTextMsg(text);

        // send the email
        return email.send();
    }

    /**
     * 发送邮件：HTML邮件 | 群发
     *
     * @param subject   邮件主题
     * @param html      HTML模板
     * @param text      替代消息(不支持接收html邮箱有效)
     * @param ssl       是否使用SSL进行连接
     * @param recipient 接收人列表
     * @return 发送ID
     * @throws EmailException
     * @throws MalformedURLException
     * @author Jie
     * @date 2015年7月18日
     */
    public String sendHtmlEmail(String subject, String html, String text, boolean ssl, String... recipients)
            throws EmailException, MalformedURLException {
        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setHostName(getHostName());
        email.setSmtpPort(getPort());
        email.setAuthenticator(new DefaultAuthenticator(getUserName(), getPassword()));
        email.addTo(recipients);
        email.setFrom(getFrom(), getNickName());
        email.setSubject(subject);
        email.setSSLOnConnect(ssl);

        // set the html message
        email.setHtmlMsg(html);

        // set the alternative message
        email.setTextMsg(text);

        // send the email
        return email.send();
    }

}
