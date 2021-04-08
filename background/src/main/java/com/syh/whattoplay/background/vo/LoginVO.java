package com.syh.whattoplay.background.vo;

import lombok.Data;

@Data
public class LoginVO {

    private String code;
    private String openId;
    private String sessionKey;
    private String avatarUrl;
    private String city;
    private String country;
    private Integer gender;
    private String language;
    private String nickName;
    private String province;

}
