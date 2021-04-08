package com.syh.whattoplay.background.global;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(WxException.class)
    public WxResult handleWebException(WxException e){
        e.printStackTrace();
        return new WxResult(e.getCode(),e.getMessage());
    }

}
