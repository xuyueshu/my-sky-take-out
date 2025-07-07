package com.zhiyou.skyserver.handler;

import com.zhiyou.skycommon.constant.MessageConstant;
import com.zhiyou.skycommon.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.sky.result.Result;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理想要中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException e){
        log.error("异常信息：{}",e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 用于捕获SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result doSQLException(SQLIntegrityConstraintViolationException ex) {
        log.error("异常信息：{}", ex.getMessage());
        String message = ex.getMessage();
        if (message.contains("Duplicate")) {
            return Result.error(message.split(" ")[2]+ MessageConstant.ALREADY_EXIST);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
