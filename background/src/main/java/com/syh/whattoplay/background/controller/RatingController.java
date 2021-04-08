package com.syh.whattoplay.background.controller;

import com.syh.whattoplay.background.annotation.Authorize;
import com.syh.whattoplay.background.global.WxException;
import com.syh.whattoplay.background.service.RatingService;
import com.syh.whattoplay.background.vo.RatingVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Resource
    private RatingService ratingService;

    @Resource
    private HttpServletRequest request;

    @Authorize
    @PostMapping
    public void create(@RequestBody RatingVO vo) {
        if (vo.getGid() == null) {
            throw new WxException(-1,"参数有误");
        }
        if (vo.getScore() == null) {
            throw new WxException(-1,"未给出评分");
        }
        vo.setUid((String) request.getAttribute("openId"));
        if (ratingService.findOne(vo.getUid(), vo.getGid()) != null) {
            ratingService.updateRating(vo);
        }else {
            ratingService.createRating(vo);
        }
    }

}
