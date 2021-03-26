package com.syh.whattoplay.background.dao;

import com.syh.whattoplay.background.entity.Game;

import java.util.List;
import java.util.Map;

public class GameDao implements MongoBase<Game>{


    @Override
    public void insert(Game object, String collectionName) {

    }

    @Override
    public Game findOne(Map<String, Object> params, String collectionName) {
        return null;
    }

    @Override
    public List<Game> findAll(Map<String, Object> params, String collectionName) {
        return null;
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
