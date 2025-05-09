package com.project.lotto.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lotto.user.dto.LoginDto;
import com.project.lotto.user.dto.SignUpDto;
import com.project.lotto.user.dto.TokenResponseDto;
import com.project.lotto.user.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @Mock
  private AuthService authService; // Mockito 가짜 빈

  @InjectMocks
  private AuthController authController; // @Mock 주입받을 컨트롤러

  private MockMvc mockMvc; //테스트할 MVC

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    //standaloneSetup으로 AuthController만 띄우기
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
  }

  @Test
  @DisplayName("회원가입 성공 시 201 CREATED 반환")
  void signUpSuccess() throws Exception {
    doNothing().when(authService).register(any(SignUpDto.class));

    mockMvc.perform(post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new SignUpDto("admin@gmail.com", "pass1234"))))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("로그인 성공 시 200 OK + JSON 토큰")
  void loginSuccess() throws Exception {
    String testToken = "jwt-test-token-123";
    when(authService.login(any(LoginDto.class))).thenReturn(testToken);

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new LoginDto("admin@gmail.com", "pass1234"))))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(new TokenResponseDto(testToken))));
  }

}


