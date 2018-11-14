package com.rqma.irc.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Mjpeg输入流,继承之DataInputStream,实现了Serializable接口
 * <p>
 * 补充：
 * 1.jpeg图片格式:开头两字节是 0xFF,0xD8,最后两字节是 0xFF,0xD9
 * 2.小车不断返回流数据,其格式为: http头信息 Mjpeg数据(0xFF 0xD8开头，0xFF 0xD9结尾)
 *
 * @Auther: RQMA
 * @Date: 2018/10/30 20:50
 */

/**
 * 该类继承了DataInputStream实现了Serializable接口
 * 1. 实例化流,获取初始化流和关闭实例流的方法
 * 2. 一个构造函数
 * 3. 一个根据帧数据大小获得位图方法
 */
public class MjpegInputStream extends DataInputStream implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * jpeg图片开头两字节0xFF,0xD8
     */
    private final byte[] SOI_MARKER = {(byte) 0xFF, (byte) 0xD8};
    /**
     * jpeg图片最后两字节0xFF,0xD9
     */
    private final byte[] EOF_MARKER = {(byte) 0xFF, (byte) 0xD9};
    /**
     * 表示服务器发给客户端的一帧数据的长度
     */
    private final String CONTENT_LENGTH = "Content-Length";
    /**
     * http头部数据最大长度
     */
    private final static int HEADER_MAX_LENGTH = 100;
    /**
     * http数据最大长度
     */
    private final static int FRAME_MAX_LENGTH = 40000 + HEADER_MAX_LENGTH;
    private int mContentLength = -1;
    /**
     * 定义一个MjpegInputStream对象mjpegInputStream
     */
    private static MjpegInputStream mjpegInputStream = null;

    /**
     * 带参构造方法
     *
     * @param in
     */
    private MjpegInputStream(InputStream in) {
        super(new BufferedInputStream(in, FRAME_MAX_LENGTH));
    }

    /**
     * 调用该类的构造方法,创建MjpegInputStream流
     *
     * @param is
     */
    public static void initInstance(InputStream is) {
        if (mjpegInputStream == null)
            mjpegInputStream = new MjpegInputStream(is);

    }

    /**
     * 获得创建的mjpegInputsteam实例
     *
     * @return
     */
    public static MjpegInputStream getInstance() {
        if (mjpegInputStream != null)
            return mjpegInputStream;

        return null;
    }

    /**
     * 关闭mjpegInputStream流
     */
    public static void closeInstance() {
        try {
            mjpegInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mjpegInputStream = null;
    }

    /**
     * 在数据流里面找SOI_MARKER={(byte)0xFF,(byte) 0xD8}
     *
     * @param in
     * @param sequence
     * @return
     * @throws IOException
     */
    private int getEndOfSeqeunce(DataInputStream in, byte[] sequence) throws IOException {
        int seqIndex = 0;
        byte c;
        for (int i = 0; i < FRAME_MAX_LENGTH; i++) {
            c = (byte) in.readUnsignedByte();
            if (c == sequence[seqIndex]) {
                seqIndex++;
                if (seqIndex == sequence.length)
                    return i + 1;
            } else
                seqIndex = 0;
        }
        return -1;
    }

    /**
     * 此方法功能是找到索引0xFF,0XD8在字符流的位置
     * 整个数据流形式：http头信息 帧头(0xFF 0xD8) 帧数据 帧尾(0xFF 0xD9)
     * 1、首先通过0xFF 0xD8找到帧头位置
     * 2、帧头位置前的数据就是http头，里面包含Content-Length，这个字段指示了整个帧数据的长度
     * 3、帧头位置后面的数据就是帧图像的开始位置
     *
     * @param in
     * @param sequence
     * @return
     * @throws IOException
     */
    private int getStartOfSequence(DataInputStream in, byte[] sequence) throws IOException {
        int end = getEndOfSeqeunce(in, sequence);
        return (end < 0) ? (-1) : (end - sequence.length);
    }

    /**
     * 从http的头信息中获取Content-Length，知道一帧数据的长度
     *
     * @param headerBytes
     * @return
     * @throws IOException
     * @throws NumberFormatException
     */
    private int parseContentLength(byte[] headerBytes) throws IOException,
            NumberFormatException {
        /**
         * 根据字节流创建ByteArrayInputStream流
         * Properties是java.util包里的一个类，它有带参数和不带参数的构造方法，表示创建无默认值和有默认值的属性列表
         * 根据流中的http头信息生成属性文件，然后找到属性文件CONTENT_LENGTH的value，这就找到了要获得的帧数据大小
         * 创建一个 ByteArrayInputStream，使用 headerBytes作为其缓冲区数组
         */
        ByteArrayInputStream headerIn = new ByteArrayInputStream(headerBytes);
        Properties props = new Properties();/*创建一个无默认值的空属性列表*/
        props.load(headerIn);/*从输入流中生成属性列表（键和元素对）。*/
        return Integer.parseInt(props.getProperty(CONTENT_LENGTH));/*用指定的键在此属性列表中搜索属性。*/
    }

    /**
     * @return
     * @throws IOException
     */
    public Bitmap readMjpegFrame() throws IOException {
        //标记流中当前的位置
        mark(FRAME_MAX_LENGTH);
        int headerLen = getStartOfSequence(this, SOI_MARKER);
        //将缓冲区的位置重置为标记位置
        reset();
        byte[] header = new byte[headerLen];
        //会一直阻塞等待，直到数据全部到达(数据缓冲区装满)
        readFully(header);
        try {
            mContentLength = parseContentLength(header);
        } catch (NumberFormatException e) {
            return null;
        }
        //根据帧数据的大小创建字节数组
        byte[] frameData = new byte[mContentLength];

        readFully(frameData);
        /**
         * 根据不同的源(file，stream，byte-arrays)创建位图
         * 把输入字节流流转为位图
         */
        return BitmapFactory.decodeStream(new ByteArrayInputStream(frameData));
    }

}
