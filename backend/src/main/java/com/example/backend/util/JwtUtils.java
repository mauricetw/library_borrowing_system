package com.example.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // JWT 密鑰 (為了面試方便直接寫死，實際專案應放在環境變數)
    // 必須是長度足夠的 Base64 字串以符合 HS256 演算法要求
    private static final String SECRET_KEY_BASE64 = "YmFja2VuZC1saWJyYXJ5LXN5c3RlbS1zZWNyZXQta2V5LXRlc3RpbmctcHVycG9zZQ==";

    // Token 有效期：這裡設定為 24 小時
    private static final long EXPIRATION_TIME = 86400000L;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY_BASE64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 產生 JWT Token (以手機號碼做為識別)
    public String generateToken(String phoneNumber) {
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 從 Token 中取得手機號碼
    public String getPhoneNumberFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 驗證 Token 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token 過期、被竄改或格式不正確
            return false;
        }
    }
}
