package com.syh.whattoplay.background.global;

import lombok.Data;

@Data
public class WxResult<T> {

    private Integer code;
    private T result;
    private String message;

    public WxResult(Integer code, T result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

    public WxResult(Integer code, T result) {
        this.code = code;
        this.result = result;
    }

    public WxResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
