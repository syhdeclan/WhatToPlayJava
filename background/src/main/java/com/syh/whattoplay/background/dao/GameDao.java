package com.syh.whattoplay.background.dao;

import com.syh.whattoplay.background.entity.Game;

import java.util.List;
import java.util.Map;

public interface GameDao extends MongoBase<Game>{
    List<Game> findAllByUid(Map<String, Object> params, String game);
}
