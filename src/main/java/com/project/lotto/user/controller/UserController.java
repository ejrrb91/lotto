package com.project.lotto.user.controller;

import com.project.lotto.user.dto.UserResponseDto;
import com.project.lotto.user.entity.User;
import com.project.lotto.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/profile")
  public ResponseEntity<UserResponseDto> getProfile(Authentication authentication) {

    String username = authentication.getName();
    User user = userService.findByUsername(username);

    UserResponseDto userResponseDto = new UserResponseDto(
        user.getId(),
        user.getUsername(),
        user.getProvider(),
        user.getUserRole()
    );

    return ResponseEntity.ok(userResponseDto);
  }

}
