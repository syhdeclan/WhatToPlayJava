package com.syh.whattoplay.background.controller;

import com.syh.whattoplay.background.global.WxResult;
import com.syh.whattoplay.background.service.UserService;
import com.syh.whattoplay.background.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public WxResult login(@RequestBody LoginVO vo){
        //这里应该提前做一些什么判断呢 数据是否有效之类的
        Map<String,Object> result = userService.loginOrRegister(vo);
        return new WxResult(200,result);
    }


}
