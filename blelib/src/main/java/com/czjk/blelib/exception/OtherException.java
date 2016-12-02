package com.czjk.blelib.exception;


import com.czjk.blelib.model.BleExceptionCode;

/**
 * 其他异常
 */
public class OtherException extends BleException {
    public OtherException(String description) {
        super(BleExceptionCode.OTHER_ERR, description);
    }
}
