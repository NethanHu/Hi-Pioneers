package com.jfinal.admin.common.kit;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * 邮件发送工具类
 * <p>
 * 文档：
 * 核心代码在 com.sun.mail.smtp.SMTPTransport.java。通过调试可以了解很多细节
 * EmailConstants 中有很多可配置常量
 */
public class EmailKit {

    private static final Log log = Log.getLog(EmailKit.class);

    private static boolean debug = false;

    private static boolean initSsl = false;

    public static void setDebug(boolean debug) {
        EmailKit.debug = debug;
    }

    private static void initSsl(String emailServer) {
        if (!initSsl) {
            /**
             * 配置邮件发送客户端信任的发送服务器，配置为 "*" 时信任所有服务器
             * 必须配置，否则将抛出如下异常：
             *   javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
             *   at com.sun.mail.smtp.SMTPTransport.openServer(SMTPTransport.java:2120)
             *   at com.sun.mail.smtp.SMTPTransport.protocolConnect(SMTPTransport.java:712)
             */
            // System.setProperty("mail.smtp.ssl.trust", emailServer);
            System.setProperty("mail.smtp.ssl.trust", "*");

            /**
             * 本变量不配置也能支持 SSL，因为本变量与 setSSLOnConnect(true) 都能触发 Email.java 中的如下代码被调用
             * properties.setProperty(EmailConstants.MAIL_SMTP_SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
             * 其中第一个参数值为： "mail.smtp.socketFactory.class"
             * 从而在 com.sun.mail.util.SocketFetcher.getSocket(String host, int port, Properties props, String prefix, boolean useSSL)
             * 方法中在 if (isSSL) 两个分支中都通过 "mail.smtp.socketFactory.class" 获取到了
             * "javax.net.ssl.SSLSocketFactory"，从而后面的 ssl 逻辑变成一样的了
             *
             *
             * 本配置在同时配置 SimpleEmail.setDebug(true) 时，控制台第 9 行输出的 isSSL 值为 true：
             * DEBUG SMTP: trying to connect to host "old.jfinal.com", port 465, isSSL true
             *
             * 建议：使用该配置
             */
            System.setProperty("mail.smtp.ssl.enable", "true");

            initSsl = true;
        }
    }

    public static String sendEmail(String fromEmail, String toEmail, String title, String content) {
        return sendEmail(null, fromEmail, null, toEmail, title, content);
    }

    /**
     * 阿里云从 2019 年 9 月底开始禁止了 25 号端口（腾迅、华为云亦如此）
     * 需要使用 465 端口。当密码不为空时认为是在使用 465 SSL 通道
     */
    public static String sendEmail(String emailServer, String fromEmail, String password, String toEmail, String title, String content) {
        // emailServer 为空时，默认使用本地 postfix 发送，这样就可以将 postfix 的 mynetworks 配置为 127.0.0.1 或 127.0.0.0/8 了
        emailServer = StrKit.notBlank(emailServer) ? emailServer : "127.0.0.1";

        initSsl(emailServer);

        SimpleEmail email = new SimpleEmail();
        email.setHostName(emailServer);
        email.setDebug(debug);                            // 调试错误原因极其有用的配置


        email.setSSLOnConnect(true);                    // 走 465 SSL 通道必须的配置，最关键配置
        // email.setSslSmtpPort("465");					// 可选配置，默认值已经是 "465"
        // email.setStartTLSRequired(true);				// 强制使用 StartTLS，在测试 SSL 是否可用时开启一下
        // email.setSSLCheckServerIdentity(true);		// 验证服务端证书


        email.setSocketConnectionTimeout(32 * 1000);    // 连接超时
        email.setSocketTimeout(32 * 1000);                // socket IO 超时

        // 如果密码为空，则不进行认证
        if (StrKit.notBlank(password)) {
            email.setAuthentication(fromEmail, password);
        }

        email.setCharset("utf-8");
        try {
            email.addTo(toEmail);
            email.setFrom(fromEmail);
            email.setSubject(title);
            email.setMsg(content);
            return email.send();
        } catch (EmailException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
		
		
	
	
