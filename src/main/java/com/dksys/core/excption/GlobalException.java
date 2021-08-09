package com.dksys.core.excption;

import com.dksys.core.commons.ReslutBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 16:35:00
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserPasswordNotMatchException.class)
    public ReslutBean UserPasswordNotMatchException(UserPasswordNotMatchException e) {
        return ReslutBean.error(null, e.getMessage());
    }
}
