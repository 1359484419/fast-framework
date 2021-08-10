package com.bix.bixApi.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * 统一返回结果
 *
 */
@ApiModel(description = "rest请求的返回模型，所有rest正常都返回该类的对象")
@Getter
public class R<T> {

    public static final String SUCCESSFUL_CODE = "000000";
    public static final String SUCCESSFUL_MESG = "处理成功";

    public static final String FAIL_CODE = "111111";
    public static final String TAX_FAIL_CODE = "000111";
    public static final String FAIL_MESG = "处理失败";


    @ApiModelProperty(value = "处理结果code", required = true)
    private String code;
    @ApiModelProperty(value = "处理结果描述信息")
    private String mesg;
    @ApiModelProperty(value = "请求结果生成时间戳")
    private Instant time;
    @ApiModelProperty(value = "处理结果数据信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public R() {
        this.time = ZonedDateTime.now().toInstant();
    }


    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code code
     * @param mesg mesg
     * @param data data
     */
    public R(String code, String mesg, T data) {
        this.code = code;
        this.mesg = mesg;
        this.data = data;
        this.time = ZonedDateTime.now().toInstant();
    }

    private R(String code, T data) {
        this.code = code;
        this.data = data;
        this.time = ZonedDateTime.now().toInstant();
    }




    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static R success(Object data) {
        return new R<>(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static  R success() {
        return success(null);
    }




    /**
     * 快速创建成功结果(税局接口)
     *
     * @return Result
     */
    public static  R success(String mesg, Object data) {
        return new  R<>(SUCCESSFUL_CODE, mesg, data);
    }


    /**
     * 快速创建成功结果(税局接口)
     *
     * @return Result
     */
    public static  R taxSuccess(String mesg, Object data) {
        return new  R<>(SUCCESSFUL_CODE, mesg, data);
    }

    /**
     * 快速创建成功结果(税局接口)
     *
     * @return Result
     */
    public static  R taxFail(String mesg) {
        return new  R<>(TAX_FAIL_CODE, mesg, null);
    }




    /**
     * 快速创建失败结果
     *
     * @return Result
     */
    public static  R error(String errorCode, String errorMsg) {
        return new  R<>(errorCode, errorMsg, Optional.empty());
    }


    /**
     * 成功code=000000
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
