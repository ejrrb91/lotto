package com.project.lotto.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {

  private Long id;

  private String username;

  /**
   * "local" 또는 "google"
   */
  private String provider;

  /**
   * "ROLE_USER" 또는 "ROLE_ADMIN"
   */
  private String role;
}
