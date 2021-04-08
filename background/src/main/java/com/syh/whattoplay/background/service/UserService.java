package com.syh.whattoplay.background.service;

import com.alibaba.fastjson.JSON;
import com.syh.whattoplay.background.dao.UserDao;
import com.syh.whattoplay.background.entity.User;
import com.syh.whattoplay.background.entity.WxOpenIdResult;
import com.syh.whattoplay.background.global.HttpRequest;
import com.syh.whattoplay.background.utils.JWTUtils;
import com.syh.whattoplay.background.vo.LoginVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private HttpServletRequest request;

    public Map<String,Object> loginOrRegister(LoginVO vo){
        //需要先拿前台传过来的Code转换成OpenId
        code2OpenId(vo);
        User user = new User();
        BeanUtils.copyProperties(vo,user);
        Map<String,Object> params = new HashMap<>();
        params.put("openId",vo.getOpenId());
        if (userDao.findOne(params, "User") == null) {
            //说明没注册过，需要先注册一下再返回token
            userDao.insert(user,"User");
        }
        //JWT生成token并返回
        String token = JWTUtils.createToken(user);
        Map<String,Object> result = new HashMap<>();
        result.put("token",token);
        result.put("openId",vo.getOpenId());
        return result;
    }

    public User findUserByOpenId(String openId){
        Map<String, Object> params = new HashMap<>();
        params.put("openId",openId);
        return userDao.findOne(params,"User");
    }

    private void code2OpenId(LoginVO vo){
        String appId = "wx18ab5c70d1f34c16";
        String appSecret = "7274ada63449eaf069937bff3267af83";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+appSecret+"&js_code="+vo.getCode()+"&grant_type=authorization_code";

        WxOpenIdResult openIdResult = JSON.parseObject(HttpRequest.sendGet(url, null), WxOpenIdResult.class);
        vo.setOpenId(openIdResult.getOpenId());
        vo.setSessionKey(openIdResult.getSessionKey());
    }

}
