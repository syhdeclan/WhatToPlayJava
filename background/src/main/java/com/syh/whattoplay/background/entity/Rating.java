package com.syh.whattoplay.background.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Rating {

    @Id
    private String _id;
    private String uid;
    private Integer gid;
    private Float score;
    private Long timestamp;

}
