package com.dksys.core.excption;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 11:16:00
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
