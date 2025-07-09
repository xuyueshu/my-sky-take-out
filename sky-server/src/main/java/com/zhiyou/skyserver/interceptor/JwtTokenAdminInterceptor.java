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
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果拦截到的不是动态方法，直接放行
        if(! (handler instanceof HandlerMethod)){
            return true;
        }
        // 从请求头中获取token
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        log.info("获取到admin的token：{}",token);

        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工的id为：{}",empId);
            BaseContext.setCurrentId(empId);
            return true;
        } catch (Exception e){
            log.error("令牌解析失败：{}",e.getMessage());
            response.setStatus(401);
        }
        return false;
    }
}
