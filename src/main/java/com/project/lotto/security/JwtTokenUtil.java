package com.project.lotto.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtil {

  private final Key secretKey;
  private final long expirationMs;

  public JwtTokenUtil(@Value("${app.jwt.secret}") String base64Secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
    byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationMs = expirationMs;
  }

  //subject(username)을 담아서 토큰 생성
  public String generateToken(String subject) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
          .setSubject(subject)
          .setIssuedAt(now)
          .setExpiration(expiry)
          .signWith(secretKey, SignatureAlgorithm.HS256)
          .compact();
  }

  //토큰에서 subject 추출
  public String getUsernameFromToken(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (JwtException e) {
      log.warn("Failed to parse JWT token : {}", e.getMessage());
      return null;
    }
  }

  //토큰 유효성(서명/만료) 검사
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      log.warn("Invalid JWT token : {}", e.getMessage());
      return false;
    }
  }

}
