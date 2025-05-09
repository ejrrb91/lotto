package com.project.lotto.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lotto.user.dto.UserResponseDto;
import com.project.lotto.user.entity.User;
import com.project.lotto.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setIp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  @DisplayName("단위 테스트 : 프로필 조회 시 서비스 호출 및 DTO 매핑 검증")
  void getProfile() throws Exception {
    User user = User.builder()
                    .id(1L)
                    .username("user@gmail.com")
                    .provider("local")
                    .userRole("ROLE_USER")
                    .build();
    when(userService.findByUsername("user@gmail.com")).thenReturn(user);

    mockMvc.perform(get("/api/user/profile")
            .principal(new UsernamePasswordAuthenticationToken(
                "user@gmail.com", null, java.util.List.of(user.getRoleAuthority())
            ))
        )
        .andExpect(status().isOk())
        .andExpect(content().json(
            objectMapper.writeValueAsString(
                new UserResponseDto(1L, "user@gmail.com", "local", "ROLE_USER")
            )
        ));
  }

}
