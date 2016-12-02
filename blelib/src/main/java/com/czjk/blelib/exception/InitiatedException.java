package com.czjk.blelib.exception;


import com.czjk.blelib.model.BleExceptionCode;

/**
 * 初始化异常
 */
public class InitiatedException extends BleException {
    public InitiatedException() {
        super(BleExceptionCode.INITIATED_ERR, "Initiated Exception Occurred! ");
    }
}
