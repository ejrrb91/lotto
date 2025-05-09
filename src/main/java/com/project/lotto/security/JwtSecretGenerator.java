package com.project.lotto.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.KeyStore;


public class JwtSecretGenerator {

  public static void main(String[] args) {
    //HS256 알고리즘용 256비트 키 생성
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String base64Key = Encoders.BASE64.encode(key.getEncoded());
    System.out.println("base64Key : " + base64Key);
  }
}
