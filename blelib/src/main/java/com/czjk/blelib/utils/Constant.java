package com.czjk.blelib.utils;

/**
 * 全局常量
 */
public class Constant {

    // sp
    public static final String SP_NAME = "sp_name_bracelet";
    public static final String SP_KEY_DEVICE_ADDRESS = "sp_device_address";


    public  static final  byte CMD_ID_DFU = 0x01;
    public  static final  byte CMD_ID_GET = 0x02;
    public  static final  byte CMD_ID_SET = 0x03;
    public  static final  byte CMD_ID_BIND = 0x04;
    public  static final  byte CMD_ID_ALARM = 0x05;
    public  static final  byte CMD_ID_APP_CONTROL = 0x06;
    public  static final  byte CMD_ID_DEVICE_CONTROL = 0x07;

    public  static final  byte KEY_GET_TIME = 0x03;
    public  static final  byte KEY_GET_FW = 0x01;
    public  static final  byte KEY_SET_TIME = 0x01;
    public  static final  byte KEY_UNBIND = 0x02;
    public  static final  byte KEY_OTA = 0x01;
    public  static final  byte KEY_GET_ELECTRICITY = 0x05;


    public  static final  byte RESERVED = 0x00;

    public  static final  String REPLY_ENTER_OTA = "0101";
    public  static final  String REPLY_GET_FW = "0201";
    public  static final  String REPLY_GET_TIME = "0203";
    public  static final  String REPLY_SET_TIME = "0301";
    public  static final  String REPLY_UNBIND = "0402";

}
