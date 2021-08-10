package com.bix.bixApi;

public interface IEnum<T> {

    /**
     * 返回code
     * @return
     */
    T getCode();

    String getMessage();
}
