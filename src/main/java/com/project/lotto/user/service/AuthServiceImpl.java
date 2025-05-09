package com.project.lotto.user.service;

import com.project.lotto.security.JwtTokenUtil;
import com.project.lotto.user.dto.LoginDto;
import com.project.lotto.user.dto.SignUpDto;
import com.project.lotto.user.entity.User;
import com.project.lotto.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  @Override
  public void register(SignUpDto signUpDto) {
    if (userRepository.existsByUsername(signUpDto.getUsername())) {
      throw new IllegalArgumentException("이미 가입된 사용자입니다.");
    }
    User user = User.builder()
        .username(signUpDto.getUsername())
        .password(passwordEncoder.encode(signUpDto.getPassword()))
        .provider("local")
        .providerId(null)
        .userRole(User.ROLE_USER)
        .build();

    userRepository.save(user);
  }

  @Override
  public String login(LoginDto loginDto) { //AuthService 인터페이스와 동일 시그니처
    // 1) 인증 시도
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginDto.getUsername(),
            loginDto.getPassword()
        )
    );

    // 2) 인증 성공 시 JWT 생성(인증된 사용자명 사용)
    String username = authentication.getName();
    return jwtTokenUtil.generateToken(username);
  }

}
