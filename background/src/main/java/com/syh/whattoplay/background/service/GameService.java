package com.syh.whattoplay.background.service;

import com.syh.whattoplay.background.dao.GameDao;
import com.syh.whattoplay.background.entity.Game;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Log4j2
public class GameService {

    @Resource
    private GameDao gameDao;

    public List<Game> findAllGame(){
        log.info(123);
        return gameDao.findAll(null,"Game");
    }

    public void create() {
        Game game = new Game();
//        game.setId(1234567);
        game.setName("hahah");
        log.info("ahaha");
        gameDao.insert(game,"Game");
    }
}
