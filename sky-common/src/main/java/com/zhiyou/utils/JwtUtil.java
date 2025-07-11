package com.zhiyou.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


/**
 * Token主要由三部分构成：header（头部）、payload（载荷）和Signature（签名）。在header中，指定了用于签名的算法；而payload部分，则可以包含诸如用户id、过期时间等非敏感信息。Signature则是通过服务器端的密钥，结合header和payload内容，按照指定的签名算法进行计算得出的。
 *
 * 当服务器接收到客户端传来的token时，会首先提取出其中的header和payload部分，并使用密钥对它们进行签名计算。随后，将计算出的签名与token中提供的签名进行对比。如果两者一致，则说明该token是合法的。此外，通过payload部分，我们可以直接获取到用户的id，而无需像session那样从redis等存储中获取，从而提高了效率。
 *
 * 这种token生成与验证方式的好处在于，只要服务器端的密钥保持安全，那么生成的token就是安全的。因为任何试图伪造token的行为，在签名验证环节都会被揭穿，确保了token的合法性。同时，这种方式也实现了token的分布式存储，避免了必须保存在服务器的限制。
 */
public class JwtUtil {
    /**
     * 生成JWT
     * 使用Hs256算法，密钥使用固定密钥
     * @param secretKey 密钥
     * @param ttlmills 过期时间
     * @param claims 设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlmills, Map<String, Object> claims){
        // 指定签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlmills;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有声明，这个是给builder的claims赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的密钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token 揭秘
     * @param secretKey jwt密钥 此密钥一定要保留在服务端，不能暴露出去，否则sign就可以被伪造
     * @param token
     * @return
     */
    public static Claims parseJWT(String secretKey,String token){

        Claims claims = Jwts.parser()
                // 设置签名的密钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的JWT
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }






}
