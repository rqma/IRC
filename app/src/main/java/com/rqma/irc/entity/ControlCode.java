package com.rqma.irc.entity;

/**
 * 小车运动控制码类
 * 控制码均为十六进制
 *
 * @Auther: RQMA
 * @Date: 2018/10/30 20:50
 */
public class ControlCode {
    public static final String COMMAND_ALARM_OFF = "FF030100FF";
    public static final String COMMAND_ALARM_ON = "FF030000FF";
    public static final String COMMAND_FILE = "CommandFile";
    public static final String COMMAND_LED_OFF = "FF020100FF";
    public static final String COMMAND_LED_ON = "FF020000FF";
    public static final String CONTROLADDRESS_KEY = "CONTROLADDRESS_KEY";
    public static final String CONTROLPORT_KEY = "CONTROLPORT_KEY";
    public static final String DOWNCOMMAND_KEY = "DOWNCOMMAND_KEY";
    public static final String LEFTCOMMAND_KEY = "LEFTCOMMAND_KEY";
    public static final String LEFTDOWNCOMMAND_KEY = "LEFTDOWNCOMMAND_KEY";
    public static final String LEFTUPCOMMAND_KEY = "LEFTUPCOMMAND_KEY";
    public static final String RIGHTCOMMAND_KEY = "RIGHTCOMMAND_KEY";
    public static final String RIGHTDOWNCOMMAND_KEY = "RIGHTDOWNCOMMAND_KEY";
    public static final String RIGHTUPCOMMAND_KEY = "RIGHTUPCOMMAND_KEY";
    public static final String RUNNING_DOWN = "FF000200FF";
    public static final String RUNNING_LEFT = "FF000400FF";
    public static final String RUNNING_LEFT_DOWN = "FF000800FF";
    public static final String RUNNING_LEFT_UP = "FF000600FF";
    public static final String RUNNING_RIGHT = "FF000300FF";
    public static final String RUNNING_RIGHT_DOWN = "FF000700FF";
    public static final String RUNNING_RIGHT_UP = "FF000500FF";
    public static final String RUNNING_STOP = "FF000000FF";
    public static final String RUNNING_UP = "FF000100FF";
    public static final String STOPCOMMAND_KEY = "STOPCOMMAND_KEY";
    public static final String UPCOMMAND_KEY = "UPCOMMAND_KEY";
    public static final String VEDIOADDRESS_KEY = "VEDIOADDRESS_KEY";

}
