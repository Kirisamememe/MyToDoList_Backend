//package com.example.mytodolist_backend.util;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//
//import javax.annotation.PostConstruct;
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secretKeyString;
//
//    private SecretKey secretKey;
//
//    @Value("${jwt.expiration}")
//    private Long expirationTime;
//
//    @PostConstruct
//    public void init() {
//        //HS512アルゴリズムを使用して秘密鍵を生成
//        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
//    }
//
//    public String generateToken(String username) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expirationTime);
//        return Jwts.builder().subject(username).issuedAt(now).expiration(expiryDate)
//                .signWith(secretKey)
//                .compact();
//    }
//
//    public Boolean validateToken(String token, String username) {
//        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
//    }
//
//    public String extractUsername(String token) {
//
//        return Jwts.parser()
//                .verifyWith(secretKey)
//                .build().parseSignedClaims(token).getPayload()
//                .getSubject();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        Date expiration = Jwts.parser()
//                .verifyWith(secretKey)
//                .build().parseSignedClaims(token).getPayload()
//                .getExpiration();
//        return expiration.before(new Date());
//    }
//}
//
