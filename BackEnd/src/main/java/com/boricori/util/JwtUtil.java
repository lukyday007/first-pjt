package com.boricori.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

  @PostConstruct
    protected void init(){
    System.out.println("secret: " + secret);
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createAccessToken(String email) {
      Claims claims = Jwts.claims().subject(email).build();
      long validFor = 1000 * 60 * 60; // 1 hour
      return Jwts.builder()
            .claims(claims)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + validFor))
            .signWith(secretKey)
            .compact();
    }


  public String createRefreshToken(String email) {
    Claims claims = Jwts.claims().subject(email).build();
    long validFor = 1000 * 60 * 60 * 24 * 14; // 2 week
    return Jwts.builder()
        .claims(claims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + validFor))
        .signWith(secretKey)
        .compact();
  }

  public Boolean isExpired(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
  }


  public String getEmail(String token) {
        try {
            Claims claim = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            return claim.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
