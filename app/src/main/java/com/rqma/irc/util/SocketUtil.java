package com.rqma.irc.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rqma.irc.entity.Server;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import com.vilyever.socketclient.util.CharsetUtil;

/**
 * Socket工具类
 *
 * @Auther: RQMA
 * @Date: 2018/10/31 13:02
 */

public class SocketUtil {
    private static SocketClient socketClient;

    /**
     * 创建Socket客户端
     *
     * @return
     */
    public static SocketClient startSocketClient() {
        socketClient = new SocketClient();
        //设置IP
        socketClient.getAddress().setRemoteIP(Server.IP);
        //设置端口
        socketClient.getAddress().setRemotePort(Integer.parseInt(Server.PORT));
        //设置超时时间
        socketClient.getAddress().setConnectionTimeout(15 * 1000);
        //设置编码格式，默认为UTF-8
        socketClient.setCharsetName(CharsetUtil.UTF_8);
        // 连接，异步进行
        socketClient.connect();
        //System.out.println("是否连接"+socketClient.isConnected());

        socketClient.registerSocketClientDelegate(new SocketClientDelegate() {
            /**
             * 连接上远程端时的回调
             * @param client
             */
            @Override
            public void onConnected(SocketClient client) {
                Log.d("socket", "连接成功");
                //launcher.callExternalInterface("gameSocketConnectSuccess", "success");
            }

            /**
             * 与远程端断开连接时的回调
             * @param client
             */
            @Override
            public void onDisconnected(SocketClient client) {
                Log.d("socket", "连接断开");
                // 可在此实现自动重连
                socketClient.connect();
                //launcher.callExternalInterface("socketClose", "close");
            }

            /**
             * 接收到数据包时的回调
             * @param client
             * @param responsePacket
             */
            @Override
            public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
                // 获取按默认设置的编码转化的String，可能为null
                String message = responsePacket.getMessage();
                Log.i("socket接收服务端消息：", message);
                //launcher.callExternalInterface("socketDataHandler", message);
            }
        });
        return socketClient;
    }
}

