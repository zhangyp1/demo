package com.newland.paas.sbcommon.vo;

import javax.validation.Valid;

/**
 * 请求body结构，业务参数
 *
 * @param <T>
 * @author wrp
 */
public class BasicRequestContentVo<T> {

    @Valid
    private T params;

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
