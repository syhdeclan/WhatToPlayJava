package com.syh.whattoplay.background.service;

import com.syh.whattoplay.background.dao.RatingDao;
import com.syh.whattoplay.background.entity.Rating;
import com.syh.whattoplay.background.vo.RatingVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class RatingService {

    @Resource
    private RatingDao ratingDao;

    public Rating findOne(String uid, Integer gid){
        Map<String, Object> params = new HashMap<>();
        params.put("uid",uid);
        params.put("gid",gid);
        return ratingDao.findOne(params,"Rating");
    }

//    public List<Rating> findAllByUid(Integer uid){
//        Map<String, Object> params = new HashMap<>();
//        params.put("uid",uid);
//        return ratingDao.findAllByUid(params,"Rating");
//    }

    public void createRating(RatingVO vo){
        vo.setTimestamp(new Date().getTime());
        Rating rating = new Rating();
        BeanUtils.copyProperties(vo,rating);
        ratingDao.insert(rating,"Rating");
    }

    public void updateRating(RatingVO vo){
        Map<String, Object> params = new HashMap<>();
        params.put("uid",vo.getUid());
        params.put("gid",vo.getGid());
        params.put("score",vo.getScore());
        params.put("timestamp",new Date().getTime());
        ratingDao.update(params,"Rating");
    }

}
