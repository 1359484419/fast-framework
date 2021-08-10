package com.bix.bixApi.controller.exception;

import com.alibaba.fastjson.JSON;
import com.bix.bixApi.ErrorCode;
import com.bix.bixApi.common.exception.BixApiException;
import com.google.common.collect.Lists;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>拦截发生错误的Controller </p>
 *
 * <pre> Created: 2019/5/16 15:18 </pre>
 *
 * @author  lx
 * @version 1.0
 * @since JDK 1.7
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

    @Value("${bixApi.isDev}")
    private boolean isDev;

    /**
     * 手动抛的异常，直接将异常信息返回页面
     */
    @ExceptionHandler(BixApiException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResp tradeApiException(BixApiException e) {
        log.error(e.getMessage(), e);
        return new ErrorResp(e.getErrorCode(), e);
    }

    /**
     * 数据库异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResp sqlException(SQLException e) {
        log.error(e.getMessage(), e);
        return new ErrorResp(ErrorCode.DB_ERROR, e);
    }

    /**
     * 数据库异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResp sysException(Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorResp(ErrorCode.SYS_ERROR, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResp methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验错误!", e);
        final BindingResult bindingResult = e.getBindingResult();
        final List<ObjectError> errorList = bindingResult.getAllErrors();
        String message = "参数传递错误";
        if (CollectionUtils.isNotEmpty(errorList)) {

            List<String> errorMessages = Lists.newArrayListWithCapacity(errorList.size());
            for (ObjectError objectError : errorList) {
                errorMessages.add(objectError.getDefaultMessage());
            }
            message = JSON.toJSONString(errorMessages);
        }

        return new ErrorResp(ErrorCode.INVALID_PARAMETER.getCode(), message, e);
    }

    /**
     * 错误返回的请求体
     */
    @Data
    public class ErrorResp {
        private int code;
        private String message;
        private String throwable;

        public ErrorResp(int code, String message, Throwable throwable) {
            this.code = code;
            this.message = message;
            //正式环境不返回这个字段
            if (isDev) {
                this.throwable = throwable.getMessage();
            }
        }

        public ErrorResp(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public ErrorResp(ErrorCode errorCode) {
            Assert.notNull(errorCode, "errorCode不能为空！");
            this.code = errorCode.getCode();
            this.message = errorCode.getMessage();
        }

        public ErrorResp(ErrorCode errorCode, Throwable throwable) {
            this(errorCode);
            //正式环境不返回这个字段
            if (isDev) {
                this.throwable = throwable.getMessage();
            }
            if (throwable instanceof BixApiException) {
                BixApiException bixApiException = (BixApiException) throwable;
                this.message = bixApiException.getBusiExcpMesg();
            }
        }
    }
}
