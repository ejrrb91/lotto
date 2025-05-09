package com.project.lotto.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.lotto.security.JwtTokenUtil;
import com.project.lotto.user.entity.User;
import com.project.lotto.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockmvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    userRepository.save(User.builder()
        .username("user@gmail.com")
        .password(passwordEncoder.encode("pass1234"))
        .provider("local")
        .providerId("")
        .userRole("ROLE_USER")
        .build());
  }

  @Test
  @DisplayName("통합 : 실제 JWT 토큰 발급 후 프로필 조회 성공")
  void getProfile_Token() throws Exception {
    // 실제 JwtTokenUtil을 사용해 토큰 생설
    String token = jwtTokenUtil.generateToken("user@gmail.com");

    mockmvc.perform(get("/api/user/profile")
        .header("Authorization", "Bearer " + token))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user@gmail.com"))
        .andExpect(jsonPath("$.provider").value("local"))
        .andExpect(jsonPath("$.role").value("ROLE_USER"));
  }
}
