package com.syh.whattoplay.background.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Rating {

    @Id
    private String _id;
    private Integer uid;
    private Integer gid;
    private Integer score;
    private Integer timestamp;

}
