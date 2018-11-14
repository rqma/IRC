package com.rqma.irc.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求工具类
 *
 * @Auther: RQMA
 * @Date: 2018/11/8 16:13
 */
public class HttpUtil {

    /**
     * 得到服务器(小车)返回的数据流
     *
     * @param url
     * @return
     */
    public static InputStream getInputStream(String url) {
        URL videoUrl;
        HttpURLConnection conn;
        try {
            videoUrl = new URL(url);
            conn = (HttpURLConnection) videoUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            return conn.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
