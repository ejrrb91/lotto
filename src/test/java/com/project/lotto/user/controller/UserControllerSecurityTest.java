package com.project.lotto.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lotto.security.CustomOAuth2UserService;
import com.project.lotto.security.JwtTokenUtil;
import com.project.lotto.security.SecurityConfig;
import com.project.lotto.user.dto.UserResponseDto;
import com.project.lotto.user.entity.User;
import com.project.lotto.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, UserControllerSecurityTest.Mocks.class})
@AutoConfigureMockMvc(addFilters = true)
public class UserControllerSecurityTest {

  @TestConfiguration
  static class Mocks {

    @Bean
    JwtTokenUtil jwtTokenUtil() {
      return org.mockito.Mockito.mock(JwtTokenUtil.class);
    }

    @Bean
    UserDetailsService userDetailsService() {
      return org.mockito.Mockito.mock(UserDetailsService.class);
    }

    @Bean
    UserService userService() {
      return org.mockito.Mockito.mock(UserService.class);
    }

    @Bean
    CustomOAuth2UserService customOAuth2UserService() {
      return org.mockito.Mockito.mock(CustomOAuth2UserService.class);
    }
  }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String VALID_TOKEN = "valid.jwt.token";

    @BeforeEach
    void setUp() {
      when(jwtTokenUtil.validateToken(VALID_TOKEN)).thenReturn(true);
      when(jwtTokenUtil.getUsernameFromToken(VALID_TOKEN))
          .thenReturn("user@gmail.com");

      UserDetails userDetails = org.springframework.security.core.userdetails.User
          .withUsername("user@gmail.com")
          .password("pass12345")
          .authorities(new SimpleGrantedAuthority("ROLE_USER"))
          .build();

      when(userDetailsService.loadUserByUsername("user@gmail.com"))
          .thenReturn(userDetails);
    }

    @Test
    @DisplayName("보안 : 토큰 없으면 401 Unauthorized")
    void getProfile_withoutToken() throws Exception {
      mockMvc.perform(get("/api/user/profile"))
          .andDo(print())
          .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("보안 : 유효 토큰 있으면 200 + UserResponseDto JSON")
    void getProfile_ValidToken() throws Exception {
      User user = User.builder()
          .id(42L)
          .username("user@gmail.com")
          .provider("local")
          .providerId("")
          .userRole("ROLE_USER")
          .build();

      when(userService.findByUsername("user@gmail.com"))
          .thenReturn(user);

      UserResponseDto userResponseDto = new UserResponseDto(
          42L, "user@gmail.com", "local", "ROLE_USER"
      );

      mockMvc.perform(
              get("/api/user/profile")
                  .header("Authorization", "Bearer " + VALID_TOKEN)
          )
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(userResponseDto)));
    }
}
