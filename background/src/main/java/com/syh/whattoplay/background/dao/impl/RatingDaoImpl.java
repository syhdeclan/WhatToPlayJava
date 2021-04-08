package com.syh.whattoplay.background.dao.impl;

import com.syh.whattoplay.background.dao.RatingDao;
import com.syh.whattoplay.background.entity.Rating;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository("ratingDaoImpl")
public class RatingDaoImpl implements RatingDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(Rating object, String collectionName) {
        mongoTemplate.insert(object,collectionName);
    }

    @Override
    public Rating findOne(Map<String, Object> params, String collectionName) {
        return mongoTemplate.findOne(new Query(new Criteria().andOperator(Criteria.where("uid").is(params.get("uid")),Criteria.where("gid").is(params.get("gid")))), Rating.class,collectionName);
    }

//    @Override
//    public List<Rating> findAllByUid(Map<String, Object> params, String collectionName) {
//        return mongoTemplate.find(new Query(Criteria.where("uid").is(params.get("uid"))), Rating.class, collectionName);
//    }

    @Override
    public List<Rating> findAll(Map<String, Object> params, String collectionName) {
        return mongoTemplate.find(new Query(Criteria.where("age").lt(params.get("maxAge"))), Rating.class, collectionName);
    }

    @Override
    public void update(Map<String, Object> params, String collectionName) {
        mongoTemplate.upsert(new Query(new Criteria().andOperator(Criteria.where("uid").is(params.get("uid")),Criteria.where("gid").is(params.get("gid")))), new Update().set("score", params.get("score")).set("timestamp",params.get("timestamp")), Rating.class,collectionName);
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
