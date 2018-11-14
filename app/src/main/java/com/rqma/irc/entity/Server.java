package com.rqma.irc.entity;

/**
 * 小车网络信息描述类
 *
 * @Auther: RQMA
 * @Date: 2018/11/3 16:49
 */
public class Server {
    /**
     * 安卓手机与小车通过Socket通信所用IP地址
     */
    public static final String IP = "192.168.8.1";
    /**
     *  抓包测试时所用url
     */
    //public static final String IP = "192.168.1.101";
    //public static final String IP = "192.168.8.155";
    /**
     * 安卓手机与小车通过Socket通信所用端口号
     */
    public static final String PORT = "2001";
    /**
     * 小车视频url
     */
    public static final String VEDIO_ADDR = "http://192.168.8.1:8083/?action=stream";

}
