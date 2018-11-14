package com.rqma.irc.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.rqma.irc.entity.Server;
import com.rqma.irc.io.MjpegInputStream;
import com.rqma.irc.util.HttpUtil;

/**
 * 视频view类，继承之SurfaceView，并实现两个接口SurfaceHolder.CallBack和Runnable
 * CallBack有3个方法，分别在SurfaceView创建，改变，销毁时进行回调
 * 本view类会被添加到MainActivity里的RelativeLayout rl_vv
 *
 * @Auther: RQMA
 * @Date: 2018/11/7 20:18
 */
public class VideoView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    /**
     * SurfaceHolder,控制SurfaceView的大小，格式，监控或者改变SurfaceView
     */
    private SurfaceHolder holder;
    /**
     * 画布
     */
    private Canvas canvas;
    /**
     * 子线程标志位
     */
    private boolean isRun;

    /**
     * 构造方法
     *
     * @param context
     */
    public VideoView(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化holder
     */
    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);
    }

    /**
     * surface创建时调用
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.isRun = true;
        new Thread(this).start();
    }

    /**
     * surface改变时调用
     *
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * surface销毁时调用
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.isRun = false;
    }

    /**
     * 进行网络请求(得到服务器返回的数据流),加载视频
     */
    @Override
    public void run() {
        //定义mjpegInputStream
        MjpegInputStream mjpegInputStream;
        //定义bitmap
        Bitmap bitmap;
        //初始化mjpegInputStream
        MjpegInputStream.initInstance(HttpUtil.getInputStream(Server.VEDIO_ADDR));
        //得到mjpegInputStream实例
        mjpegInputStream = MjpegInputStream.getInstance();
        while (isRun) {
            try {
                //锁定画布，锁定后通过其返回的画布对象Canvas，在其上面进行画图等操作
                canvas = holder.lockCanvas();
                //创建一个bitmap
                bitmap = mjpegInputStream.readMjpegFrame();
                //加载本地图片方法(用于测试BitmapFactory.decode()方法)
                //bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/test.png"));
                /**
                 * API说明：
                 *  Start editing the pixels in the surface. The returned Canvas can be used to draw into the surface's bitmap.
                 *  A null is returned if the surface has not been created or otherwise cannot be edited.
                 *  You will usually need to implement Callback.surfaceCreated to find out when the Surface is available for use
                 * 所以必须判断canvas是否为null
                 */
                if (canvas != null) {
                    RectF rectf = new RectF(0, 0, 1000, 800);
                    canvas.drawBitmap(bitmap, null, rectf, null);
                    holder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
}
