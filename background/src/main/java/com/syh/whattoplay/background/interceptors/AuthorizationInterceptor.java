package com.syh.whattoplay.background.interceptors;

import com.auth0.jwt.JWT;
import com.syh.whattoplay.background.annotation.Authorize;
import com.syh.whattoplay.background.entity.User;
import com.syh.whattoplay.background.global.WxException;
import com.syh.whattoplay.background.service.UserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthorizationInterceptor implements HandlerInterceptor {

    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        //如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(Authorize.class)) {
            Authorize userLoginToken = method.getAnnotation(Authorize.class);
            if (userLoginToken.required()) {
                String token = request.getHeader("authorization");
                if (token == null) {
                    throw new WxException(401,"token失效，请重新登陆");
                }
                String openId;
                try{
                    openId = JWT.decode(token).getKeyId();
                    request.setAttribute("openId",openId);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new WxException(401,"token失效，请重新登陆");
                }
                User user = userService.findUserByOpenId(openId);
                if (user == null) {
                    throw new WxException(401,"token无效，未注册");
                }
                try{
                    String sessionKey = JWT.decode(token).getClaim("session_key").asString();
                    request.setAttribute("sessionKey",sessionKey);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new WxException(401,"token无效，未获取到session");
                }
                return true;
            }
        }
        return true;
    }
}
