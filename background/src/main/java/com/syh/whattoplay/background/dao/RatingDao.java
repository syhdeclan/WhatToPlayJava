package com.syh.whattoplay.background.dao;

import com.syh.whattoplay.background.entity.Rating;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class RatingDao implements MongoBase<Rating> {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(Rating object, String collectionName) {
        mongoTemplate.insert(object,collectionName);
    }

    @Override
    public Rating findOne(Map<String, Object> params, String collectionName) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(params.get("id"))), Rating.class,collectionName);
    }

    @Override
    public List<Rating> findAll(Map<String, Object> params, String collectionName) {
        return mongoTemplate.find(new Query(Criteria.where("age").lt(params.get("maxAge"))), Rating.class, collectionName);
    }

    @Override
    public void update(Map<String, Object> params, String collectionName) {
        mongoTemplate.upsert(new Query(Criteria.where("id").is(params.get("id"))), new Update().set("name", params.get("name")), Rating.class,collectionName);
    }

    @Override
    public void createCollection(String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    @Override
    public void remove(Map<String, Object> params, String collectionName) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(params.get("id"))),Rating.class,collectionName);
    }
}
