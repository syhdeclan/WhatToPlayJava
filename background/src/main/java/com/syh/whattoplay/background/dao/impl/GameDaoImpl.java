package com.syh.whattoplay.background.dao.impl;

import com.syh.whattoplay.background.dao.GameDao;
import com.syh.whattoplay.background.entity.Game;
import com.syh.whattoplay.background.entity.Rating;
import com.syh.whattoplay.background.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("gameDaoImpl")
public class GameDaoImpl implements GameDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(Game object, String collectionName) {

    }

    @Override
    public Game findOne(Map<String, Object> params, String collectionName) {
        return null;
    }

    @Override
    public List<Game> findAll(Map<String, Object> params, String collectionName) {
        return mongoTemplate.findAll(Game.class, collectionName);
    }

    public List<Game> findAllByUid(Map<String, Object> params, String collectionName) {
        List<Integer> gameList = mongoTemplate.find(new Query(Criteria.where("uid").is(params.get("uid"))), Rating.class, "Rating").stream().map(Rating::getGid).collect(Collectors.toList());
        return mongoTemplate.find(new Query(Criteria.where("id").nin(gameList)).skip(((long) params.get("pageNumber") - 1) * (int)params.get("pageSize")).limit((int)params.get("pageSize")), Game.class, collectionName);
    }

    @Override
    public void update(Map<String, Object> params, String collectionName) {

    }

    @Override
    public void createCollection(String collectionName) {

    }

    @Override
    public void remove(Map<String, Object> params, String collectionName) {

    }
}
