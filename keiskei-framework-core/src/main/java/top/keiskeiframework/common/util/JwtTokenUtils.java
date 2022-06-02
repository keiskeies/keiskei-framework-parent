package top.keiskeiframework.common.util;


import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * jwt工具类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/3/10 22:25
 */

public class JwtTokenUtils {

    public static int EXPIRES = 60 * 60 * 2;
    public static String SECRET = "secret";

    /**
     * 加密
     *
     * @param t   实体类
     * @param <T> 实体类
     * @return 。
     */
    public static <T> String getJwtString(T t) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user", t);
        return Jwts.builder()
                .setClaims(map)
                .setSubject(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRES * 1000L))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 解码
     *
     * @param token  密文
     * @param tClass 实体类
     * @param <T>    实体类
     * @return 。
     */
    public static <T> T parse(String token, Class<T> tClass) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            HashMap<?, ?> map = claims.get("user", HashMap.class);
            return JSON.parseObject(JSON.toJSONString(map), tClass);
        } catch (Exception e) {
            return null;
        }
    }


}