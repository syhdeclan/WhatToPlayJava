package com.syh.whattoplay.background.service;

import com.syh.whattoplay.background.dao.RatingDao;
import com.syh.whattoplay.background.entity.Rating;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Log4j2
public class RatingService {

    @Resource
    private RatingDao ratingDao;

    public void createRating(){
        Rating rating = new Rating();

        ratingDao.insert(rating,"Rating");
    }

}
