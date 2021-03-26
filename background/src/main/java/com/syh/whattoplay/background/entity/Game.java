package com.syh.whattoplay.background.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Game {
    @Id
    private String _id;
    private Integer id;
    private String name;
    private String description;
    private String publisher;
    private String publishTime;
    private String platforms;
    private String genres;
    private String tags;
    private String price;
    private String imgUrl;
}
