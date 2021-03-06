package com.syh.whattoplay.background.controller;

import com.syh.whattoplay.background.annotation.Authorize;
import com.syh.whattoplay.background.entity.Game;
import com.syh.whattoplay.background.service.GameService;
import com.syh.whattoplay.background.vo.MarkingListVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Resource
    private GameService gameService;

    @Authorize
    @GetMapping()
    public List<Game> findAllGame(){
        return gameService.findAllGame();
    }

    @GetMapping("/create")
    public void create(){
        gameService.create();
    }

    @Authorize
    @GetMapping("/user")
    public List<Game> getGamesByUser(MarkingListVO vo){
        return gameService.findAllByUid(vo);
    }


}
