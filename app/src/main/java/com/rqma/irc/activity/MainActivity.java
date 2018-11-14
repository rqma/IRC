package com.rqma.irc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rqma.irc.entity.ControlCode;
import com.rqma.irc.R;
import com.rqma.irc.util.HexUtil;


import com.rqma.irc.util.SocketUtil;
import com.rqma.irc.view.VideoView;
import com.vilyever.socketclient.SocketClient;


import static java.lang.Thread.sleep;

/**
 * 主Activity
 *
 * @Auther: RQMA
 * @Date: 2018/10/30 20:40
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 相对布局,用于承载videoview
     */
    private RelativeLayout rl_vv;
    /**
     * 按钮:前进,后退,左转,右转,左前，左后，右前，右后，暂停
     */
    private Button bt_up, bt_down, bt_left, bt_right, bt_left_up, bt_left_down, bt_right_up, bt_right_down, bt_stop;
    /**
     * 按钮:用于发送多条指令
     */
    private Button bt_map;
    /**
     * 表示运动方向的字符串
     */
    private String direction;
    /**
     * Socket客户端
     */
    private SocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCamera();
        init();
        /**
         * Socket客户端连接小车
         * 异步方式连接
         */
        client = SocketUtil.startSocketClient();
    }

    /**
     * 初始化视频控件
     */
    private void initCamera() {
        rl_vv = findViewById(R.id.rl_vv);
        rl_vv.addView(new VideoView(this));
    }

    /**
     * 初始化按钮控件
     */
    private void init() {
        bt_up = findViewById(R.id.bt_up);
        bt_down = findViewById(R.id.bt_down);
        bt_left = findViewById(R.id.bt_left);
        bt_right = findViewById(R.id.bt_right);
        bt_left_up = findViewById(R.id.bt_left_up);
        bt_left_down = findViewById(R.id.bt_left_down);
        bt_right_up = findViewById(R.id.bt_right_up);
        bt_right_down = findViewById(R.id.bt_right_down);
        bt_stop = findViewById(R.id.bt_stop);
        bt_map = findViewById(R.id.bt_map);

        bt_up.setOnClickListener(this);
        bt_down.setOnClickListener(this);
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        bt_left_up.setOnClickListener(this);
        bt_left_down.setOnClickListener(this);
        bt_right_up.setOnClickListener(this);
        bt_right_down.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
        bt_map.setOnClickListener(this);
    }

    /**
     * 按钮监听器
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_up:
                direction = ControlCode.RUNNING_UP;
                break;
            case R.id.bt_down:
                direction = ControlCode.RUNNING_DOWN;
                break;
            case R.id.bt_left:
                direction = ControlCode.RUNNING_LEFT;
                break;
            case R.id.bt_right:
                direction = ControlCode.RUNNING_RIGHT;
                break;
            case R.id.bt_left_up:
                direction = ControlCode.RUNNING_LEFT_UP;
                break;
            case R.id.bt_left_down:
                direction = ControlCode.RUNNING_LEFT_DOWN;
                break;
            case R.id.bt_right_up:
                direction = ControlCode.RUNNING_RIGHT_UP;
                break;
            case R.id.bt_right_down:
                direction = ControlCode.RUNNING_RIGHT_DOWN;
                break;
            case R.id.bt_stop:
                direction = ControlCode.RUNNING_STOP;
                break;
            default:
                break;
        }
        //调用向小车发送控制命令函数
        if (!(v.getId() == R.id.bt_map))
            sendData();
        else
            sendDatas();
    }

    /**
     * 向小车发送一条运动指令
     */
    private void sendData() {
        System.out.println("***********发送控制一条命令**************");
        System.out.println(client.getState());
        if (!client.isConnected())
            return;
        client.sendData(HexUtil.hexStrTobytes(direction));

    }

    /**
     * 向小车发送多条指令,
     * 目前，只实现最简单的运动，且走完才停
     */
    public void sendDatas() {
        System.out.println("***********发送控制多条命令**************");
        if (!client.isConnected())
            return;
        try {
            for (int i = 0; i <= 6; i++) {
                if (i % 2 == 0) {
                    client.sendData(HexUtil.hexStrTobytes(ControlCode.RUNNING_UP));
                    sleep(1000);
                } else {
                    client.sendData(HexUtil.hexStrTobytes(ControlCode.RUNNING_LEFT));
                    sleep(950);
                }

            }
            client.sendData(HexUtil.hexStrTobytes(ControlCode.RUNNING_STOP));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
