package com.dksys.core.commons;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 10:40:00
 */
public class ReslutBean extends HashMap<String, Object> {

    private static final String CODE_TAG = "code";
    private static final String DATA_TAG = "data";
    private static final String MESSAGE_TAG = "message";
    public ReslutBean() {

    }
    public ReslutBean(int code, Object data, String message) {
        super.put(CODE_TAG, code);
        super.put(MESSAGE_TAG, message);
        if (StrUtil.isEmptyIfStr(data)) {
            super.put(DATA_TAG, data);
        }
    }

    public static ReslutBean success(Object data, String msg) {

        return new ReslutBean(200, data, msg);
    }

    public static ReslutBean error(Object data, String msg) {

        return new ReslutBean(500, data, msg);
    }

}
