package com.syh.whattoplay.background.vo;

import lombok.Data;

@Data
public class RatingVO {
    private Integer gid;
    private String uid;
    private Float score;
    private Long timestamp;
}
