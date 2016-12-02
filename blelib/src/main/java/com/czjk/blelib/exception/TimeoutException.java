package com.czjk.blelib.exception;


import com.czjk.blelib.model.BleExceptionCode;

/**
 *  超时异常
 */
public class TimeoutException extends BleException {
    public TimeoutException() {
        super(BleExceptionCode.TIMEOUT, "Timeout Exception Occurred! ");
    }
}
