package com.bix.bixApi;

/**
 * <p> http错误码列表 </p>
 *
 * <pre> Created: 2019/5/16 14:55 </pre>
 *
 * @author  lx
 * @version 1.0
 * @since JDK 1.7
 */
public enum ErrorCode implements IEnum<Integer> {

    /**
     * 系统级别错误
     */
    SYS_ERROR(1, "系统级别错误"),
    SERVER_ERROR(2, "服务发生错误"),
    NOT_FOUND(400, "无法找到"),
    OK(200, "请求成功"),
    SERVER_EXCEPTRION(500, "服务处理异常"),
    TIME_OUT(504, "请求超时"),
    NOT_SUPPORT(505, "所请求操作不支持"),
    NOT_PERMISSION(403, "暂无权限"),
    DB_ERROR(599, "数据库发生错误"),
    JSON_FORMATE_EXCEPTION(1001, "JSON参数格式错误"),
    JSON_CONFIG_EXCEPTION(1002, "JSON配置错误"),
    API_CLOSED(1003, "API接口已停用"),
    ACCESS_FORBIDDEN(1008, "禁止访问"),
    EXCEED_LIMIT(1009, "超过访问次数限制"),
    BUSI_EXCEPTION(501, "业务异常"),
    INVALID_PARAMETER(502, "参数不合法"),
    USERNAME_PASSWORD_NOT_MATCH(502, "账号密码不匹配");

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
