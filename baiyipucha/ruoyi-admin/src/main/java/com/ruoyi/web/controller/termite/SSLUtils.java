package com.ruoyi.web.controller.termite;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class SSLUtils {
    public static void disableCertificateValidation() {
        try {
            // 创建一个信任所有证书的 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }}, null);

            // 将此 SSLContext 应用于 HttpsURLConnection
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
