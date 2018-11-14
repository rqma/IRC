package com.rqma.irc.util;

/**
 * 进制转换工具类
 *
 * @Auther: RQMA
 * @Date: 2018/11/1 21:23
 */
public class HexUtil {
    /**
     * 十六进制转bytes
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStrTobytes(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[len / 2];
        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
            index += 2;
        }
        return bytes;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * bytes转十六进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexStr(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
