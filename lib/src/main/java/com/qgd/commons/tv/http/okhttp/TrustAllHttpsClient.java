/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.http.okhttp;

import com.squareup.okhttp.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 */
public class TrustAllHttpsClient extends OkHttpClient {
    public TrustAllHttpsClient() {
        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{tm}, null);
            setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            throw new RuntimeException("init SslSocketFactory fail", e);
        }
    }
}
