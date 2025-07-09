package com.zhiyou.skyserver.interceptor;

import com.zhiyou.skycommon.constant.JwtClaimsConstant;
import com.zhiyou.skycommon.context.BaseContext;
import com.zhiyou.skycommon.properties.JwtProperties;
import com.zhiyou.skycommon.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是访问动态方法，直接放行
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        // 获取token
        String token = request.getHeader(jwtProperties.getUserTokenName());
        log.info("获取到用户token：{}",token);
        try {
            // 解析token
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            // 将id存储到ThreadLocal中
            BaseContext.setCurrentId(userId);
            // 放行
            return true;
        } catch (Exception e) {
            log.error("jwt解析失败:{}",e.getMessage());
            response.setStatus(401);
        }
        return false;
    }
}
