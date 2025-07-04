package com.zhiyou.skyserver;

import com.zhiyou.skycommon.constant.JwtClaimsConstant;
import com.zhiyou.skycommon.properties.JwtProperties;
import com.zhiyou.skycommon.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;

@SpringBootTest
class MySkyApplicationTests {
    @Autowired
    private JwtProperties jwtProperties;


    @Test
    void testCreateJWT() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,"123232434");
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        System.out.println(token);
    }
    @Test
    void parseJWT(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJlbXBJZCI6IjEyMzIzMjQzNCIsImV4cCI6MTc1MTY0OTMxNX0.4rSDdqxTe4ROuhDd6VkfOEWyUthM4eVL-sWmMFdrj70";
        String adminSecretKey = jwtProperties.getAdminSecretKey();
        Claims claims = JwtUtil.parseJWT(adminSecretKey, token);
        String empId = (String) claims.get(JwtClaimsConstant.EMP_ID);
        long time = claims.getExpiration().getTime();
        Date date = new Date(time);
        System.out.println(empId);
        System.out.println(time);
        System.out.println(date);
    }

}
