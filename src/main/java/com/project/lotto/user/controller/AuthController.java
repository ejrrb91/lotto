package com.project.lotto.user.controller;

import com.project.lotto.security.JwtTokenUtil;
import com.project.lotto.user.dto.LoginDto;
import com.project.lotto.user.dto.SignUpDto;
import com.project.lotto.user.dto.TokenResponseDto;
import com.project.lotto.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;


  /**
   * 로컬 회원가입
   * 성공 시 201 반환
   */
  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignUpDto signUpDto) {
    authService.register(signUpDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 로컬 로그인 -> JWT 발급
   * 200 + {token}
   */
  @PostMapping("/login")
  public ResponseEntity<TokenResponseDto> login (@Valid @RequestBody LoginDto loginDto) {
//    Authentication authentication = authenticationManager.authenticate(
//        new UsernamePasswordAuthenticationToken(
//            loginDto.getUsername(), loginDto.getPassword()
//        )
//    );

//    String token = jwtTokenUtil.generateToken(authentication.getName());
    String token = authService.login(loginDto);
    return ResponseEntity.ok(new TokenResponseDto(token));
  }

}
