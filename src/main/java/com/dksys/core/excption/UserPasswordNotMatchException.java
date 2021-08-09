package com.dksys.core.excption;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 11:13:00
 */
public class UserPasswordNotMatchException extends RuntimeException {
    public UserPasswordNotMatchException(String message) {
        super(message);
    }
}
