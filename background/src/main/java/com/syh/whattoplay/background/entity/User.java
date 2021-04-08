package com.syh.whattoplay.background.entity;

import lombok.Data;

@Data
public class User {

    //微信唯一ID
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
