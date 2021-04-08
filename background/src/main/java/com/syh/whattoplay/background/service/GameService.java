package com.syh.whattoplay.background.service;

import com.syh.whattoplay.background.dao.GameDao;
import com.syh.whattoplay.background.entity.Game;
import com.syh.whattoplay.background.vo.MarkingListVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class GameService {

    @Resource
    private GameDao gameDao;

    @Resource
    private HttpServletRequest request;

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

    public List<Game> findAllByUid(MarkingListVO vo) {
        Map<String,Object> params = new HashMap<>();
        params.put("uid", request.getAttribute("openId"));
        params.put("pageSize",vo.getPageSize());
        params.put("pageNumber",vo.getPageNumber());
        return gameDao.findAllByUid(params,"Game");
    }
}
